package com.example.labres.mapper;

import com.example.labres.domain.Reservation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationMapper {
    @Select("SELECT * FROM reservations ORDER BY id DESC")
    List<Reservation> findAll();

    @Select("SELECT * FROM reservations WHERE user_id = #{userId} ORDER BY id DESC")
    List<Reservation> findByUser(Long userId);

    @Select("SELECT * FROM reservations WHERE id = #{id}")
    Reservation findById(Long id);

    @Select("SELECT * FROM reservations WHERE equipment_id = #{equipmentId} AND status IN ('PENDING','APPROVED') " +
            "AND NOT (end_time <= #{start} OR start_time >= #{end})")
    List<Reservation> findConflict(Long equipmentId, LocalDateTime start, LocalDateTime end);

    @Insert("INSERT INTO reservations(user_id, equipment_id, start_time, end_time, purpose, status) " +
            "VALUES(#{userId}, #{equipmentId}, #{startTime}, #{endTime}, #{purpose}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reservation reservation);

    @Update("UPDATE reservations SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, String status);

    @Update("UPDATE reservations SET status = #{status}, reject_reason = #{reason} WHERE id = #{id}")
    int updateStatusWithReason(Long id, String status, String reason);

    @Select("SELECT COUNT(*) FROM reservations WHERE status = #{status}")
    long countByStatus(String status);
}
