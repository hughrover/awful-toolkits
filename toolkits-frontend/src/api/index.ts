import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.response.use(
  (response) => {
    const data = response.data
    if (data.code !== 200) {
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    return data
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default api

export const chatApi = {
  getSessions: () => api.get('/chat/sessions'),
  createSession: (title: string) => api.post('/chat/sessions', { title }),
  deleteSession: (id: number) => api.delete(`/chat/sessions/${id}`),
  getHistory: (sessionId: number) => api.get(`/chat/history/${sessionId}`),
}
