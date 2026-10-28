<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="名称/编号模糊搜索" clearable style="width:220px" />
        <el-select v-model="query.labId" placeholder="实验室" clearable style="width:160px">
          <el-option v-for="l in labs" :key="l.id" :label="l.name" :value="l.id" />
        </el-select>
        <el-select v-model="query.status" placeholder="状态" clearable style="width:140px">
          <el-option label="可用" value="AVAILABLE" />
          <el-option label="维修中" value="REPAIRING" />
          <el-option label="报废" value="RETIRED" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button v-if="isAdmin" type="success" @click="openEdit()">新增设备</el-button>
      </div>
      <el-table :data="list" border stripe style="margin-top:12px">
        <el-table-column prop="code" label="编号" width="120" />
        <el-table-column prop="name" label="名称" width="180" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="labName" label="实验室" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }"><el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="model" label="型号" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openReserve(row)">预约</el-button>
            <template v-if="isAdmin">
              <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
              <el-button link type="danger" @click="remove(row)">删除</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editing.id ? '编辑设备' : '新增设备'" width="520px">
      <el-form :model="editing" label-width="90px">
        <el-form-item label="编号"><el-input v-model="editing.code" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="editing.name" /></el-form-item>
        <el-form-item label="型号"><el-input v-model="editing.model" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="editing.category" /></el-form-item>
        <el-form-item label="实验室">
          <el-select v-model="editing.labId" style="width:100%">
            <el-option v-for="l in labs" :key="l.id" :label="l.name" :value="l.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editing.status" style="width:100%">
            <el-option label="可用" value="AVAILABLE" />
            <el-option label="维修中" value="REPAIRING" />
            <el-option label="报废" value="RETIRED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="reserveVisible" title="预约设备" width="520px">
      <el-form :model="reserve" label-width="90px">
        <el-form-item label="设备">{{ reserve.equipmentName }}</el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="reserve.startTime" type="datetime" style="width:100%" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="reserve.endTime" type="datetime" style="width:100%" /></el-form-item>
        <el-form-item label="用途"><el-input v-model="reserve.purpose" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reserveVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReserve">提交预约</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { equipmentApi, labApi, reservationApi } from '../api'
import { useAuthStore } from '../store/auth'

const auth = useAuthStore()
const isAdmin = computed(() => ['LAB_ADMIN', 'SYS_ADMIN'].includes(auth.role))
const list = ref([])
const labs = ref([])
const query = reactive({ keyword: '', labId: null, status: '' })
const dialogVisible = ref(false)
const reserveVisible = ref(false)
const editing = reactive({ id: null, code: '', name: '', model: '', category: '', labId: null, status: 'AVAILABLE' })
const reserve = reactive({ equipmentId: null, equipmentName: '', startTime: null, endTime: null, purpose: '' })

const statusMap = { AVAILABLE: '可用', REPAIRING: '维修中', RETIRED: '报废' }
const statusText = (s) => statusMap[s] || s
const statusType = (s) => (s === 'AVAILABLE' ? 'success' : s === 'REPAIRING' ? 'warning' : 'info')

async function load() {
  const res = await equipmentApi.list(query)
  list.value = res.data
}

async function loadLabs() {
  const res = await labApi.list()
  labs.value = res.data
}

function openEdit(row) {
  Object.assign(editing, row ? { id: row.id, code: row.code, name: row.name, model: row.model, category: row.category, labId: row.labId, status: row.status } : { id: null, code: '', name: '', model: '', category: '', labId: null, status: 'AVAILABLE' })
  dialogVisible.value = true
}

async function save() {
  if (editing.id) await equipmentApi.update(editing.id, editing)
  else await equipmentApi.create(editing)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

async function remove(row) {
  await ElMessageBox.confirm(`确定删除设备 ${row.name} 吗？`, '提示', { type: 'warning' })
  await equipmentApi.remove(row.id)
  ElMessage.success('删除成功')
  load()
}

function openReserve(row) {
  reserve.equipmentId = row.id
  reserve.equipmentName = `${row.code} ${row.name}`
  reserve.startTime = null
  reserve.endTime = null
  reserve.purpose = ''
  reserveVisible.value = true
}

async function submitReserve() {
  if (!reserve.startTime || !reserve.endTime) return ElMessage.warning('请选择时间')
  await reservationApi.create({ ...reserve, startTime: reserve.startTime.toISOString(), endTime: reserve.endTime.toISOString() })
  ElMessage.success('预约已提交，等待审批')
  reserveVisible.value = false
}

onMounted(() => { load(); loadLabs() })
</script>

<style scoped>
.toolbar { display: flex; gap: 10px; align-items: center; flex-wrap: wrap; }
</style>
