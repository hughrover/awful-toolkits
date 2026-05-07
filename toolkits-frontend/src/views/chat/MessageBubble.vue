<template>
  <div :class="['message-row', role]">
    <div class="avatar">{{ role === 'user' ? 'U' : 'AI' }}</div>
    <div class="message-bubble">
      <div class="content" v-html="formattedContent">
      </div>
      <span v-if="isStreaming" class="cursor">|</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  role: 'user' | 'assistant' | 'system';
  content?: string;
  isStreaming?: boolean;
}>();

const formattedContent = computed(() => {
  const text = props.content || '';
  // Simple markdown-like formatting
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br/>')
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`(.*?)`/g, '<code>$1</code>');
});
</script>

<style scoped>
.message-row {
  display: flex;
  margin-bottom: 24px;
}

.message-row.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #1a73e8;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  flex-shrink: 0;
}

.user .avatar {
  background: #34a853;
  margin-left: 12px;
}

.assistant .avatar {
  margin-right: 12px;
}

.message-bubble {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 12px;
  font-size: 15px;
  line-height: 1.5;
  word-break: break-word;
}

.user .message-bubble {
  background: #e8f3ff;
  color: #000;
}

.assistant .message-bubble {
  background: #f1f3f4;
  color: #000;
}

.cursor {
  display: inline-block;
  width: 2px;
  height: 1em;
  background: currentColor;
  margin-left: 2px;
  vertical-align: middle;
  animation: blink 1s infinite;
}

@keyframes blink {
  0% { opacity: 1; }
  50% { opacity: 0; }
  100% { opacity: 1; }
}

:deep(code) {
  background: rgba(0,0,0,0.05);
  padding: 2px 4px;
  border-radius: 4px;
}
</style>
