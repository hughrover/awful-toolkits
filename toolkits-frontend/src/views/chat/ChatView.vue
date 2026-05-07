<template>
  <div class="chat-container">
    <ChatSidebar 
      :sessions="sessions" 
      :currentSessionId="currentSessionId"
      @new-chat="createNewSession"
      @select-session="selectSession"
      @delete-session="deleteSession"
    />
    
    <div class="chat-main">
      <div class="chat-header">
        <h2>{{ currentSession?.title || 'AI 助手' }}</h2>
      </div>
      
      <div class="messages" ref="messageContainer">
        <MessageBubble v-for="(msg, index) in messages" :key="index" :role="msg.role" :content="msg.content" />
        <MessageBubble v-if="isStreaming" role="assistant" :isStreaming="true">
          {{ streamingContent }}
        </MessageBubble>
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
            @keydown.enter.prevent="sendMessage"
            rows="3"
          ></textarea>
          <button class="send-btn" :disabled="!userInput.trim() || isStreaming" @click="sendMessage">
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue';
import ChatSidebar from './ChatSidebar.vue';
import MessageBubble from './MessageBubble.vue';
import { chatApi } from '@/api';

interface Session {
  id: number;
  title: string;
}

interface Message {
  role: 'user' | 'assistant' | 'system';
  content: string;
}

const sessions = ref<Session[]>([]);
const messages = ref<Message[]>([]);
const userInput = ref('');
const currentSessionId = ref<number | null>(null);
const isStreaming = ref(false);
const streamingContent = ref('');
const messageContainer = ref<HTMLElement | null>(null);

const currentSession = computed(() => sessions.value.find(s => s.id === currentSessionId.value));

const scrollToBottom = () => {
  nextTick(() => {
    if (messageContainer.value) {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
    }
  });
};

const fetchSessions = async () => {
  const res = await chatApi.getSessions();
  sessions.value = res.data;
};

const createNewSession = async () => {
  const res = await chatApi.createSession('新对话');
  await fetchSessions();
  selectSession(res.data);
};

const selectSession = async (session: Session) => {
  currentSessionId.value = session.id;
  const res = await chatApi.getHistory(session.id);
  messages.value = res.data;
  scrollToBottom();
};

const deleteSession = async (id: number) => {
  await chatApi.deleteSession(id);
  if (currentSessionId.value === id) {
    currentSessionId.value = null;
    messages.value = [];
  }
  await fetchSessions();
};

const sendMessage = async () => {
  if (!userInput.value.trim() || isStreaming.value) return;
  
  const userMsg = userInput.value;
  userInput.value = ''; // Clear input immediately for better UX
  
  try {
    if (!currentSessionId.value) {
      const res = await chatApi.createSession(userMsg.substring(0, 20));
      currentSessionId.value = res.data.id;
      await fetchSessions();
    }

    messages.value.push({ role: 'user', content: userMsg });
    scrollToBottom();

    isStreaming.value = true;
    streamingContent.value = '';

    const url = `/api/v1/chat/stream?sessionId=${currentSessionId.value}&message=${encodeURIComponent(userMsg)}`;
    const eventSource = new EventSource(url);
    
    eventSource.onmessage = (event) => {
      let data = event.data;
      if (data.startsWith('"') && data.endsWith('"')) {
        data = JSON.parse(data);
      }
      streamingContent.value += data;
      scrollToBottom();
    };

    eventSource.onerror = (error) => {
      console.log('SSE connection closed or error occurred');
      eventSource.close();
      if (streamingContent.value) {
        messages.value.push({ role: 'assistant', content: streamingContent.value });
      }
      streamingContent.value = '';
      isStreaming.value = false;
      fetchSessions();
    };
  } catch (error: any) {
    console.error('Failed to send message:', error);
    alert('发送失败: ' + (error.message || '未知错误'));
    isStreaming.value = false;
    userInput.value = userMsg; // Restore input on error
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
  fetchSessions();
});
</script>

<style scoped>
.chat-container {
  display: flex;
  height: calc(100vh - 60px);
  background: #fff;
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
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
