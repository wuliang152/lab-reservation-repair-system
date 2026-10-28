package com.example.labres.mapper;

import com.example.labres.domain.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} ORDER BY id DESC")
    List<Notification> findByUser(Long userId);

    @Select("SELECT * FROM notifications WHERE id = #{id}")
    Notification findById(Long id);

    @Insert("INSERT INTO notifications(user_id, title, content, is_read, related_id) VALUES(#{userId}, #{title}, #{content}, #{isRead}, #{relatedId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notification notification);

    @Update("UPDATE notifications SET is_read = TRUE WHERE id = #{id} AND user_id = #{userId}")
    int markRead(Long id, Long userId);
}
