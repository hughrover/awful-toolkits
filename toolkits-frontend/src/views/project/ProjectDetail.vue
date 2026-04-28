<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

interface Feature {
  id: number
  projectId: number
  parentId: number | null
  name: string
  description: string
  sortOrder: number
  children?: Feature[]
}

interface BudgetItem {
  id: number
  featureId: number
  name: string
  unit: string
  unitCost: number
  quantity: number
}

interface Project {
  id: number
  name: string
  description: string
}

interface TeamMember {
  id: number
  personnelId: number
  projectId: number
  roleInProject: string
}

interface Personnel {
  id: number
  name: string
}

const route = useRoute()
const router = useRouter()
const projectId = computed(() => Number(route.params.id))

const project = ref<Project | null>(null)
const features = ref<Feature[]>([])
const budgetItems = ref<Record<number, BudgetItem[]>>({})
const featureSubtotals = ref<Record<number, number>>({})
const totalBudget = ref(0)
const teamMembers = ref<TeamMember[]>([])
const allPersonnel = ref<Personnel[]>([])
const personnelMap = ref<Record<number, string>>({})
const teamDialogVisible = ref(false)
const teamForm = ref({ personnelId: undefined as number | undefined, roleInProject: '' })

const featureDialogVisible = ref(false)
const budgetDialogVisible = ref(false)
const featureForm = ref<Partial<Feature>>({})
const budgetForm = ref<Partial<BudgetItem>>({})
const currentFeatureId = ref<number | null>(null)
const isEditFeature = ref(false)
const isEditBudget = ref(false)

async function loadProject() {
  const res = await api.get(`/projects/${projectId.value}`)
  project.value = res.data
}

async function loadFeatures() {
  const res = await api.get(`/projects/${projectId.value}/features`)
  features.value = res.data
  for (const f of features.value) {
    await loadBudgetItems(f.id)
  }
}

async function loadBudgetItems(featureId: number) {
  const res = await api.get(`/features/${featureId}/budget-items`)
  budgetItems.value[featureId] = res.data
  const subtotalRes = await api.get(`/features/${featureId}/budget-items/subtotal`)
  featureSubtotals.value[featureId] = subtotalRes.data
  recalcTotal()
}

function recalcTotal() {
  totalBudget.value = Object.values(featureSubtotals.value).reduce((sum, v) => sum + v, 0)
}

function openAddFeature(parentId: number | null = null) {
  isEditFeature.value = false
  featureForm.value = { name: '', description: '', parentId, sortOrder: 0 }
  featureDialogVisible.value = true
}

function openEditFeature(feature: Feature) {
  isEditFeature.value = true
  featureForm.value = { ...feature }
  featureDialogVisible.value = true
}

async function handleSaveFeature() {
  if (isEditFeature.value) {
    await api.put(`/projects/${projectId.value}/features/${featureForm.value.id}`, featureForm.value)
    ElMessage.success('更新成功')
  } else {
    await api.post(`/projects/${projectId.value}/features`, featureForm.value)
    ElMessage.success('创建成功')
  }
  featureDialogVisible.value = false
  loadFeatures()
}

async function handleDeleteFeature(feature: Feature) {
  await ElMessageBox.confirm(`确定删除功能「${feature.name}」及其预算项？`, '确认删除')
  await api.delete(`/projects/${projectId.value}/features/${feature.id}`)
  ElMessage.success('删除成功')
  loadFeatures()
}

function openAddBudget(featureId: number) {
  isEditBudget.value = false
  currentFeatureId.value = featureId
  budgetForm.value = { name: '', unit: '', unitCost: 0, quantity: 1 }
  budgetDialogVisible.value = true
}

function openEditBudget(item: BudgetItem) {
  isEditBudget.value = true
  currentFeatureId.value = item.featureId
  budgetForm.value = { ...item }
  budgetDialogVisible.value = true
}

async function handleSaveBudget() {
  const fid = currentFeatureId.value!
  if (isEditBudget.value) {
    await api.put(`/features/${fid}/budget-items/${budgetForm.value.id}`, budgetForm.value)
    ElMessage.success('更新成功')
  } else {
    await api.post(`/features/${fid}/budget-items`, budgetForm.value)
    ElMessage.success('创建成功')
  }
  budgetDialogVisible.value = false
  loadBudgetItems(fid)
}

async function handleDeleteBudget(item: BudgetItem) {
  await ElMessageBox.confirm('确定删除该预算项？', '确认删除')
  await api.delete(`/features/${item.featureId}/budget-items/${item.id}`)
  ElMessage.success('删除成功')
  loadBudgetItems(item.featureId)
}

function formatMoney(val: number) {
  return val.toLocaleString('zh-CN', { style: 'currency', currency: 'CNY' })
}

async function loadTeam() {
  const res = await api.get(`/projects/${projectId.value}/team`)
  teamMembers.value = res.data
}

async function loadAllPersonnel() {
  const res = await api.get('/personnel')
  allPersonnel.value = res.data
  personnelMap.value = {}
  for (const p of res.data) {
    personnelMap.value[p.id] = p.name
  }
}

function openAddTeamMember() {
  teamForm.value = { personnelId: undefined, roleInProject: '' }
  teamDialogVisible.value = true
}

async function handleAddTeamMember() {
  await api.post(`/projects/${projectId.value}/team`, teamForm.value)
  ElMessage.success('已添加团队成员')
  teamDialogVisible.value = false
  loadTeam()
}

async function handleRemoveTeamMember(member: TeamMember) {
  await ElMessageBox.confirm('确定移除该团队成员？', '确认移除')
  await api.delete(`/personnel-project/${member.id}`)
  ElMessage.success('已移除')
  loadTeam()
}

onMounted(() => {
  loadProject()
  loadFeatures()
  loadTeam()
  loadAllPersonnel()
})
</script>

<template>
  <div>
    <el-page-header @back="router.push('/projects')">
      <template #content>
        <span v-if="project">{{ project.name }}</span>
      </template>
    </el-page-header>

    <el-card style="margin-top: 16px">
      <template #header>
        <div class="card-header">
          <span>项目团队</span>
          <el-button type="primary" size="small" @click="openAddTeamMember">
            <el-icon><Plus /></el-icon>添加成员
          </el-button>
        </div>
      </template>
      <el-table :data="teamMembers" stripe>
        <el-table-column label="姓名" width="150">
          <template #default="{ row }">{{ personnelMap[row.personnelId] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="roleInProject" label="项目角色" min-width="150" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="handleRemoveTeamMember(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="teamMembers.length === 0" description="暂无团队成员" />
    </el-card>

    <el-card style="margin-top: 16px">
      <template #header>
        <div class="card-header">
          <span>功能拆解 (WBS)</span>
          <el-button type="primary" size="small" @click="openAddFeature(null)">
            <el-icon><Plus /></el-icon>添加顶级功能
          </el-button>
        </div>
      </template>

      <div v-for="feature in features" :key="feature.id" class="feature-block">
        <div class="feature-header">
          <div>
            <el-icon><Folder /></el-icon>
            <strong>{{ feature.name }}</strong>
            <span v-if="feature.description" class="feature-desc"> - {{ feature.description }}</span>
          </div>
          <div>
            <el-button size="small" @click="openAddBudget(feature.id)">添加预算项</el-button>
            <el-button size="small" @click="openAddFeature(feature.id)">添加子功能</el-button>
            <el-button size="small" type="primary" @click="openEditFeature(feature)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteFeature(feature)">删除</el-button>
          </div>
        </div>

        <el-table :data="budgetItems[feature.id] || []" size="small" stripe style="margin: 8px 0 0 24px">
          <el-table-column prop="name" label="预算项" min-width="120" />
          <el-table-column prop="unit" label="单位" width="80" />
          <el-table-column prop="unitCost" label="单价" width="100">
            <template #default="{ row }">{{ formatMoney(row.unitCost) }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column label="小计" width="120">
            <template #default="{ row }">{{ formatMoney(row.unitCost * row.quantity) }}</template>
          </el-table-column>
          <el-table-column label="操作" width="140">
            <template #default="{ row }">
              <el-button size="small" @click="openEditBudget(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDeleteBudget(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="subtotal" style="margin-left: 24px">
          小计: <strong>{{ formatMoney(featureSubtotals[feature.id] || 0) }}</strong>
        </div>
      </div>

      <el-divider />
      <div class="total-budget">
        项目总预算: <strong>{{ formatMoney(totalBudget) }}</strong>
      </div>
    </el-card>

    <!-- Team Member Dialog -->
    <el-dialog v-model="teamDialogVisible" title="添加团队成员" width="400px">
      <el-form :model="teamForm" label-width="80px">
        <el-form-item label="人员">
          <el-select v-model="teamForm.personnelId" placeholder="选择人员" filterable>
            <el-option v-for="p in allPersonnel" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目角色">
          <el-input v-model="teamForm.roleInProject" placeholder="如：前端开发、项目经理" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="teamDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddTeamMember">添加</el-button>
      </template>
    </el-dialog>

    <!-- Feature Dialog -->
    <el-dialog v-model="featureDialogVisible" :title="isEditFeature ? '编辑功能' : '添加功能'" width="500px">
      <el-form :model="featureForm" label-width="80px">
        <el-form-item label="功能名称">
          <el-input v-model="featureForm.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="featureForm.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="featureForm.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="featureDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveFeature">保存</el-button>
      </template>
    </el-dialog>

    <!-- Budget Dialog -->
    <el-dialog v-model="budgetDialogVisible" :title="isEditBudget ? '编辑预算项' : '添加预算项'" width="500px">
      <el-form :model="budgetForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="budgetForm.name" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="budgetForm.unit" placeholder="人天/个/项" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="budgetForm.unitCost" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="budgetForm.quantity" :min="0" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="budgetDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveBudget">保存</el-button>
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

.feature-block {
  margin-bottom: 16px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 12px;
}

.feature-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.feature-desc {
  color: #909399;
  font-size: 13px;
}

.subtotal {
  margin-top: 8px;
  text-align: right;
  color: #606266;
  font-size: 14px;
}

.total-budget {
  text-align: right;
  font-size: 18px;
  color: #303133;
}
</style>
