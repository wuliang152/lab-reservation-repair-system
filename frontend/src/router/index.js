import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'

const routes = [
  { path: '/login', name: 'login', component: () => import('../views/LoginView.vue'), meta: { public: true } },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', component: () => import('../views/DashboardView.vue'), meta: { title: '工作台' } },
      { path: 'equipment', component: () => import('../views/EquipmentView.vue'), meta: { title: '设备管理' } },
      { path: 'reservations', component: () => import('../views/ReservationView.vue'), meta: { title: '预约管理' } },
      { path: 'repairs', component: () => import('../views/RepairView.vue'), meta: { title: '报修管理' } },
      { path: 'notifications', component: () => import('../views/NotificationsView.vue'), meta: { title: '消息通知' } },
      { path: 'users', component: () => import('../views/UsersView.vue'), meta: { title: '用户管理', roles: ['SYS_ADMIN'] } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()
  if (to.meta.public) return true
  if (!auth.isLoggedIn) return '/login'
  if (!auth.user) {
    try {
      await auth.fetchMe()
    } catch (e) {
      auth.logout()
      return '/login'
    }
  }
  if (to.meta.roles && !to.meta.roles.includes(auth.role)) {
    return '/dashboard'
  }
  return true
})

export default router
