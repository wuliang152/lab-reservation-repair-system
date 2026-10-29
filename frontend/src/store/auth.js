import { defineStore } from 'pinia'
import { authApi } from '../api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: null
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    role: (state) => state.user?.role || ''
  },
  actions: {
    async login(username, password) {
      const res = await authApi.login({ username, password })
      this.token = res.data.token
      localStorage.setItem('token', this.token)
      await this.fetchMe()
    },
    async fetchMe() {
      if (!this.token) return null
      const res = await authApi.me()
      this.user = res.data
      return this.user
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
    }
  }
})
