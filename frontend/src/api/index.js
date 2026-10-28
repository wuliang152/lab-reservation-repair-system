import http from './http'

export const authApi = {
  login: (data) => http.post('/auth/login', data),
  me: () => http.get('/auth/me')
}

export const userApi = {
  list: () => http.get('/users'),
  updateStatus: (id, status) => http.patch(`/users/${id}/status`, { status })
}

export const labApi = {
  list: () => http.get('/labs'),
  create: (data) => http.post('/labs', data),
  update: (id, data) => http.patch(`/labs/${id}`, data),
  remove: (id) => http.delete(`/labs/${id}`)
}

export const equipmentApi = {
  list: (params) => http.get('/equipment', { params }),
  create: (data) => http.post('/equipment', data),
  update: (id, data) => http.patch(`/equipment/${id}`, data),
  remove: (id) => http.delete(`/equipment/${id}`)
}

export const reservationApi = {
  availability: (equipmentId, date) =>
    http.get('/reservations/availability', { params: { equipmentId, date } }),
  list: () => http.get('/reservations'),
  create: (data) => http.post('/reservations', data),
  approve: (id) => http.post(`/reservations/${id}/approve`),
  reject: (id, reason) => http.post(`/reservations/${id}/reject`, { reason }),
  cancel: (id) => http.post(`/reservations/${id}/cancel`)
}

export const repairApi = {
  list: () => http.get('/repairs'),
  create: (data) => http.post('/repairs', data),
  assign: (id, technicianId) => http.post(`/repairs/${id}/assign`, { technicianId }),
  accept: (id) => http.post(`/repairs/${id}/accept`),
  start: (id) => http.post(`/repairs/${id}/start`),
  complete: (id, result) => http.post(`/repairs/${id}/complete`, { result }),
  confirm: (id) => http.post(`/repairs/${id}/confirm`)
}

export const notificationApi = {
  list: () => http.get('/notifications'),
  read: (id) => http.patch(`/notifications/${id}/read`)
}

export const dashboardApi = {
  summary: () => http.get('/dashboard/summary')
}
