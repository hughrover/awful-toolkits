<template>
  <div class="sidebar">
    <div class="new-chat">
      <button @click="$emit('new-chat')">+ 新建对话</button>
    </div>
    <div class="session-list">
      <div 
        v-for="session in sessions" 
        :key="session.id" 
        :class="['session-item', { active: currentSessionId === session.id }]"
        @click="$emit('select-session', session)"
      >
        <span class="session-title">{{ session.title || '新对话' }}</span>
        <button class="delete-btn" @click.stop="$emit('delete-session', session.id)">×</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  sessions: any[];
  currentSessionId: number | null;
}>();

defineEmits(['new-chat', 'select-session', 'delete-session']);
</script>

<style scoped>
.sidebar {
  width: 260px;
  border-right: 1px solid #e5e5e5;
  display: flex;
  flex-direction: column;
  background: #f9f9f9;
  height: 100%;
}

.new-chat {
  padding: 20px;
}

.new-chat button {
  width: 100%;
  padding: 10px;
  background: #fff;
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
}

.new-chat button:hover {
  background: #f0f0f0;
}

.session-list {
  flex: 1;
  overflow-y: auto;
  padding: 0 10px;
}

.session-item {
  padding: 12px 15px;
  margin-bottom: 5px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.session-title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.session-item:hover {
  background: #ececec;
}

.session-item.active {
  background: #e8f3ff;
  color: #1a73e8;
}

.delete-btn {
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  font-size: 18px;
  padding: 0 5px;
  display: none;
}

.session-item:hover .delete-btn {
  display: block;
}

.delete-btn:hover {
  color: #ff4d4f;
}
</style>
