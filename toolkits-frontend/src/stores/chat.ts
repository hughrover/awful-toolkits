import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { chatApi } from '@/api'

interface Session {
  id: number;
  title: string;
}

interface Message {
  role: 'user' | 'assistant' | 'system';
  content: string;
}

export const useChatStore = defineStore('chat', () => {
  const isSidebarVisible = ref(false)
  const sessions = ref<Session[]>([])
  const messages = ref<Message[]>([])
  const currentSessionId = ref<number | null>(null)
  const isStreaming = ref(false)
  const streamingContent = ref('')

  const currentSession = computed(() => sessions.value.find(s => s.id === currentSessionId.value))

  function toggleSidebar() {
    isSidebarVisible.value = !isSidebarVisible.value
  }

  function setSidebarVisible(visible: boolean) {
    isSidebarVisible.value = visible
  }

  const fetchSessions = async () => {
    const res = await chatApi.getSessions()
    sessions.value = res.data
  }

  const createNewSession = async (title = '新对话') => {
    const res = await chatApi.createSession(title)
    await fetchSessions()
    await selectSession(res.data)
  }

  const selectSession = async (session: Session) => {
    currentSessionId.value = session.id
    const res = await chatApi.getHistory(session.id)
    messages.value = res.data
  }

  const deleteSession = async (id: number) => {
    await chatApi.deleteSession(id)
    if (currentSessionId.value === id) {
      currentSessionId.value = null
      messages.value = []
    }
    await fetchSessions()
  }

  const sendMessage = async (userMsg: string, onUpdate?: () => void) => {
    if (!userMsg.trim() || isStreaming.value) return

    try {
      if (!currentSessionId.value) {
        const res = await chatApi.createSession(userMsg.substring(0, 20))
        currentSessionId.value = res.data.id
        await fetchSessions()
      }

      messages.value.push({ role: 'user', content: userMsg })
      onUpdate?.()

      isStreaming.value = true
      streamingContent.value = ''

      const url = `/api/v1/chat/stream?sessionId=${currentSessionId.value}&message=${encodeURIComponent(userMsg)}`
      const eventSource = new EventSource(url)
      
      eventSource.onmessage = (event) => {
        let data = event.data
        if (data.startsWith('"') && data.endsWith('"')) {
          data = JSON.parse(data)
        }
        streamingContent.value += data
        onUpdate?.()
      }

      eventSource.onerror = (error) => {
        console.log('SSE connection closed or error occurred')
        eventSource.close()
        if (streamingContent.value) {
          messages.value.push({ role: 'assistant', content: streamingContent.value })
        }
        streamingContent.value = ''
        isStreaming.value = false
        fetchSessions()
        onUpdate?.()
      }
    } catch (error: any) {
      console.error('Failed to send message:', error)
      isStreaming.value = false
      throw error
    }
  }

  return {
    isSidebarVisible,
    sessions,
    messages,
    currentSessionId,
    isStreaming,
    streamingContent,
    currentSession,
    toggleSidebar,
    setSidebarVisible,
    fetchSessions,
    createNewSession,
    selectSession,
    deleteSession,
    sendMessage
  }
})
