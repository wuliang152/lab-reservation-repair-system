package com.example.labres.mapper;

import com.example.labres.domain.RepairOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RepairMapper {
    @Select("SELECT * FROM repair_orders ORDER BY id DESC")
    List<RepairOrder> findAll();

    @Select("SELECT * FROM repair_orders WHERE user_id = #{userId} ORDER BY id DESC")
    List<RepairOrder> findByUser(Long userId);

    @Select("SELECT * FROM repair_orders WHERE technician_id = #{technicianId} ORDER BY id DESC")
    List<RepairOrder> findByTechnician(Long technicianId);

    @Select("SELECT * FROM repair_orders WHERE id = #{id}")
    RepairOrder findById(Long id);

    @Insert("INSERT INTO repair_orders(user_id, equipment_id, technician_id, urgency, description, status) " +
            "VALUES(#{userId}, #{equipmentId}, #{technicianId}, #{urgency}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RepairOrder order);

    @Update("UPDATE repair_orders SET technician_id = #{technicianId}, status = #{status}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    int assign(Long id, Long technicianId, String status);

    @Update("UPDATE repair_orders SET status = #{status}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    int updateStatus(Long id, String status);

    @Update("UPDATE repair_orders SET status = #{status}, result = #{result}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    int complete(Long id, String status, String result);

    @Select("SELECT COUNT(*) FROM repair_orders WHERE status = #{status}")
    long countByStatus(String status);
}
