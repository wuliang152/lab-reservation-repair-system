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

1. 安装并启动 MySQL 8。
2. 执行 `database/schema.sql` 创建表结构。
3. 执行 `database/init-data.sql` 或按 README 使用后端 `DataInitializer` 自动写入。
4. 启动后端时指定 MySQL 配置：

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

并将 `backend/src/main/resources/application-mysql.yml` 中的数据库密码改成真实值。

## 构建

```bash
cd backend
mvn test

cd frontend
npm run build
```
