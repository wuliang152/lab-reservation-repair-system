package com.example.labres.mapper;

import com.example.labres.domain.RepairLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RepairLogMapper {
    @Insert("INSERT INTO repair_logs(repair_id, from_status, to_status, operator_id, note) VALUES(#{repairId}, #{fromStatus}, #{toStatus}, #{operatorId}, #{note})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RepairLog log);

    @Select("SELECT * FROM repair_logs WHERE repair_id = #{repairId} ORDER BY id")
    List<RepairLog> findByRepair(Long repairId);
}
