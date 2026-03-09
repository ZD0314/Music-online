<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon user">
              <el-icon :size="40"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon artist">
              <el-icon :size="40"><Microphone /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.artistCount }}</div>
              <div class="stat-label">歌手总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon song">
              <el-icon :size="40"><Headset /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.songCount }}</div>
              <div class="stat-label">歌曲总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon playlist">
              <el-icon :size="40"><List /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.playlistCount }}</div>
              <div class="stat-label">歌单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon active">
              <el-icon :size="40"><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.activeUserCount }}</div>
              <div class="stat-label">活跃用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon play">
              <el-icon :size="40"><VideoPlay /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.todayPlayCount }}</div>
              <div class="stat-label">今日播放次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>数据分布</span>
          </template>
          <div ref="pieChartRef" style="width: 100%; height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>近7天播放趋势</span>
          </template>
          <div ref="lineChartRef" style="width: 100%; height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>歌手歌曲数量排行 TOP 10</span>
          </template>
          <div ref="barChartRef" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { User, Microphone, Headset, List, TrendCharts, VideoPlay } from '@element-plus/icons-vue'
import { getStatistics } from '@/api'
import type { Statistics } from '@/types'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'

const statistics = ref<Statistics>({
  userCount: 0,
  artistCount: 0,
  songCount: 0,
  playlistCount: 0,
  activeUserCount: 0,
  todayPlayCount: 0
})

const pieChartRef = ref<HTMLDivElement>()
const lineChartRef = ref<HTMLDivElement>()
const barChartRef = ref<HTMLDivElement>()

let pieChart: ECharts | null = null
let lineChart: ECharts | null = null
let barChart: ECharts | null = null

const loadStatistics = async () => {
  try {
    const data = await getStatistics()
    statistics.value = data

    // 加载图表数据后初始化图表
    initCharts()
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const initCharts = () => {
  initPieChart()
  initLineChart()
  initBarChart()
}

// 饼图 - 数据分布
const initPieChart = () => {
  if (!pieChartRef.value) return

  pieChart = echarts.init(pieChartRef.value)

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '数据分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: statistics.value.userCount, name: '用户', itemStyle: { color: '#667eea' } },
          { value: statistics.value.artistCount, name: '歌手', itemStyle: { color: '#f5576c' } },
          { value: statistics.value.songCount, name: '歌曲', itemStyle: { color: '#4facfe' } },
          { value: statistics.value.playlistCount, name: '歌单', itemStyle: { color: '#43e97b' } }
        ]
      }
    ]
  }

  pieChart.setOption(option)
}

// 折线图 - 近7天播放趋势
const initLineChart = () => {
  if (!lineChartRef.value) return

  lineChart = echarts.init(lineChartRef.value)

  // 模拟近7天数据
  const dates = []
  const playData = []
  const today = new Date()

  for (let i = 6; i >= 0; i--) {
    const date = new Date(today)
    date.setDate(date.getDate() - i)
    dates.push(`${date.getMonth() + 1}/${date.getDate()}`)
    // 模拟数据,实际应该从后端获取
    playData.push(Math.floor(Math.random() * 1000) + 500)
  }

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '播放次数',
        type: 'line',
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ])
        },
        itemStyle: {
          color: '#409eff'
        },
        data: playData
      }
    ]
  }

  lineChart.setOption(option)
}

// 柱状图 - 歌手歌曲数量排行
const initBarChart = () => {
  if (!barChartRef.value) return

  barChart = echarts.init(barChartRef.value)

  // 模拟数据,实际应该从后端获取
  const artistNames = ['周杰伦', '林俊杰', '邓紫棋', '薛之谦', '陈奕迅', '张学友', '王力宏', '李荣浩', '毛不易', '许嵩']
  const songCounts = [156, 142, 128, 115, 98, 87, 76, 65, 54, 43]

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: artistNames
    },
    series: [
      {
        name: '歌曲数量',
        type: 'bar',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        },
        data: songCounts
      }
    ]
  }

  barChart.setOption(option)
}

// 窗口大小改变时重新渲染图表
const handleResize = () => {
  pieChart?.resize()
  lineChart?.resize()
  barChart?.resize()
}

onMounted(() => {
  loadStatistics()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  pieChart?.dispose()
  lineChart?.dispose()
  barChart?.dispose()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 80px;
  height: 80px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-icon.user {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.artist {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.song {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.playlist {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon.active {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-icon.play {
  background: linear-gradient(135deg, #30cfd0 0%, #330867 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
