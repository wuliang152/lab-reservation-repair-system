package com.example.labres.service;

import com.example.labres.common.BusinessException;
import com.example.labres.common.Constants;
import com.example.labres.domain.Equipment;
import com.example.labres.domain.RepairLog;
import com.example.labres.domain.RepairOrder;
import com.example.labres.domain.User;
import com.example.labres.dto.RepairRequest;
import com.example.labres.mapper.EquipmentMapper;
import com.example.labres.mapper.RepairLogMapper;
import com.example.labres.mapper.RepairMapper;
import com.example.labres.mapper.UserMapper;
import com.example.labres.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepairService {

    private final RepairMapper repairMapper;
    private final RepairLogMapper repairLogMapper;
    private final EquipmentMapper equipmentMapper;
    private final UserMapper userMapper;
    private final NotificationService notificationService;

    public RepairService(RepairMapper repairMapper, RepairLogMapper repairLogMapper,
                         EquipmentMapper equipmentMapper, UserMapper userMapper,
                         NotificationService notificationService) {
        this.repairMapper = repairMapper;
        this.repairLogMapper = repairLogMapper;
        this.equipmentMapper = equipmentMapper;
        this.userMapper = userMapper;
        this.notificationService = notificationService;
    }

    public List<Map<String, Object>> list() {
        User current = userMapper.findByUsername(SecurityUtils.currentUsername());
        List<RepairOrder> list;
        if (Constants.ROLE_USER.equals(current.getRole())) {
            list = repairMapper.findByUser(current.getId());
        } else if (Constants.ROLE_TECHNICIAN.equals(current.getRole())) {
            list = repairMapper.findByTechnician(current.getId());
        } else {
            list = repairMapper.findAll();
        }
        return list.stream().map(this::toMap).collect(Collectors.toList());
    }

    public RepairOrder create(RepairRequest request) {
        if (request.equipmentId() == null || request.description() == null || request.description().isBlank()) {
            throw new BusinessException("设备和故障描述不能为空");
        }
        User current = userMapper.findByUsername(SecurityUtils.currentUsername());
        RepairOrder order = new RepairOrder();
        order.setUserId(current.getId());
        order.setEquipmentId(request.equipmentId());
        order.setUrgency(request.urgency() == null ? "NORMAL" : request.urgency());
        order.setDescription(request.description());
        order.setStatus(Constants.REPAIR_PENDING);
        repairMapper.insert(order);
        return order;
    }

    public void assign(Long id, Long technicianId) {
        RepairOrder order = mustGet(id);
        if (!Constants.REPAIR_PENDING.equals(order.getStatus())) {
            throw new BusinessException("只有待派单报修可以派单");
        }
        User tech = userMapper.findById(technicianId);
        if (tech == null || !Constants.ROLE_TECHNICIAN.equals(tech.getRole())) {
            throw new BusinessException("维修人员不存在");
        }
        repairMapper.assign(id, technicianId, Constants.REPAIR_ASSIGNED);
        log(order, Constants.REPAIR_ASSIGNED, "派单给 " + tech.getName());
        notificationService.notify(technicianId, "新维修任务", "有一条报修单等待处理", id);
    }

    public void accept(Long id) {
        transition(id, Constants.REPAIR_ASSIGNED, Constants.REPAIR_REPAIRING, "接单");
    }

    public void start(Long id) {
        transition(id, Constants.REPAIR_ASSIGNED, Constants.REPAIR_REPAIRING, "开始维修");
    }

    public void complete(Long id, String result) {
        RepairOrder order = mustGet(id);
        if (!Constants.REPAIR_REPAIRING.equals(order.getStatus())) {
            throw new BusinessException("只有维修中的报修可以完成");
        }
        repairMapper.complete(id, Constants.REPAIR_COMPLETED, result);
        log(order, Constants.REPAIR_COMPLETED, result);
        notificationService.notify(order.getUserId(), "维修完成", "您的报修已维修完成，请确认", id);
    }

    public void confirm(Long id) {
        RepairOrder order = mustGet(id);
        if (!Constants.REPAIR_COMPLETED.equals(order.getStatus())) {
            throw new BusinessException("只有已完成报修可以确认");
        }
        User current = userMapper.findByUsername(SecurityUtils.currentUsername());
        if (!order.getUserId().equals(current.getId())) {
            throw new BusinessException(403, "只能确认自己的报修单");
        }
        repairMapper.updateStatus(id, Constants.REPAIR_CONFIRMED);
        log(order, Constants.REPAIR_CONFIRMED, "用户确认");
    }

    private void transition(Long id, String from, String to, String note) {
        RepairOrder order = mustGet(id);
        if (!from.equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许该操作");
        }
        repairMapper.updateStatus(id, to);
        log(order, to, note);
    }

    private void log(RepairOrder order, String to, String note) {
        RepairLog log = new RepairLog();
        log.setRepairId(order.getId());
        log.setFromStatus(order.getStatus());
        log.setToStatus(to);
        User operator = userMapper.findByUsername(SecurityUtils.currentUsername());
        log.setOperatorId(operator == null ? null : operator.getId());
        log.setNote(note);
        repairLogMapper.insert(log);
    }

    private RepairOrder mustGet(Long id) {
        RepairOrder order = repairMapper.findById(id);
        if (order == null) {
            throw new BusinessException("报修单不存在");
        }
        return order;
    }

    private Map<String, Object> toMap(RepairOrder o) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", o.getId());
        map.put("userId", o.getUserId());
        map.put("equipmentId", o.getEquipmentId());
        map.put("technicianId", o.getTechnicianId());
        map.put("urgency", o.getUrgency());
        map.put("description", o.getDescription());
        map.put("status", o.getStatus());
        map.put("result", o.getResult());
        map.put("createdAt", o.getCreatedAt());
        Equipment e = equipmentMapper.findById(o.getEquipmentId());
        map.put("equipmentName", e == null ? "" : e.getName());
        map.put("equipmentCode", e == null ? "" : e.getCode());
        User u = userMapper.findById(o.getUserId());
        map.put("userName", u == null ? "" : u.getName());
        User t = o.getTechnicianId() == null ? null : userMapper.findById(o.getTechnicianId());
        map.put("technicianName", t == null ? "" : t.getName());
        map.put("logs", repairLogMapper.findByRepair(o.getId()));
        return map;
    }
}
