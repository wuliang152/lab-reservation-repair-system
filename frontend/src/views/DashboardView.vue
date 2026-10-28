<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6" v-for="item in cards" :key="item.label">
        <el-card class="stat">
          <div class="label">{{ item.label }}</div>
          <div class="value">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12"><el-card><div ref="equipChart" style="height:320px"></div></el-card></el-col>
      <el-col :span="12"><el-card><div ref="repairChart" style="height:320px"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import { dashboardApi } from '../api'

const cards = reactive([
  { label: '设备总数', value: 0 },
  { label: '可用设备', value: 0 },
  { label: '待审批预约', value: 0 },
  { label: '待处理报修', value: 0 }
])
const equipChart = ref(null)
const repairChart = ref(null)

onMounted(async () => {
  const res = await dashboardApi.summary()
  const d = res.data
  cards[0].value = d.equipmentTotal
  cards[1].value = d.availableEquipment
  cards[2].value = d.pendingReservations
  cards[3].value = d.pendingRepairs

  const equip = echarts.init(equipChart.value)
  equip.setOption({
    title: { text: '设备状态分布' },
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: '60%', data: d.equipmentStatus }]
  })

  const repair = echarts.init(repairChart.value)
  repair.setOption({
    title: { text: '报修单状态分布' },
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: '60%', data: d.repairStatus }]
  })
})
</script>

<style scoped>
.stat .label { color: #666; font-size: 14px; }
.stat .value { font-size: 30px; font-weight: bold; margin-top: 8px; }
</style>
