package com.example.labres.service;

import com.example.labres.common.Constants;
import com.example.labres.mapper.EquipmentMapper;
import com.example.labres.mapper.RepairMapper;
import com.example.labres.mapper.ReservationMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final EquipmentMapper equipmentMapper;
    private final ReservationMapper reservationMapper;
    private final RepairMapper repairMapper;

    public DashboardService(EquipmentMapper equipmentMapper, ReservationMapper reservationMapper, RepairMapper repairMapper) {
        this.equipmentMapper = equipmentMapper;
        this.reservationMapper = reservationMapper;
        this.repairMapper = repairMapper;
    }

    public Map<String, Object> summary() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("equipmentTotal", equipmentMapper.count());
        map.put("availableEquipment", equipmentMapper.countByStatus(Constants.EQUIPMENT_AVAILABLE));
        map.put("pendingReservations", reservationMapper.countByStatus(Constants.RESERVATION_PENDING));
        map.put("pendingRepairs", repairMapper.countByStatus(Constants.REPAIR_PENDING));
        map.put("equipmentStatus", equipmentStatus());
        map.put("repairStatus", repairStatus());
        return map;
    }

    private List<Map<String, Object>> equipmentStatus() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(statusItem("可用", equipmentMapper.countByStatus(Constants.EQUIPMENT_AVAILABLE)));
        list.add(statusItem("维修中", equipmentMapper.countByStatus(Constants.EQUIPMENT_REPAIRING)));
        list.add(statusItem("报废", equipmentMapper.countByStatus(Constants.EQUIPMENT_RETIRED)));
        return list;
    }

    private List<Map<String, Object>> repairStatus() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(statusItem("待派单", repairMapper.countByStatus(Constants.REPAIR_PENDING)));
        list.add(statusItem("已派单", repairMapper.countByStatus(Constants.REPAIR_ASSIGNED)));
        list.add(statusItem("维修中", repairMapper.countByStatus(Constants.REPAIR_REPAIRING)));
        list.add(statusItem("已完成", repairMapper.countByStatus(Constants.REPAIR_COMPLETED)));
        list.add(statusItem("已确认", repairMapper.countByStatus(Constants.REPAIR_CONFIRMED)));
        return list;
    }

    private Map<String, Object> statusItem(String name, long value) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }
}
