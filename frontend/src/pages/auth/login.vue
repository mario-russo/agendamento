<template>
    <!-- Container principal que ocupa toda a tela e centraliza o conteúdo -->
    <div class="window-height window-width row justify-center items-center bg-grey-2">
        <div class="column q-pa-lg">
            <div class="row">
                <!-- Card de login com largura máxima definida para ficar elegante -->
                <q-card square class="shadow-24 q-pa-md" style="width: 360px; min-height: 400px;">

                    <q-card-section class="bg-primary text-white text-center q-py-lg">
                        <div class="text-h5 text-weight-bold">Bem-vindo</div>
                        <div class="text-subtitle2">Faça login para continuar</div>
                    </q-card-section>

                    <q-card-section class="q-mt-md">
                        <!-- Formulário Quasar com validações automáticas -->
                        <q-form @submit.prevent="handleLogin" class="q-gutter-md">

                            <!-- Campo de E-mail -->
                            <q-input v-model="formData.email" type="email" label="E-mail" lazy-rules :rules="[
                                val => val && val.length > 0 || 'Por favor, digite seu e-mail',
                                val => /.+@.+\..+/.test(val) || 'Digite um e-mail válido'
                            ]">
                                <template v-slot:prepend>
                                    <q-icon name="email" />
                                </template>
                            </q-input>

                            <!-- Campo de Senha -->
                            <q-input v-model="formData.password" type="password" label="Senha" lazy-rules
                                :rules="[val => val && val.length >= 4 || 'A senha deve ter pelo menos 6 caracteres']">
                                <template v-slot:prepend>
                                    <q-icon name="lock" />
                                </template>
                            </q-input>

                            <!-- Botão de Enviar (Entrar) -->
                            <q-card-actions class="q-px-none q-mt-lg">
                                <q-btn type="submit" color="primary" label="Entrar" class="full-width text-weight-bold"
                                    size="lg" :loading="loading" />
                            </q-card-actions>

                        </q-form>
                    </q-card-section>

                </q-card>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useQuasar } from 'quasar'
import { login, LoginInteface, } from '@/services/authService'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const $q = useQuasar()
const loading = ref(false)
const router = useRouter()
const authStore = useAuthStore()

// Estado reativo do formulário baseado na sua Interface
const formData = reactive<LoginInteface>({
    email: '',
    password: ''
})

const handleLogin = async () => {
    loading.value = true
    try {
        const data = await login({
            email: formData.email,
            password: formData.password
        })

        $q.notify({
            type: 'positive',
            message: 'Login realizado com sucesso!',
            position: 'top'
        })

        if (data.status == 200) {
            authStore.setTokens(data.data)
            await router.push("/")
        }

    } catch (error: any) {
        $q.notify({
            type: 'negative',
            message: error.response?.data?.message || 'Falha na autenticação. Verifique seus dados.',
            position: 'top'
        })
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
/* Estilos adicionais leves caso queira customizar o visual do card */
.q-card {
    border-radius: 8px !important;
}
</style>
