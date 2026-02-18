import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const postService = {
  getAll: () => api.get('/posts'),
  getUserPosts: (wallet) => api.get(`/posts/user/${wallet}`),
  create: (data) => api.post('/posts', data),
  like: (postId, wallet) => api.post(`/posts/${postId}/like/${wallet}`),
  unlike: (postId, wallet) => api.delete(`/posts/${postId}/like/${wallet}`),
  getComments: (postId) => api.get(`/posts/${postId}/comments`),
  addComment: (postId, data) => api.post(`/posts/${postId}/comments`, data),
}

export const userService = {
  getProfile: (wallet) => api.get(`/users/${wallet}`),
  updateProfile: (wallet, data) => api.put(`/users/${wallet}`, data),
  follow: (data) => api.post('/users/follow', data),
  unfollow: (data) => api.post('/users/unfollow', data),
  getFollowers: (wallet) => api.get(`/users/${wallet}/followers`),
  getFollowing: (wallet) => api.get(`/users/${wallet}/following`),
}

export const notificationService = {
  getAll: (wallet) => api.get(`/notifications/${wallet}`),
  getUnread: (wallet) => api.get(`/notifications/${wallet}/unread`),
  getCount: (wallet) => api.get(`/notifications/${wallet}/count`),
  markAsRead: (id) => api.put(`/notifications/${id}/read`),
  markAllAsRead: (wallet) => api.put(`/notifications/${wallet}/read-all`),
}

export default api
