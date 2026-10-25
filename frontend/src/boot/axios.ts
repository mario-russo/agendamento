import { defineBoot } from '#q-app'
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  timeout: 15000,
})

export default defineBoot(({ app }) => {
  // Options API: this.$api
  app.config.globalProperties.$api = api
})

// Composition API: import { api } from 'boot/axios'
export { api }