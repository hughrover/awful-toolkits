<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

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

const statusMap: Record<number, string> = { 0: '草稿', 1: '进行中', 2: '已完成' }
const statusType: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'success' }

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

function viewDetail(row: Project) {
  router.push(`/projects/${row.id}`)
}

onMounted(loadProjects)
</script>

<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目列表</span>
          <el-button type="primary" @click="openCreate">
            <el-icon><Plus /></el-icon>新建项目
          </el-button>
        </div>
      </template>

      <el-table :data="projects" stripe>
        <el-table-column prop="name" label="项目名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status as number]">{{ statusMap[row.status as number] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row)">详情</el-button>
            <el-button size="small" type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑项目' : '新建项目'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="项目名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option :value="0" label="草稿" />
            <el-option :value="1" label="进行中" />
            <el-option :value="2" label="已完成" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
