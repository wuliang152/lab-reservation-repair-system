-- 演示数据（密码使用 BCrypt 加密，对应明文 123456）
-- 如果使用后端 H2 模式，不需要执行本文件；后端会自动写入相同数据。
USE lab_reservation;

INSERT INTO users(username, password, name, role, status) VALUES
('2024001', '$2a$10$EIX/Q8KQbYV1Y3vV8V5A5u6mW9e8WjV1t3N2M6o4K7G8e5K5O2W6W', '张三', 'USER', 'ACTIVE'),
('admin',   '$2a$10$EIX/Q8KQbYV1Y3vV8V5A5u6mW9e8WjV1t3N2M6o4K7G8e5K5O2W6W', '李管理', 'LAB_ADMIN', 'ACTIVE'),
('tech',    '$2a$10$EIX/Q8KQbYV1Y3vV8V5A5u6mW9e8WjV1t3N2M6o4K7G8e5K5O2W6W', '王维修', 'TECHNICIAN', 'ACTIVE'),
('sys',     '$2a$10$EIX/Q8KQbYV1Y3vV8V5A5u6mW9e8WjV1t3N2M6o4K7G8e5K5O2W6W', '赵系统', 'SYS_ADMIN', 'ACTIVE');

INSERT INTO labs(name, location, description) VALUES
('计算机实验室', '实验楼A201', '40台计算机'),
('电子实验室', '实验楼B305', '示波器与电源');

INSERT INTO equipment(lab_id, code, name, model, category, status, purchase_date) VALUES
(1, 'EQ-001', '戴尔工作站', 'T5820', '计算机', 'AVAILABLE', '2023-09-01'),
(1, 'EQ-002', '联想台式机', 'M450', '计算机', 'AVAILABLE', '2023-09-01'),
(2, 'EQ-003', '数字示波器', 'DS1054Z', '仪器', 'AVAILABLE', '2024-03-10'),
(2, 'EQ-004', '直流稳压电源', 'DP832', '仪器', 'REPAIRING', '2024-03-10');
