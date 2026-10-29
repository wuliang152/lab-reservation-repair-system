<template>
  <el-card>
    <div class="toolbar">
      <el-radio-group v-model="filter">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="mine">我的预约</el-radio-button>
      </el-radio-group>
      <el-button @click="load">刷新</el-button>
    </div>
    <el-table :data="list" border stripe style="margin-top:12px">
      <el-table-column prop="equipmentCode" label="设备编号" width="120" />
      <el-table-column prop="equipmentName" label="设备名称" width="180" />
      <el-table-column prop="userName" label="申请人" width="120" />
      <el-table-column prop="startTime" label="开始时间" width="170" />
      <el-table-column prop="endTime" label="结束时间" width="170" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }"><el-tag :type="tag(row.status)">{{ text(row.status) }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="purpose" label="用途" />
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <template v-if="row.status === 'PENDING' && isAdmin">
            <el-button link type="success" @click="approve(row)">通过</el-button>
            <el-button link type="danger" @click="reject(row)">驳回</el-button>
          </template>
          <template v-if="['PENDING','APPROVED'].includes(row.status) && row.userId === auth.user?.id">
            <el-button link type="danger" @click="cancel(row)">取消</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reservationApi } from '../api'
import { useAuthStore } from '../store/auth'

const auth = useAuthStore()
const isAdmin = computed(() => ['LAB_ADMIN', 'SYS_ADMIN'].includes(auth.role))
const list = ref([])
const filter = ref('all')
const statusMap = { PENDING: '待审批', APPROVED: '已通过', REJECTED: '已驳回', CANCELLED: '已取消', COMPLETED: '已完成' }
const tag = (s) => (s === 'APPROVED' ? 'success' : s === 'REJECTED' || s === 'CANCELLED' ? 'danger' : s === 'PENDING' ? 'warning' : 'info')
const text = (s) => statusMap[s] || s

async function load() {
  const res = await reservationApi.list()
  list.value = filter.value === 'mine' ? res.data.filter((r) => r.userId === auth.user?.id) : res.data
}

async function approve(row) {
  await reservationApi.approve(row.id)
  ElMessage.success('已通过')
  load()
}

async function reject(row) {
  const { value } = await ElMessageBox.prompt('请输入驳回理由', '驳回预约', { type: 'warning' })
  await reservationApi.reject(row.id, value)
  ElMessage.success('已驳回')
  load()
}

async function cancel(row) {
  await ElMessageBox.confirm('确定取消该预约吗？', '提示', { type: 'warning' })
  await reservationApi.cancel(row.id)
  ElMessage.success('已取消')
  load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; gap: 10px; align-items: center; }
</style>
