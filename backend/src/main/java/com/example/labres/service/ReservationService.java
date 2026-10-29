package com.example.labres.service;

import com.example.labres.common.BusinessException;
import com.example.labres.common.Constants;
import com.example.labres.domain.Equipment;
import com.example.labres.domain.Reservation;
import com.example.labres.domain.User;
import com.example.labres.dto.ReservationRequest;
import com.example.labres.mapper.EquipmentMapper;
import com.example.labres.mapper.ReservationMapper;
import com.example.labres.mapper.UserMapper;
import com.example.labres.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final EquipmentMapper equipmentMapper;
    private final UserMapper userMapper;
    private final NotificationService notificationService;

    public ReservationService(ReservationMapper reservationMapper, EquipmentMapper equipmentMapper,
                              UserMapper userMapper, NotificationService notificationService) {
        this.reservationMapper = reservationMapper;
        this.equipmentMapper = equipmentMapper;
        this.userMapper = userMapper;
        this.notificationService = notificationService;
    }

    public List<Map<String, Object>> list() {
        User current = userMapper.findByUsername(SecurityUtils.currentUsername());
        List<Reservation> list;
        if (Constants.ROLE_USER.equals(current.getRole())) {
            list = reservationMapper.findByUser(current.getId());
        } else {
            list = reservationMapper.findAll();
        }
        return list.stream().map(this::toMap).collect(Collectors.toList());
    }

    public List<Map<String, Object>> availability(Long equipmentId, LocalDateTime start, LocalDateTime end) {
        return equipmentMapper.findAll().stream()
                .filter(e -> Constants.EQUIPMENT_AVAILABLE.equals(e.getStatus()))
                .filter(e -> equipmentId == null || e.getId().equals(equipmentId))
                .filter(e -> reservationMapper.findConflict(e.getId(), start, end).isEmpty())
                .map(e -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", e.getId());
                    map.put("code", e.getCode());
                    map.put("name", e.getName());
                    return map;
                }).collect(Collectors.toList());
    }

    public Reservation create(ReservationRequest request) {
        if (request.startTime() == null || request.endTime() == null
                || !request.endTime().isAfter(request.startTime())) {
            throw new BusinessException("时间范围不合法");
        }
        Equipment equipment = equipmentMapper.findById(request.equipmentId());
        if (equipment == null || !Constants.EQUIPMENT_AVAILABLE.equals(equipment.getStatus())) {
            throw new BusinessException("设备不可预约");
        }
        if (!reservationMapper.findConflict(request.equipmentId(), request.startTime(), request.endTime()).isEmpty()) {
            throw new BusinessException("该时段已被预约");
        }
        User current = userMapper.findByUsername(SecurityUtils.currentUsername());
        Reservation r = new Reservation();
        r.setUserId(current.getId());
        r.setEquipmentId(request.equipmentId());
        r.setStartTime(request.startTime());
        r.setEndTime(request.endTime());
        r.setPurpose(request.purpose());
        r.setStatus(Constants.RESERVATION_PENDING);
        reservationMapper.insert(r);
        return r;
    }

    public void approve(Long id) {
        Reservation r = mustGet(id);
        if (!Constants.RESERVATION_PENDING.equals(r.getStatus())) {
            throw new BusinessException("只有待审批预约可以审批");
        }
        reservationMapper.updateStatus(id, Constants.RESERVATION_APPROVED);
        notificationService.notify(r.getUserId(), "预约已通过", "您的设备预约已审批通过", r.getId());
    }

    public void reject(Long id, String reason) {
        Reservation r = mustGet(id);
        if (!Constants.RESERVATION_PENDING.equals(r.getStatus())) {
            throw new BusinessException("只有待审批预约可以驳回");
        }
        reservationMapper.updateStatusWithReason(id, Constants.RESERVATION_REJECTED, reason);
        notificationService.notify(r.getUserId(), "预约被驳回", "驳回理由：" + reason, r.getId());
    }

    public void cancel(Long id) {
        Reservation r = mustGet(id);
        if (!Constants.RESERVATION_PENDING.equals(r.getStatus())
                && !Constants.RESERVATION_APPROVED.equals(r.getStatus())) {
            throw new BusinessException("当前状态不能取消");
        }
        User current = userMapper.findByUsername(SecurityUtils.currentUsername());
        if (!r.getUserId().equals(current.getId())
                && !Constants.ROLE_LAB_ADMIN.equals(current.getRole())
                && !Constants.ROLE_SYS_ADMIN.equals(current.getRole())) {
            throw new BusinessException(403, "只能取消自己的预约");
        }
        reservationMapper.updateStatus(id, Constants.RESERVATION_CANCELLED);
    }

    private Reservation mustGet(Long id) {
        Reservation r = reservationMapper.findById(id);
        if (r == null) {
            throw new BusinessException("预约不存在");
        }
        return r;
    }

    private Map<String, Object> toMap(Reservation r) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", r.getId());
        map.put("userId", r.getUserId());
        map.put("equipmentId", r.getEquipmentId());
        map.put("startTime", r.getStartTime());
        map.put("endTime", r.getEndTime());
        map.put("purpose", r.getPurpose());
        map.put("status", r.getStatus());
        map.put("rejectReason", r.getRejectReason());
        Equipment e = equipmentMapper.findById(r.getEquipmentId());
        map.put("equipmentCode", e == null ? "" : e.getCode());
        map.put("equipmentName", e == null ? "" : e.getName());
        User u = userMapper.findById(r.getUserId());
        map.put("userName", u == null ? "" : u.getName());
        return map;
    }
}
