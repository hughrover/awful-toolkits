<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false)

const menuItems = [
  { index: '/projects', title: '项目预算管理', icon: 'Money' },
  { index: '/personnel', title: '人员管理', icon: 'User' },
  { index: '/roles', title: '角色管理', icon: 'Setting' },
]

function handleSelect(index: string) {
  router.push(index)
}
</script>

<template>
  <el-container class="app-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="app-aside">
      <div class="logo-container">
        <el-icon v-if="isCollapse" class="logo-icon"><Tools /></el-icon>
        <span v-else class="logo-text">工作工具集</span>
      </div>
      <el-menu
        :default-active="route.path"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        router
        class="side-menu"
      >
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
      <div class="collapse-btn" @click="isCollapse = !isCollapse">
        <el-icon v-if="!isCollapse"><DArrowLeft /></el-icon>
        <el-icon v-else><DArrowRight /></el-icon>
      </div>
    </el-aside>
    <el-container class="main-container">
      <el-header class="app-header">
        <div class="header-left">
          <span class="header-title">工作工具集平台</span>
        </div>
      </el-header>
      <el-main class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.app-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.app-aside {
  background-color: #304156;
  transition: width 0.3s cubic-bezier(0.2, 0, 0, 1);
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  z-index: 1001;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b2f3a;
}

.logo-text {
  white-space: nowrap;
}

.logo-icon {
  font-size: 24px;
}

.side-menu {
  border-right: none;
  flex: 1;
}

.collapse-btn {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #bfcbd9;
  cursor: pointer;
  background-color: #2b2f3a;
  transition: background-color 0.3s;
}

.collapse-btn:hover {
  background-color: #263445;
}

.main-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  padding: 0 24px;
  height: 60px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.app-main {
  background: #f0f2f5;
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

/* transition */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
