package com.example.labres.mapper;

import com.example.labres.domain.Equipment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EquipmentMapper {
    @Select("<script>SELECT * FROM equipment WHERE 1=1" +
            "<if test='keyword != null'> AND (name LIKE CONCAT('%', #{keyword}, '%') OR code LIKE CONCAT('%', #{keyword}, '%'))</if>" +
            "<if test='labId != null'> AND lab_id = #{labId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            " ORDER BY id</script>")
    List<Equipment> findByCondition(String keyword, Long labId, String status);

    @Select("SELECT * FROM equipment ORDER BY id")
    List<Equipment> findAll();

    @Select("SELECT * FROM equipment WHERE id = #{id}")
    Equipment findById(Long id);

    @Insert("INSERT INTO equipment(lab_id, code, name, model, category, status, purchase_date) VALUES(#{labId}, #{code}, #{name}, #{model}, #{category}, #{status}, #{purchaseDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Equipment equipment);

    @Update("UPDATE equipment SET lab_id = #{labId}, code = #{code}, name = #{name}, model = #{model}, category = #{category}, status = #{status}, purchase_date = #{purchaseDate} WHERE id = #{id}")
    int update(Equipment equipment);

    @Delete("DELETE FROM equipment WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT COUNT(*) FROM equipment")
    long count();

    @Select("SELECT COUNT(*) FROM equipment WHERE status = #{status}")
    long countByStatus(String status);
}
