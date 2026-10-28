# 数据库设计

核心表：`users`、`labs`、`equipment`、`reservations`、`repair_orders`、`notifications`、`repair_logs`。

## 状态枚举

- 用户状态：`ACTIVE`、`DISABLED`
- 角色：`USER`、`LAB_ADMIN`、`TECHNICIAN`、`SYS_ADMIN`
- 设备状态：`AVAILABLE`、`REPAIRING`、`RETIRED`
- 预约状态：`PENDING`、`APPROVED`、`REJECTED`、`CANCELLED`、`COMPLETED`
- 报修状态：`PENDING`、`ASSIGNED`、`REPAIRING`、`COMPLETED`、`CONFIRMED`

## 关系

- 用户 1-N 预约
- 设备 1-N 预约
- 用户 1-N 报修单
- 设备 1-N 报修单
- 报修单 1-N 状态日志
