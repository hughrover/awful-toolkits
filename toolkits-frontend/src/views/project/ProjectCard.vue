<script setup lang="ts">
import { computed } from 'vue'
import { MoreFilled, Timer, Calendar, User } from '@element-plus/icons-vue'

interface Project {
  id: number
  name: string
  description: string
  startDate: string
  endDate: string
  status: number
  personnelCount?: number
}

const props = defineProps<{
  project: Project
}>()

const emit = defineEmits(['edit', 'delete', 'view'])

const statusMap: Record<number, string> = { 0: '草稿', 1: '进行中', 2: '已完成' }
const statusType: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'success' }

const progress = computed(() => {
  const start = new Date(props.project.startDate).getTime()
  const end = new Date(props.project.endDate).getTime()
  const now = new Date().getTime()

  if (isNaN(start) || isNaN(end)) return 0
  if (now < start) return 0
  if (now > end) return 100

  const total = end - start
  const elapsed = now - start
  return Math.round((elapsed / total) * 100)
})

const remainingDays = computed(() => {
  const end = new Date(props.project.endDate).getTime()
  const now = new Date().getTime()
  
  if (now > end) return 0
  
  const diff = end - now
  return Math.ceil(diff / (1000 * 60 * 60 * 24))
})

const progressStatus = computed(() => {
  if (progress.value >= 100) return 'success'
  if (remainingDays.value <= 3 && props.project.status === 1) return 'exception'
  return ''
})
</script>

<template>
  <el-card class="project-card" shadow="hover" :body-style="{ padding: '20px' }">
    <div class="card-header">
      <div class="title-section">
        <h3 class="project-name" @click="emit('view', project)">{{ project.name }}</h3>
        <div class="status-wrapper">
          <span v-if="project.status === 1" class="pulse-dot"></span>
          <el-tag :type="statusType[project.status]" size="small" effect="plain" class="status-tag">
            {{ statusMap[project.status] }}
          </el-tag>
        </div>
      </div>
      <el-dropdown trigger="click">
        <el-button link :icon="MoreFilled" class="more-btn"></el-button>
        <template #footer>
          <el-dropdown-menu>
            <el-dropdown-item @click="emit('view', project)">查看详情</el-dropdown-item>
            <el-dropdown-item @click="emit('edit', project)">编辑项目</el-dropdown-item>
            <el-dropdown-item @click="emit('delete', project)" divided style="color: #f56c6c">删除项目</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <p class="project-desc">{{ project.description || '暂无项目描述' }}</p>

    <div class="progress-section">
      <div class="progress-info">
        <span class="label">项目进度</span>
        <span class="percentage">{{ progress }}%</span>
      </div>
      <el-progress 
        :percentage="progress" 
        :status="progressStatus" 
        :stroke-width="6" 
        :show-text="false"
      />
      <div class="time-info">
        <span v-if="progress < 100 && project.status === 1" class="days-left">
          <el-icon><Timer /></el-icon> 剩余 {{ remainingDays }} 天
        </span>
        <span v-else-if="progress >= 100" class="completed-text">已达到结束日期</span>
        <span v-else class="date-range">
          <el-icon><Calendar /></el-icon> {{ project.startDate }} 至 {{ project.endDate }}
        </span>
      </div>
    </div>

    <div class="card-footer">
      <div class="personnel-info">
        <el-icon><User /></el-icon>
        <span class="count">{{ project.personnelCount || 0 }}</span>
        <span class="label">人参与</span>
      </div>
      <el-button type="primary" link size="small" @click="emit('view', project)">进入工作区</el-button>
    </div>
  </el-card>
</template>

<style scoped>
.project-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  border: 1px solid #f0f0f0;
}

.project-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08) !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.title-section {
  flex: 1;
}

.project-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  cursor: pointer;
  transition: color 0.2s;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.project-name:hover {
  color: #409eff;
}

.status-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-tag {
  border-radius: 4px;
}

.pulse-dot {
  width: 8px;
  height: 8px;
  background-color: #67c23a;
  border-radius: 50%;
  position: relative;
}

.pulse-dot::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: inherit;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.8;
  }
  70% {
    transform: scale(2.5);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

.more-btn {
  color: #909399;
  padding: 4px;
}

.project-desc {
  font-size: 13px;
  color: #606266;
  margin: 0 0 20px 0;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 42px;
}

.progress-section {
  margin-bottom: 20px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 12px;
}

.progress-info .label {
  color: #909399;
}

.progress-info .percentage {
  font-weight: 600;
  color: #303133;
}

.time-info {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
}

.days-left {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #e6a23c;
  font-weight: 500;
}

.completed-text {
  color: #67c23a;
}

.date-range {
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-footer {
  margin-top: auto;
  padding-top: 15px;
  border-top: 1px solid #f5f7fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.personnel-info {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  font-size: 13px;
}

.personnel-info .el-icon {
  font-size: 16px;
  color: #909399;
}

.personnel-info .count {
  font-weight: 600;
  color: #303133;
}

.personnel-info .label {
  color: #909399;
}
</style>
