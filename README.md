# 实验室预约与报修系统

软件工程基础课程第九组项目：校园实验室设备预约与报修系统。

## 技术栈

- 前端：Vue 3 + Vite + Element Plus + Pinia + Axios + ECharts
- 后端：Spring Boot 3 + MyBatis + Spring Security + JWT
- 数据库：开发演示使用 H2 内存库（MySQL 模式），生产使用 MySQL 8

## 目录结构

```
frontend/        Vue 3 前端
backend/         Spring Boot 后端
database/        MySQL 建表脚本与初始化数据
docs/            需求、API、数据库说明
```

## 快速启动

### 1. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端默认使用 H2 内存数据库，首次启动自动建表和写入演示数据。

### 2. 启动前端

```bash
cd frontend
npm install
npm run dev
```

浏览器打开 http://localhost:5173

### 3. 演示账号

| 账号 | 密码 | 角色 |
|---|---|---|
| 2024001 | 123456 | 普通用户 |
| admin | 123456 | 实验室管理员 |
| tech | 123456 | 维修人员 |
| sys | 123456 | 系统管理员 |

## 切换 MySQL

1. 确保 MySQL 8 已启动，并已创建数据库：

```sql
CREATE DATABASE lab_reservation CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'lab'@'localhost' IDENTIFIED BY '你的密码';
GRANT ALL PRIVILEGES ON lab_reservation.* TO 'lab'@'localhost';
FLUSH PRIVILEGES;
```

2. 设置数据库连接环境变量：

```powershell
$env:DB_USERNAME = "lab"
$env:DB_PASSWORD = "你的密码"
```

3. 启动后端并启用 mysql profile：

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

后端会自动执行 `schema-mysql.sql` 建表，并在首次启动时写入演示数据。

## 构建

```bash
cd backend
mvn test

cd frontend
npm run build
```
