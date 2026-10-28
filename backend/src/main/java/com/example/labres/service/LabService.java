package com.example.labres.service;

import com.example.labres.common.BusinessException;
import com.example.labres.domain.Lab;
import com.example.labres.dto.LabRequest;
import com.example.labres.mapper.LabMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabService {

    private final LabMapper labMapper;

    public LabService(LabMapper labMapper) {
        this.labMapper = labMapper;
    }

    public List<Lab> list() {
        return labMapper.findAll();
    }

    public Lab create(LabRequest request) {
        Lab lab = new Lab();
        lab.setName(request.name());
        lab.setLocation(request.location());
        lab.setDescription(request.description());
        labMapper.insert(lab);
        return lab;
    }

    public Lab update(Long id, LabRequest request) {
        Lab lab = labMapper.findById(id);
        if (lab == null) {
            throw new BusinessException("实验室不存在");
        }
        lab.setName(request.name());
        lab.setLocation(request.location());
        lab.setDescription(request.description());
        labMapper.update(lab);
        return lab;
    }

    public void delete(Long id) {
        labMapper.delete(id);
    }
}
