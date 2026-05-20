<template>
  <div :class="['message-row', role]">
    <div class="avatar">{{ role === 'user' ? 'U' : 'AI' }}</div>
    <div class="message-bubble">
      <div v-if="isThinking" class="thinking-dots">
        <span></span>
        <span></span>
        <span></span>
      </div>
      <div v-else class="content markdown-body" v-html="formattedContent" @click="handleImageClick">
      </div>
      <span v-if="isStreaming && !isThinking" class="cursor">|</span>
    </div>

    <!-- Image Viewer -->
    <el-image-viewer
      v-if="showViewer"
      :url-list="[previewUrl]"
      @close="closeViewer"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, inject, ref } from 'vue';
import MarkdownIt from 'markdown-it';

const props = defineProps<{
  role: 'user' | 'assistant' | 'system';
  content?: string;
  isStreaming?: boolean;
}>();

const scrollToBottom = inject('scrollToBottom', () => {});

const isThinking = computed(() => {
  return props.role === 'assistant' && props.isStreaming && (!props.content || props.content.trim() === '');
});

// Image preview state
const showViewer = ref(false);
const previewUrl = ref('');

const handleImageClick = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (target.tagName === 'IMG') {
    previewUrl.value = (target as HTMLImageElement).src;
    showViewer.value = true;
  }
};

const closeViewer = () => {
  showViewer.value = false;
};

const md = new MarkdownIt({
  html: false,
  linkify: true,
  breaks: true
});

const formattedContent = computed(() => {
  const rendered = md.render(props.content || '');
  // Trigger scroll after render in next tick if needed, 
  // though ChatView usually handles this during streaming.
  return rendered;
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
  min-height: 44px; /* Ensure bubble doesn't collapse */
  display: flex;
  align-items: center;
}

.user .message-bubble {
  background: #e8f3ff;
  color: #000;
}

.assistant .message-bubble {
  background: #f1f3f4;
  color: #000;
}

.content {
  width: 100%;
}

/* Markdown Styles */
:deep(.markdown-body) {
  word-wrap: break-word;
}

:deep(.markdown-body p) {
  margin-top: 0;
  margin-bottom: 10px;
}

:deep(.markdown-body p:last-child) {
  margin-bottom: 0;
}

:deep(.markdown-body img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin-top: 10px;
  border: 1px solid #e0e0e0;
  display: block;
  cursor: pointer;
  transition: opacity 0.2s;
}

:deep(.markdown-body img:hover) {
  opacity: 0.9;
}

:deep(.markdown-body code) {
  background: rgba(0,0,0,0.05);
  padding: 2px 4px;
  border-radius: 4px;
  font-family: monospace;
}

:deep(.markdown-body pre) {
  background: #f6f8fa;
  padding: 12px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 10px 0;
}

:deep(.markdown-body ul), :deep(.markdown-body ol) {
  padding-left: 20px;
  margin-bottom: 10px;
}

:deep(.markdown-body table) {
  border-collapse: collapse;
  width: 100%;
  margin: 10px 0;
}

:deep(.markdown-body th), :deep(.markdown-body td) {
  border: 1px solid #dfe2e5;
  padding: 6px 13px;
}

:deep(.markdown-body tr:nth-child(2n)) {
  background-color: #f6f8fa;
}

.thinking-dots {
  display: flex;
  gap: 4px;
  padding: 4px 0;
}

.thinking-dots span {
  width: 6px;
  height: 6px;
  background-color: #909399;
  border-radius: 50%;
  display: inline-block;
  animation: bounce 1.4s infinite ease-in-out both;
}

.thinking-dots span:nth-child(1) {
  animation-delay: -0.32s;
}

.thinking-dots span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% { 
    transform: scale(0);
  } 
  40% { 
    transform: scale(1.0);
  }
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
</style>
