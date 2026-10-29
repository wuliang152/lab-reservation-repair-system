<template>
  <el-card>
    <el-table :data="list" border stripe>
      <el-table-column prop="username" label="账号" width="160" />
      <el-table-column prop="name" label="姓名" width="160" />
      <el-table-column prop="role" label="角色" width="160">
        <template #default="{ row }">{{ roleText(row.role) }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }"><el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">{{ row.status === 'ACTIVE' ? '启用' : '停用' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button link :type="row.status === 'ACTIVE' ? 'danger' : 'success'" @click="toggle(row)">
            {{ row.status === 'ACTIVE' ? '停用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi } from '../api'

const list = ref([])
const roleMap = { USER: '普通用户', LAB_ADMIN: '实验室管理员', TECHNICIAN: '维修人员', SYS_ADMIN: '系统管理员' }
const roleText = (r) => roleMap[r] || r

async function load() {
  const res = await userApi.list()
  list.value = res.data
}
async function toggle(row) {
  await userApi.updateStatus(row.id, row.status === 'ACTIVE' ? 'DISABLED' : 'ACTIVE')
  ElMessage.success('状态已更新')
  load()
}
onMounted(load)
</script>
