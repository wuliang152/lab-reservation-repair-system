<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="dialogVisible = true">提交报修</el-button>
        <el-button @click="load">刷新</el-button>
      </div>
      <el-table :data="list" border stripe style="margin-top:12px">
        <el-table-column prop="equipmentName" label="设备" width="200" />
        <el-table-column prop="userName" label="报修人" width="120" />
        <el-table-column prop="urgency" label="紧急程度" width="100">
          <template #default="{ row }"><el-tag :type="urgencyType(row.urgency)">{{ urgencyText(row.urgency) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="description" label="故障描述" />
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }"><el-tag :type="tag(row.status)">{{ text(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <template v-if="row.status === 'PENDING' && isAdmin">
              <el-select v-model="assignTech[row.id]" placeholder="选择维修人员" size="small" style="width:120px">
                <el-option v-for="t in technicians" :key="t.id" :label="t.name" :value="t.id" />
              </el-select>
              <el-button link type="primary" @click="assign(row)">派单</el-button>
            </template>
            <el-button v-if="row.status === 'ASSIGNED' && auth.role === 'TECHNICIAN'" link type="primary" @click="action(row, 'accept')">接单</el-button>
            <el-button v-if="row.status === 'REPAIRING' && auth.role === 'TECHNICIAN'" link type="success" @click="complete(row)">完成</el-button>
            <el-button v-if="row.status === 'COMPLETED' && row.userId === auth.user?.id" link type="primary" @click="action(row, 'confirm')">确认</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="提交报修" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="设备">
          <el-select v-model="form.equipmentId" style="width:100%">
            <el-option v-for="e in equipments" :key="e.id" :label="`${e.code} ${e.name}`" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="紧急程度">
          <el-select v-model="form.urgency" style="width:100%">
            <el-option label="普通" value="NORMAL" />
            <el-option label="紧急" value="URGENT" />
            <el-option label="特急" value="EMERGENCY" />
          </el-select>
        </el-form-item>
        <el-form-item label="故障描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { equipmentApi, repairApi, userApi } from '../api'
import { useAuthStore } from '../store/auth'

const auth = useAuthStore()
const isAdmin = computed(() => ['LAB_ADMIN', 'SYS_ADMIN'].includes(auth.role))
const list = ref([])
const equipments = ref([])
const technicians = ref([])
const dialogVisible = ref(false)
const assignTech = reactive({})
const form = reactive({ equipmentId: null, urgency: 'NORMAL', description: '' })

const statusMap = { PENDING: '待派单', ASSIGNED: '已派单', REPAIRING: '维修中', COMPLETED: '已完成', CONFIRMED: '已确认' }
const text = (s) => statusMap[s] || s
const tag = (s) => (s === 'COMPLETED' || s === 'CONFIRMED' ? 'success' : s === 'REPAIRING' ? 'warning' : s === 'PENDING' ? 'danger' : 'info')
const urgencyText = (s) => ({ NORMAL: '普通', URGENT: '紧急', EMERGENCY: '特急' }[s] || s)
const urgencyType = (s) => ({ NORMAL: 'info', URGENT: 'warning', EMERGENCY: 'danger' }[s] || 'info')

async function load() {
  const res = await repairApi.list()
  list.value = res.data
}

async function loadOptions() {
  const [eq, users] = await Promise.all([equipmentApi.list(), userApi.list()])
  equipments.value = eq.data
  technicians.value = users.data.filter((u) => u.role === 'TECHNICIAN' && u.status === 'ACTIVE')
}

async function submit() {
  if (!form.equipmentId) return ElMessage.warning('请选择设备')
  await repairApi.create(form)
  ElMessage.success('报修已提交')
  dialogVisible.value = false
  load()
}

async function assign(row) {
  if (!assignTech[row.id]) return ElMessage.warning('请选择维修人员')
  await repairApi.assign(row.id, assignTech[row.id])
  ElMessage.success('派单成功')
  load()
}

async function action(row, type) {
  const fn = { accept: repairApi.accept, start: repairApi.start, confirm: repairApi.confirm }[type]
  await fn(row.id)
  ElMessage.success('操作成功')
  load()
}

async function complete(row) {
  const { value } = await ElMessageBox.prompt('请输入维修结果', '完成维修', { type: 'success' })
  await repairApi.complete(row.id, value)
  ElMessage.success('已完成维修')
  load()
}

onMounted(() => { load(); loadOptions() })
</script>

<style scoped>
.toolbar { display: flex; gap: 10px; }
</style>
