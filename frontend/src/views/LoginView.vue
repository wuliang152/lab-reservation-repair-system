<template>
  <div class="login">
    <el-card class="card">
      <h2>实验室预约与报修系统</h2>
      <el-form :model="form" @keyup.enter="submit">
        <el-form-item><el-input v-model="form.username" placeholder="学号/工号" size="large" /></el-form-item>
        <el-form-item><el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password /></el-form-item>
        <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="submit">登录</el-button>
      </el-form>
      <p class="hint">演示账号：2024001 / 123456（普通用户）</p>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../store/auth'

const auth = useAuthStore()
const router = useRouter()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

async function submit() {
  if (!form.username || !form.password) return ElMessage.warning('请输入账号和密码')
  loading.value = true
  try {
    await auth.login(form.username, form.password)
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login { height: 100%; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #1f5c3a 0%, #2e7d52 100%); }
.card { width: 380px; }
.card h2 { text-align: center; margin: 8px 0 20px; }
.hint { color: #999; font-size: 12px; text-align: center; margin-top: 12px; }
</style>
