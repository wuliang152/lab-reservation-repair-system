<template>
  <el-card>
    <el-table :data="list" border stripe>
      <el-table-column prop="title" label="标题" width="240" />
      <el-table-column prop="content" label="内容" />
      <el-table-column prop="createdAt" label="时间" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isRead ? 'info' : 'danger'">{{ row.isRead ? '已读' : '未读' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="!row.isRead" link type="primary" @click="read(row)">标记已读</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { notificationApi } from '../api'

const list = ref([])
async function load() {
  const res = await notificationApi.list()
  list.value = res.data
}
async function read(row) {
  await notificationApi.read(row.id)
  ElMessage.success('已标记为已读')
  load()
}
onMounted(load)
</script>
