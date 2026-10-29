package com.example.labres.mapper;

import com.example.labres.domain.Lab;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LabMapper {
    @Select("SELECT * FROM labs ORDER BY id")
    List<Lab> findAll();

    @Select("SELECT * FROM labs WHERE id = #{id}")
    Lab findById(Long id);

    @Insert("INSERT INTO labs(name, location, description) VALUES(#{name}, #{location}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Lab lab);

    @Update("UPDATE labs SET name = #{name}, location = #{location}, description = #{description} WHERE id = #{id}")
    int update(Lab lab);

    @Delete("DELETE FROM labs WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT COUNT(*) FROM labs")
    long count();
}
