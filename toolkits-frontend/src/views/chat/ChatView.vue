<template>
  <div class="chat-container">
    <ChatSidebar 
      :sessions="store.sessions" 
      :currentSessionId="store.currentSessionId"
      @new-chat="store.createNewSession"
      @select-session="store.selectSession"
      @delete-session="store.deleteSession"
    />
    
    <div class="chat-main">
      <div class="chat-header">
        <h2>{{ store.currentSession?.title || 'AI 助手' }}</h2>
      </div>
      
      <div class="messages" ref="messageContainer">
        <MessageBubble v-for="(msg, index) in store.messages" :key="index" :role="msg.role" :content="msg.content" />
        <MessageBubble v-if="store.isStreaming" role="assistant" :isStreaming="true" :content="store.streamingContent" />
      </div>
      
      <div class="input-container">
        <div class="skills-bar">
          <button class="skill-btn" @click="useSkill('translate')">翻译</button>
          <button class="skill-btn" @click="useSkill('image')">文生图</button>
        </div>
        <div class="input-box">
          <textarea 
            v-model="userInput" 
            placeholder="输入您的问题..." 
            @keydown.enter.prevent="handleSendMessage"
            rows="3"
          ></textarea>
          <button class="send-btn" :disabled="!userInput.trim() || store.isStreaming" @click="handleSendMessage">
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, provide } from 'vue';
import ChatSidebar from './ChatSidebar.vue';
import MessageBubble from './MessageBubble.vue';
import { useChatStore } from '@/stores/chat';

const store = useChatStore();
const userInput = ref('');
const messageContainer = ref<HTMLElement | null>(null);

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
    alert('发送失败: ' + (error.message || '未知错误'));
    userInput.value = userMsg;
  }
};

const useSkill = (skill: string) => {
  if (skill === 'translate') {
    userInput.value = "请帮我翻译这段话：";
  } else if (skill === 'image') {
    userInput.value = "请帮我生成一张图片：";
  }
};

onMounted(() => {
  store.fetchSessions();
  scrollToBottom();
});
</script>

<style scoped>
.chat-container {
  display: flex;
  height: 100%;
  background: #fff;
}

:global(.app-main:has(.chat-container)) {
  overflow: hidden !important;
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
  overflow: hidden;
}

.chat-header {
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.input-container {
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.skills-bar {
  margin-bottom: 10px;
  display: flex;
  gap: 10px;
}

.skill-btn {
  padding: 4px 12px;
  border-radius: 15px;
  border: 1px solid #e0e0e0;
  background: #fff;
  font-size: 12px;
  cursor: pointer;
}

.skill-btn:hover {
  background: #f5f5f5;
}

.input-box {
  display: flex;
  gap: 10px;
  align-items: flex-end;
}

textarea {
  flex: 1;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  resize: none;
  font-family: inherit;
  font-size: 14px;
}

textarea:focus {
  outline: none;
  border-color: #1a73e8;
}

.send-btn {
  padding: 10px 20px;
  background: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
}

.send-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
