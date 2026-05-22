<template>
  <transition name="slide-fade">
    <div v-if="store.isSidebarVisible" class="sidebar-assistant">
      <div class="sidebar-header">
        <div class="header-info">
          <el-icon><ChatLineRound /></el-icon>
          <span class="title">AI 助手</span>
        </div>
        <el-button link @click="store.toggleSidebar">
          <el-icon><Close /></el-icon>
        </el-button>
      </div>
      
      <div class="sidebar-content">
        <ChatSidebar 
          v-if="showSessions"
          :sessions="store.sessions" 
          :currentSessionId="store.currentSessionId"
          @new-chat="store.createNewSession"
          @select-session="store.selectSession"
          @delete-session="store.deleteSession"
        />
        
        <div class="chat-main">
          <div class="messages" ref="messageContainer">
            <MessageBubble v-for="(msg, index) in store.messages" :key="index" :role="msg.role" :content="msg.content" />
            <MessageBubble v-if="store.isStreaming" role="assistant" :isStreaming="true">
              {{ store.streamingContent }}
            </MessageBubble>
          </div>
          
          <div class="input-container">
            <div class="input-box">
              <textarea 
                v-model="userInput" 
                placeholder="输入您的问题..." 
                @keydown.enter.prevent="handleSendMessage"
                rows="2"
              ></textarea>
              <button class="send-btn" :disabled="!userInput.trim() || store.isStreaming" @click="handleSendMessage">
                <el-icon><Promotion /></el-icon>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, provide } from 'vue';
import ChatSidebar from './ChatSidebar.vue';
import MessageBubble from './MessageBubble.vue';
import { useChatStore } from '@/stores/chat';
import { ChatLineRound, Close, Promotion } from '@element-plus/icons-vue';

const store = useChatStore();
const userInput = ref('');
const messageContainer = ref<HTMLElement | null>(null);
const showSessions = ref(false); // Initially hide sessions to keep it compact

const scrollToBottom = () => {
  nextTick(() => {
    if (messageContainer.value) {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
    }
  });
};

provide('scrollToBottom', scrollToBottom);

const handleSendMessage = async () => {
  if (!userInput.value.trim() || store.isStreaming) return;
  
  const userMsg = userInput.value;
  userInput.value = '';
  
  try {
    await store.sendMessage(userMsg, scrollToBottom);
  } catch (error: any) {
    console.error('Failed to send message:', error);
    userInput.value = userMsg;
  }
};

onMounted(() => {
  store.fetchSessions();
  scrollToBottom();
});
</script>

<style scoped>
.sidebar-assistant {
  position: fixed;
  top: 0;
  right: 0;
  width: 400px;
  height: 100vh;
  background: #fff;
  box-shadow: -2px 0 12px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  z-index: 2999;
}

.sidebar-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #409eff;
  color: white;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 16px;
}

.sidebar-header :deep(.el-button) {
  color: white;
  font-size: 20px;
}

.sidebar-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #f8f9fa;
}

.input-container {
  padding: 16px;
  border-top: 1px solid #f0f0f0;
  background: #fff;
}

.input-box {
  display: flex;
  gap: 10px;
  align-items: flex-end;
}

textarea {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  resize: none;
  font-family: inherit;
  font-size: 14px;
  line-height: 1.5;
}

textarea:focus {
  outline: none;
  border-color: #409eff;
}

.send-btn {
  width: 36px;
  height: 36px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.send-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* Transitions */
.slide-fade-enter-active {
  transition: all 0.3s ease-out;
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(400px);
  opacity: 0;
}
</style>
