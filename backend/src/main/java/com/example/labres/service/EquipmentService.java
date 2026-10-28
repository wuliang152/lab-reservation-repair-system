package com.example.labres.service;

import com.example.labres.common.BusinessException;
import com.example.labres.domain.Equipment;
import com.example.labres.domain.Lab;
import com.example.labres.dto.EquipmentRequest;
import com.example.labres.mapper.EquipmentMapper;
import com.example.labres.mapper.LabMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentMapper equipmentMapper;
    private final LabMapper labMapper;

    public EquipmentService(EquipmentMapper equipmentMapper, LabMapper labMapper) {
        this.equipmentMapper = equipmentMapper;
        this.labMapper = labMapper;
    }

    public List<Map<String, Object>> list(String keyword, Long labId, String status) {
        return equipmentMapper.findByCondition(keyword, labId, status).stream()
                .map(this::toMap).collect(Collectors.toList());
    }

    public Equipment create(EquipmentRequest request) {
        Equipment equipment = new Equipment();
        equipment.setLabId(request.labId());
        equipment.setCode(request.code());
        equipment.setName(request.name());
        equipment.setModel(request.model());
        equipment.setCategory(request.category());
        equipment.setStatus(request.status() == null ? "AVAILABLE" : request.status());
        equipment.setPurchaseDate(LocalDate.now());
        equipmentMapper.insert(equipment);
        return equipment;
    }

    public Equipment update(Long id, EquipmentRequest request) {
        Equipment equipment = equipmentMapper.findById(id);
        if (equipment == null) {
            throw new BusinessException("设备不存在");
        }
        equipment.setLabId(request.labId());
        equipment.setCode(request.code());
        equipment.setName(request.name());
        equipment.setModel(request.model());
        equipment.setCategory(request.category());
        equipment.setStatus(request.status());
        equipmentMapper.update(equipment);
        return equipment;
    }

    public void delete(Long id) {
        equipmentMapper.delete(id);
    }

    public Map<String, Object> toMap(Equipment e) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", e.getId());
        map.put("labId", e.getLabId());
        map.put("code", e.getCode());
        map.put("name", e.getName());
        map.put("model", e.getModel());
        map.put("category", e.getCategory());
        map.put("status", e.getStatus());
        map.put("purchaseDate", e.getPurchaseDate());
        Lab lab = labMapper.findById(e.getLabId());
        map.put("labName", lab == null ? "" : lab.getName());
        return map;
    }
}
