<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">实验室预约与报修系统</div>
      <el-menu :default-active="$route.path" router background-color="#1f2d3d" text-color="#c0c4cc" active-text-color="#ffffff">
        <el-menu-item index="/dashboard"><el-icon><Odometer /></el-icon><span>工作台</span></el-menu-item>
        <el-menu-item index="/equipment"><el-icon><Monitor /></el-icon><span>设备管理</span></el-menu-item>
        <el-menu-item index="/reservations"><el-icon><Calendar /></el-icon><span>预约管理</span></el-menu-item>
        <el-menu-item index="/repairs"><el-icon><Tools /></el-icon><span>报修管理</span></el-menu-item>
        <el-menu-item index="/notifications"><el-icon><Bell /></el-icon><span>消息通知</span></el-menu-item>
        <el-menu-item v-if="auth.role === 'SYS_ADMIN'" index="/users"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="title">{{ $route.meta.title || '工作台' }}</div>
        <div class="right">
          <el-tag size="small">{{ roleText }}</el-tag>
          <span class="user">{{ auth.user?.name }}</span>
          <el-button link type="primary" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main"><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const auth = useAuthStore()
const router = useRouter()
const roleMap = {
  USER: '普通用户',
  LAB_ADMIN: '实验室管理员',
  TECHNICIAN: '维修人员',
  SYS_ADMIN: '系统管理员'
}
const roleText = computed(() => roleMap[auth.role] || auth.role)

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100%; }
.aside { background: #1f2d3d; }
.logo { height: 60px; line-height: 60px; text-align: center; color: #fff; font-weight: bold; }
.aside :deep(.el-menu) { border-right: none; }
.header { display: flex; align-items: center; justify-content: space-between; background: #fff; border-bottom: 1px solid #e5e9ef; }
.header .title { font-size: 18px; font-weight: 600; }
.right { display: flex; align-items: center; gap: 12px; }
.user { color: #333; }
.main { background: #f5f7fa; }
</style>
