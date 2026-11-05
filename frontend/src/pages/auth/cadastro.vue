<template>
    <div class="window-height window-width row justify-center items-center bg-grey-2">
        <div class="column q-pa-lg">
            <div class="row">
                <q-card square class="shadow-24 q-pa-md" style="width: 400px;">

                    <q-card-section class="bg-primary text-white text-center q-py-lg">
                        <div class="text-h5 text-weight-bold">Criar Conta</div>
                        <div class="text-subtitle2">Preencha os dados para se cadastrar</div>
                    </q-card-section>

                    <q-card-section class="q-mt-md">
                        <q-form @submit.prevent="handleRegister" class="q-gutter-md">

                            <!-- Nome Completo -->
                            <q-input v-model="formData.name" type="text" label="Nome completo" lazy-rules :rules="[
                                val => (val && val.length >= 3) || 'Digite seu nome completo',
                            ]">
                                <template v-slot:prepend>
                                    <q-icon name="person" />
                                </template>
                            </q-input>

                            <!-- E-mail -->
                            <q-input v-model="formData.email" type="email" label="E-mail" lazy-rules :rules="[
                                val => (val && val.length > 0) || 'Por favor, digite seu e-mail',
                                val => /.+@.+\..+/.test(val) || 'Digite um e-mail válido',
                            ]">
                                <template v-slot:prepend>
                                    <q-icon name="email" />
                                </template>
                            </q-input>

                            <!-- Senha -->
                            <q-input v-model="formData.password" :type="showPassword ? 'text' : 'password'"
                                label="Senha" lazy-rules :rules="[
                                    val => (val && val.length >= 6) || 'A senha deve ter pelo menos 6 caracteres',
                                    val => /[A-Z]/.test(val) || 'Deve conter uma letra maiúscula',
                                    val => /[0-9]/.test(val) || 'Deve conter um número',
                                ]">
                                <template v-slot:prepend>
                                    <q-icon name="lock" />
                                </template>
                                <template v-slot:append>
                                    <q-icon :name="showPassword ? 'visibility_off' : 'visibility'"
                                        class="cursor-pointer" @click="showPassword = !showPassword" />
                                </template>
                            </q-input>

                            <!-- Confirmar Senha -->
                            <q-input v-model="formData.confirmPassword" :type="showPassword ? 'text' : 'password'"
                                label="Confirmar senha" lazy-rules :rules="[
                                    val => (val && val.length > 0) || 'Confirme sua senha',
                                    val => val === formData.password || 'As senhas não coincidem',
                                ]">
                                <template v-slot:prepend>
                                    <q-icon name="lock_reset" />
                                </template>
                            </q-input>

                            <q-card-actions class="q-px-none q-mt-lg">
                                <q-btn type="submit" color="primary" label="Cadastrar"
                                    class="full-width text-weight-bold" size="lg" :loading="loading" />
                            </q-card-actions>

                            <div class="text-center q-mt-sm">
                                <span class="text-grey-8">Já tem conta? </span>
                                <q-btn flat dense color="primary" label="Fazer login" to="login" />
                            </div>

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
import { useRouter } from 'vue-router'
import { register } from '@/services/cadastraService'

const $q = useQuasar()
const loading = ref(false)
const showPassword = ref(false)
const router = useRouter()


interface RegisterInterface {
    name: string
    email: string
    password: string
    confirmPassword: string
}

// Estado reativo do formulário
const formData = reactive<RegisterInterface>({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
})

const handleRegister = async () => {
    loading.value = true
    try {
        const data = await register({
            name: formData.name,
            email: formData.email,
            password: formData.password,
        })

        $q.notify({
            type: 'positive',
            message: 'Cadastro realizado com sucesso! Faça login.',
            position: 'top',
        })

        await router.push('/auth/login')
    } catch (error: any) {
        $q.notify({
            type: 'negative',
            message:
                error.response?.data?.message ||
                'Não foi possível realizar o cadastro. Tente novamente.',
            position: 'top',
        })
    } finally {
        loading.value = false

    }
}
</script>

<style scoped>
.q-card {
    border-radius: 8px !important;
}
</style>