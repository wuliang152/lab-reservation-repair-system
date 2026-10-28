package com.example.labres.service;

import com.example.labres.common.Constants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(JdbcTemplate jdbc, PasswordEncoder passwordEncoder) {
        this.jdbc = jdbc;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Long count = jdbc.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        if (count != null && count > 0) {
            return;
        }
        String pwd = passwordEncoder.encode("123456");
        Long user1 = insertUser("2024001", pwd, "张三", Constants.ROLE_USER);
        Long admin = insertUser("admin", pwd, "李管理", Constants.ROLE_LAB_ADMIN);
        Long tech = insertUser("tech", pwd, "王维修", Constants.ROLE_TECHNICIAN);
        insertUser("sys", pwd, "赵系统", Constants.ROLE_SYS_ADMIN);

        jdbc.update("INSERT INTO labs(name, location, description) VALUES(?,?,?)", "计算机实验室", "实验楼A201", "40台计算机");
        jdbc.update("INSERT INTO labs(name, location, description) VALUES(?,?,?)", "电子实验室", "实验楼B305", "示波器与电源");

        jdbc.update("INSERT INTO equipment(lab_id, code, name, model, category, status, purchase_date) VALUES(?,?,?,?,?,?,?)",
                1L, "EQ-001", "戴尔工作站", "T5820", "计算机", "AVAILABLE", java.sql.Date.valueOf("2023-09-01"));
        jdbc.update("INSERT INTO equipment(lab_id, code, name, model, category, status, purchase_date) VALUES(?,?,?,?,?,?,?)",
                1L, "EQ-002", "联想台式机", "M450", "计算机", "AVAILABLE", java.sql.Date.valueOf("2023-09-01"));
        jdbc.update("INSERT INTO equipment(lab_id, code, name, model, category, status, purchase_date) VALUES(?,?,?,?,?,?,?)",
                2L, "EQ-003", "数字示波器", "DS1054Z", "仪器", "AVAILABLE", java.sql.Date.valueOf("2024-03-10"));
        jdbc.update("INSERT INTO equipment(lab_id, code, name, model, category, status, purchase_date) VALUES(?,?,?,?,?,?,?)",
                2L, "EQ-004", "直流稳压电源", "DP832", "仪器", "REPAIRING", java.sql.Date.valueOf("2024-03-10"));

        jdbc.update("INSERT INTO reservations(user_id, equipment_id, start_time, end_time, purpose, status) VALUES(?,?,?,?,?,?)",
                user1, 1L, java.sql.Timestamp.valueOf("2026-10-29 09:00:00"), java.sql.Timestamp.valueOf("2026-10-29 11:00:00"), "课程实验", "PENDING");
        jdbc.update("INSERT INTO repair_orders(user_id, equipment_id, technician_id, urgency, description, status) VALUES(?,?,?,?,?,?)",
                user1, 4L, tech, "URGENT", "输出电压不稳定", "ASSIGNED");
    }

    private Long insertUser(String username, String password, String name, String role) {
        jdbc.update("INSERT INTO users(username, password, name, role, status) VALUES(?,?,?,?,?)",
                username, password, name, role, Constants.USER_ACTIVE);
        return jdbc.queryForObject("SELECT id FROM users WHERE username = ?", Long.class, username);
    }
}
