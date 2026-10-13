<template>
    <q-page class="bg-dark page-form">
        <div class="q-pa-md">
            <!-- Header -->
            <div class="row items-center q-mb-lg">
                <q-btn flat round icon="arrow_back" class="text-white" @click="$router.back()" />
                <div class="q-ml-md">
                    <div class="text-h6 text-white text-weight-bold">Novo Serviço</div>
                    <div class="text-caption text-grey-5">Cadastre um serviço oferecido</div>
                </div>
            </div>

            <!-- Formulário -->
            <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-y-md">
                <!-- Nome do Serviço -->
                <q-input v-model="form.name" label="Nome do Serviço *" hint="Ex: Corte de Cabelo, Manicure, etc." filled
                    dark color="primary" bg-color="dark-surface" lazy-rules :rules="[
                        val => !!val || 'Nome é obrigatório',
                        val => val.length >= 3 || 'Mínimo 3 caracteres'
                    ]">
                    <template v-slot:prepend>
                        <q-icon name="content_cut" />
                    </template>
                </q-input>

                <!-- Descrição -->
                <q-input v-model="form.description" label="Descrição" hint="Breve descrição do serviço (opcional)"
                    filled dark type="textarea" color="primary" bg-color="dark-surface" autogrow :maxlength="200"
                    counter>
                    <template v-slot:prepend>
                        <q-icon name="description" />
                    </template>
                </q-input>

                <!-- Preço e Duração -->
                <div class="row q-col-gutter-md">
                    <div class="col-6">
                        <q-input v-model="form.price" label="Preço *" hint="Valor em R$" filled dark color="primary"
                            bg-color="dark-surface" prefix="R$" mask="#.##" fill-mask="0" reverse-fill-mask lazy-rules
                            :rules="[
                                val => !!val || 'Preço é obrigatório',
                                val => parseFloat(val) > 0 || 'Preço deve ser maior que zero'
                            ]">
                            <template v-slot:prepend>
                                <q-icon name="attach_money" />
                            </template>
                        </q-input>
                    </div>

                    <div class="col-6">
                        <q-input v-model="form.duration" label="Duração *" hint="Em minutos" filled dark type="number"
                            color="primary" bg-color="dark-surface" suffix="min" min="10" max="480" step="5" lazy-rules
                            :rules="[
                                val => !!val || 'Duração é obrigatória',
                                val => val >= 10 || 'Mínimo 10 minutos',
                                val => val <= 480 || 'Máximo 8 horas'
                            ]">
                            <template v-slot:prepend>
                                <q-icon name="schedule" />
                            </template>
                        </q-input>
                    </div>
                </div>

                <!-- Categoria -->
                <!-- <q-select v-model="form.category" label="Categoria *" filled dark color="primary"
                    bg-color="dark-surface" :options="categories" lazy-rules
                    :rules="[val => !!val || 'Categoria é obrigatória']">
                    <template v-slot:prepend>
                        <q-icon name="category" />
                    </template>
                </q-select> -->

                <!-- Status -->
                <q-select v-model="form.status" label="Status" filled dark color="primary" bg-color="dark-surface"
                    :options="statusOptions">
                    <template v-slot:prepend>
                        <q-icon name="info" />
                    </template>
                </q-select>

                <!-- Cor para identificação -->
                <!-- <div class="q-pa-md bg-dark-surface rounded-borders">
                    <div class="text-subtitle2 text-white q-mb-sm">Cor de Identificação</div>
                    <div class="row q-gutter-sm">
                        <q-btn v-for="color in colors" :key="color.value" round
                            :style="{ backgroundColor: color.value }"
                            :class="{ 'color-selected': form.color === color.value }" @click="form.color = color.value">
                            <q-icon v-if="form.color === color.value" name="check" color="white" />
                        </q-btn>
                    </div>
                </div> -->

                <!-- Toggle de Disponibilidade -->
                <q-toggle v-model="form.active" label="Serviço Ativo" color="primary" left-label />

                <!-- Botões -->
                <div class="row q-gutter-md q-mt-xl">
                    <div class="col">
                        <q-btn label="Cancelar" color="grey-7" flat class="full-width btn-cancel"
                            @click="$router.back()" />
                    </div>
                    <div class="col">
                        <q-btn label="Salvar Serviço" type="submit" color="primary" unelevated
                            class="full-width btn-save" :loading="saving" />
                    </div>
                </div>
            </q-form>
        </div>
    </q-page>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useQuasar } from 'quasar';
import { ServiceForm } from '@/types/servicos';




const $q = useQuasar();
const saving = ref(false);

const form = reactive<ServiceForm>({
    name: '',
    description: '',
    price: 0,
    duration: null,
    category: '',
    status: 'active',
    color: '#6366f1',
    active: true,
});

const categories = [
    'Cabelo',
    'Barba',
    'Unhas',
    'Estética',
    'Massagem',
    'Outros',
];

const statusOptions = [
    { label: 'Ativo', value: 'active' },
    { label: 'Inativo', value: 'inactive' },
];

const colors = [
    { value: '#6366f1', label: 'Azul' },
    { value: '#10b981', label: 'Verde' },
    { value: '#f59e0b', label: 'Laranja' },
    { value: '#ef4444', label: 'Vermelho' },
    { value: '#8b5cf6', label: 'Roxo' },
    { value: '#ec4899', label: 'Rosa' },
];

const onSubmit = async () => {
    saving.value = true;
    try {
        // Simular chamada API
        await new Promise(resolve => setTimeout(resolve, 1000));

        console.log('Dados do formulário:', form);

        $q.notify({
            type: 'positive',
            message: 'Serviço cadastrado com sucesso!',
            position: 'top',
            timeout: 3000,
        });

        // Limpar formulário ou navegar
        // onReset();
        // $router.push('/servicos');
    } catch (error) {
        $q.notify({
            type: 'negative',
            message: 'Erro ao cadastrar serviço',
            position: 'top',
            timeout: 3000,
        });
    } finally {
        saving.value = false;
    }
};

const onReset = () => {
    form.name = '';
    form.description = '';
    form.price = 0;
    form.duration = null;
    form.category = '';
    form.status = 'active';
    form.color = '#6366f1';
    form.active = true;
};
</script>

<style scoped>
.page-form {
    background: #0a0a12;
    min-height: 100vh;
}

.bg-dark-surface {
    background: #1a1a2e !important;
    border: 1px solid rgba(255, 255, 255, 0.05);
}

.rounded-borders {
    border-radius: 12px;
}

.color-selected {
    border: 3px solid #fff;
    transform: scale(1.1);
}

.btn-save {
    background: linear-gradient(135deg, #6366f1, #3b82f6) !important;
    border-radius: 12px !important;
    height: 48px !important;
    font-weight: 600 !important;
    box-shadow: 0 4px 15px rgba(99, 102, 241, 0.3);
}

.btn-cancel {
    border-radius: 12px !important;
    height: 48px !important;
    border: 1px solid rgba(255, 255, 255, 0.1);
}

/* Ajustes para inputs dark */
:deep(.q-field--dark .q-field__control) {
    background: #1a1a2e !important;
    border-radius: 12px !important;
    border: 1px solid rgba(255, 255, 255, 0.05);
}

:deep(.q-field--dark .q-field__label) {
    color: #8898aa;
}

:deep(.q-field--dark .q-field__native) {
    color: #ffffff;
}
</style>