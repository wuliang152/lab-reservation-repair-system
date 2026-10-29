# 接口契约

统一前缀：`/api/v1`  
统一返回格式：

```json
{ "code": 0, "message": "ok", "data": {} }
```

错误码：`400` 参数错误、`401` 未登录、`403` 越权、`409` 业务冲突、`500` 服务器错误。

## 认证

- `POST /auth/login` 登录
- `GET /auth/me` 当前用户

## 用户

- `GET /users` 用户列表（管理员）
- `PATCH /users/{id}/status` 停用/启用（系统管理员）

## 实验室与设备

- `GET /labs`
- `POST /labs`、`PATCH /labs/{id}`、`DELETE /labs/{id}`（管理员）
- `GET /equipment?keyword=&labId=&status=`
- `POST /equipment`、`PATCH /equipment/{id}`、`DELETE /equipment/{id}`（管理员）

## 预约

- `GET /reservations/availability?equipmentId=&start=&end=`
- `GET /reservations`
- `POST /reservations`
- `POST /reservations/{id}/approve`（管理员）
- `POST /reservations/{id}/reject`（管理员）
- `POST /reservations/{id}/cancel`

## 报修

- `GET /repairs`
- `POST /repairs`
- `POST /repairs/{id}/assign`（管理员）
- `POST /repairs/{id}/accept`（维修人员）
- `POST /repairs/{id}/complete`（维修人员）
- `POST /repairs/{id}/confirm`（报修人）

## 通知与统计

- `GET /notifications`
- `PATCH /notifications/{id}/read`
- `GET /dashboard/summary`
