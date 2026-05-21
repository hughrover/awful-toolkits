<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Filter } from '@element-plus/icons-vue'
import api from '@/api'
import ProjectCard from './ProjectCard.vue'

interface Project {
  id: number
  name: string
  description: string
  startDate: string
  endDate: string
  status: number
}

const router = useRouter()
const projects = ref<Project[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref<Partial<Project>>({})
const searchQuery = ref('')
const statusFilter = ref<number | null>(null)

const filteredProjects = computed(() => {
  return projects.value.filter(p => {
    const matchesSearch = p.name.toLowerCase().includes(searchQuery.value.toLowerCase()) || 
                         (p.description && p.description.toLowerCase().includes(searchQuery.value.toLowerCase()))
    const matchesStatus = statusFilter.value === null || p.status === statusFilter.value
    return matchesSearch && matchesStatus
  })
})

async function loadProjects() {
  const res = await api.get('/projects')
  projects.value = res.data
}

function openCreate() {
  isEdit.value = false
  form.value = { name: '', description: '', startDate: '', endDate: '', status: 0 }
  dialogVisible.value = true
}

function openEdit(project: Project) {
  isEdit.value = true
  form.value = { ...project }
  dialogVisible.value = true
}

async function handleSave() {
  if (isEdit.value) {
    await api.put(`/projects/${form.value.id}`, form.value)
    ElMessage.success('更新成功')
  } else {
    await api.post('/projects', form.value)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadProjects()
}

async function handleDelete(project: Project) {
  await ElMessageBox.confirm(`确定删除项目「${project.name}」？`, '确认删除')
  await api.delete(`/projects/${project.id}`)
  ElMessage.success('删除成功')
  loadProjects()
}

function viewDetail(project: Project) {
  router.push(`/projects/${project.id}`)
}

onMounted(loadProjects)
</script>

<template>
  <div class="project-list-container">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">项目工作台</h2>
        <span class="page-subtitle">管理并追踪您的所有项目进度</span>
      </div>
      <div class="header-actions">
        <el-input
          v-model="searchQuery"
          placeholder="搜索项目名称或描述..."
          class="search-input"
          :prefix-icon="Search"
          clearable
        />
        <el-select v-model="statusFilter" placeholder="全部状态" clearable class="status-select">
          <template #prefix><el-icon><Filter /></el-icon></template>
          <el-option :value="0" label="草稿" />
          <el-option :value="1" label="进行中" />
          <el-option :value="2" label="已完成" />
        </el-select>
        <el-button type="primary" @click="openCreate" :icon="Plus">新建项目</el-button>
      </div>
    </div>

    <div v-if="filteredProjects.length > 0" class="project-grid">
      <el-row :gutter="20">
        <el-col 
          v-for="project in filteredProjects" 
          :key="project.id" 
          :xs="24" :sm="12" :md="8" :lg="6" :xl="4"
          class="grid-col"
        >
          <ProjectCard 
            :project="project" 
            @edit="openEdit" 
            @delete="handleDelete" 
            @view="viewDetail"
          />
        </el-col>
      </el-row>
    </div>

    <el-empty v-else description="没有找到匹配的项目" :image-size="200">
      <el-button v-if="searchQuery || statusFilter !== null" @click="searchQuery = ''; statusFilter = null">清除筛选</el-button>
      <el-button v-else type="primary" @click="openCreate">立即创建项目</el-button>
    </el-empty>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑项目' : '新建项目'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="项目名称">
          <el-input v-model="form.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="简要说明项目目标和背景" />
        </el-form-item>
        <el-form-item label="起止日期">
          <div class="date-picker-group">
            <el-date-picker
              v-model="form.startDate"
              type="date"
              placeholder="开始日期"
              value-format="YYYY-MM-DD"
              style="width: 48%"
            />
            <span class="date-separator">-</span>
            <el-date-picker
              v-model="form.endDate"
              type="date"
              placeholder="结束日期"
              value-format="YYYY-MM-DD"
              style="width: 48%"
            />
          </div>
        </el-form-item>
        <el-form-item label="项目状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">草稿</el-radio>
            <el-radio :label="1">进行中</el-radio>
            <el-radio :label="2">已完成</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.project-list-container {
  padding: 0;
  max-width: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #909399;
}

.header-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.search-input {
  width: 280px;
}

.status-select {
  width: 150px;
}

.grid-col {
  margin-bottom: 20px;
}

.date-picker-group {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.date-separator {
  display: inline-block;
  width: 4%;
  text-align: center;
  color: #909399;
}

:deep(.el-dialog__body) {
  padding-top: 10px;
}

/* 列表过渡动画 */
.project-grid {
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
