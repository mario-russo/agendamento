import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { Token } from '@/services/authService'

export const useAuthStore = defineStore('auth', () => {
    // Estado inicial reativo
    const tokenAccess = ref<string | null>(null)
    const refresh = ref<string | null>(null)

    // Getter para saber se o usuário está logado
    const isAuthenticated = computed(() => !!tokenAccess.value)

    // Ação para salvar os tokens após o login bem-sucedido
    function setTokens(tokens: Token) {
        tokenAccess.value = tokens.tokenAccess
        refresh.value = tokens.refresh
    }

    // Ação para limpar tudo no logout
    function logout() {
        tokenAccess.value = null
        refresh.value = null
    }

    return {
        tokenAccess,
        refresh,
        isAuthenticated,
        setTokens,
        logout
    }
})
