<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

interface Role {
  id: number
  name: string
  description: string
}

const roles = ref<Role[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref<Partial<Role>>({})

async function loadRoles() {
  const res = await api.get('/roles')
  roles.value = res.data
}

function openCreate() {
  isEdit.value = false
  form.value = { name: '', description: '' }
  dialogVisible.value = true
}

function openEdit(role: Role) {
  isEdit.value = true
  form.value = { ...role }
  dialogVisible.value = true
}

async function handleSave() {
  if (isEdit.value) {
    await api.put(`/roles/${form.value.id}`, form.value)
    ElMessage.success('更新成功')
  } else {
    await api.post('/roles', form.value)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadRoles()
}

async function handleDelete(role: Role) {
  await ElMessageBox.confirm(`确定删除角色「${role.name}」？`, '确认删除')
  try {
    await api.delete(`/roles/${role.id}`)
    ElMessage.success('删除成功')
    loadRoles()
  } catch (e: any) {
    ElMessage.error(e.message || '删除失败')
  }
}

onMounted(loadRoles)
</script>

<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>开发角色管理</span>
          <el-button type="primary" @click="openCreate">
            <el-icon><Plus /></el-icon>新建角色
          </el-button>
        </div>
      </template>

      <el-table :data="roles" stripe>
        <el-table-column prop="name" label="角色名称" width="200" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑角色' : '新建角色'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
