import { defineBoot } from '#q-app'
import axios from 'axios'

// Cria uma instância isolada do Axios apontando para o seu Quarkus JAR
const api = axios.create({ 
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080' 
})
export default defineBoot(({ app }) => {
  // Torna o $axios e a $api disponíveis dentro dos componentes Vue (Options API)
  app.config.globalProperties.$axios = axios
  app.config.globalProperties.$api = api
})

// Permite importar o { api } diretamente em arquivos de script (Composition API)
export { api }
