<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

interface Personnel {
  id: number
  name: string
  email: string
  phone: string
  hireDate: string
  roleId: number
  status: number
}

interface Role {
  id: number
  name: string
}

interface SalaryRecord {
  id: number
  personnelId: number
  amount: number
  effectiveDate: string
}

const personnelList = ref<Personnel[]>([])
const roles = ref<Role[]>([])
const dialogVisible = ref(false)
const salaryDialogVisible = ref(false)
const isEdit = ref(false)
const form = ref<Partial<Personnel>>({})
const salaryForm = ref({ amount: 0, effectiveDate: '' })
const currentPersonnelId = ref<number | null>(null)
const salaryHistory = ref<SalaryRecord[]>([])
const salaryHistoryDialogVisible = ref(false)
const currentPersonnelName = ref('')

const roleMap = ref<Record<number, string>>({})

async function loadRoles() {
  const res = await api.get('/roles')
  roles.value = res.data
  roleMap.value = {}
  for (const r of res.data) {
    roleMap.value[r.id] = r.name
  }
}

async function loadPersonnel() {
  const res = await api.get('/personnel')
  personnelList.value = res.data
}

function openCreate() {
  isEdit.value = false
  form.value = { name: '', email: '', phone: '', hireDate: '', roleId: undefined, status: 1 }
  dialogVisible.value = true
}

function openEdit(p: Personnel) {
  isEdit.value = true
  form.value = { ...p }
  dialogVisible.value = true
}

async function handleSave() {
  if (isEdit.value) {
    await api.put(`/personnel/${form.value.id}`, form.value)
    ElMessage.success('更新成功')
  } else {
    await api.post('/personnel', form.value)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadPersonnel()
}

async function handleDelete(p: Personnel) {
  await ElMessageBox.confirm(`确定删除人员「${p.name}」？`, '确认删除')
  await api.delete(`/personnel/${p.id}`)
  ElMessage.success('删除成功')
  loadPersonnel()
}

function openSalaryDialog(personnelId: number) {
  currentPersonnelId.value = personnelId
  salaryForm.value = { amount: 0, effectiveDate: '' }
  salaryDialogVisible.value = true
}

async function handleSaveSalary() {
  await api.post(`/personnel/${currentPersonnelId.value}/salaries`, salaryForm.value)
  ElMessage.success('薪资记录已添加')
  salaryDialogVisible.value = false
}

async function viewSalaryHistory(p: Personnel) {
  currentPersonnelName.value = p.name
  const res = await api.get(`/personnel/${p.id}/salaries`)
  salaryHistory.value = res.data
  salaryHistoryDialogVisible.value = true
}

function formatMoney(val: number) {
  return val?.toLocaleString('zh-CN', { style: 'currency', currency: 'CNY' })
}

onMounted(() => {
  loadRoles()
  loadPersonnel()
})
</script>

<template>
  <div class="personnel-container">
    <el-card shadow="hover" class="content-card">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon"><User /></el-icon>
            <span class="header-title">人员列表</span>
          </div>
          <el-button type="primary" @click="openCreate">
            <el-icon><Plus /></el-icon>新增人员
          </el-button>
        </div>
      </template>

      <el-table :data="personnelList" stripe hover style="width: 100%">
        <el-table-column prop="name" label="姓名" width="120" fixed="left" />
        <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
        <el-table-column prop="phone" label="电话" width="140" />
        <el-table-column prop="hireDate" label="入职日期" width="120" />
        <el-table-column label="角色" width="140">
          <template #default="{ row }">
            <el-tag size="small" effect="plain">{{ roleMap[row.roleId] || '未分配' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="{ row }">
            <el-button size="small" link type="primary" @click="viewSalaryHistory(row)">薪资历史</el-button>
            <el-button size="small" link type="primary" @click="openSalaryDialog(row.id)">调整薪资</el-button>
            <el-button size="small" link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Personnel Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑人员' : '新增人员'"
      width="550px"
      destroy-on-close
      class="custom-dialog"
    >
      <el-form :model="form" label-width="100px" label-position="right">
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker
            v-model="form.hireDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.roleId" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="r in roles" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option :value="1" label="在职" />
            <el-option :value="0" label="离职" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">确认保存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Salary Dialog -->
    <el-dialog
      v-model="salaryDialogVisible"
      title="调整薪资"
      width="450px"
      destroy-on-close
    >
      <el-form :model="salaryForm" label-width="100px">
        <el-form-item label="月薪金额" required>
          <el-input-number
            v-model="salaryForm.amount"
            :min="0"
            :precision="2"
            :step="500"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="生效日期" required>
          <el-date-picker
            v-model="salaryForm.effectiveDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择生效日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="salaryDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveSalary">提交变更</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Salary History Dialog -->
    <el-dialog
      v-model="salaryHistoryDialogVisible"
      :title="`${currentPersonnelName} - 薪资历史`"
      width="600px"
    >
      <el-table :data="salaryHistory" stripe border>
        <el-table-column label="薪资金额" width="200">
          <template #default="{ row }">
            <span class="money-text">{{ formatMoney(row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="effectiveDate" label="生效日期" />
      </el-table>
      <el-empty v-if="salaryHistory.length === 0" description="暂无历史薪资记录" />
    </el-dialog>
  </div>
</template>

<style scoped>
.personnel-container {
  padding: 0;
}

.content-card {
  border-radius: 8px;
  border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-icon {
  font-size: 20px;
  margin-right: 8px;
  color: #409eff;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.money-text {
  font-weight: bold;
  color: #f56c6c;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-card__header) {
  padding: 15px 20px;
  border-bottom: 1px solid #f0f2f5;
}

:deep(.el-table .cell) {
  white-space: nowrap;
}
</style>
