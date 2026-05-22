<template>
  <div class="floating-ball" :class="{ 'active': store.isSidebarVisible }" @click="store.toggleSidebar">
    <el-icon v-if="!store.isSidebarVisible"><ChatDotRound /></el-icon>
    <el-icon v-else><Close /></el-icon>
  </div>
</template>

<script setup lang="ts">
import { useChatStore } from '@/stores/chat';
import { ChatDotRound, Close } from '@element-plus/icons-vue';

const store = useChatStore();
</script>

<style scoped>
.floating-ball {
  position: fixed;
  right: 30px;
  bottom: 30px;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 3000;
}

.floating-ball:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.floating-ball.active {
  background-color: #909399;
  transform: rotate(90deg);
}

.floating-ball.active:hover {
  transform: rotate(90deg) scale(1.1);
}
</style>
