package com.example.labres.mapper;

import com.example.labres.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM users ORDER BY id")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE role = #{role} AND status = 'ACTIVE'")
    List<User> findByRole(String role);

    @Insert("INSERT INTO users(username, password, name, role, status) VALUES(#{username}, #{password}, #{name}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE users SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, String status);

    @Select("SELECT COUNT(*) FROM users")
    long count();
}
