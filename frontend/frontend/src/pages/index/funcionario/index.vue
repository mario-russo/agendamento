<template>
  <q-page class="bg-dark page-employee">
    <div class="q-pa-md">
      <!-- Header -->
      <div class="row items-center q-mb-lg">
        <q-btn
          flat
          round
          icon="arrow_back"
          class="text-white"
          @click="$router.back()"
        />
        <div class="q-ml-md">
          <div class="text-h6 text-white text-weight-bold">Novo Funcionário</div>
          <div class="text-caption text-grey-5">Cadastre um profissional</div>
        </div>
      </div>

      <!-- Formulário -->
      <q-form
        @submit="onSubmit"
        @reset="onReset"
        class="q-gutter-y-md"
      >
        <!-- Nome do Funcionário -->
        <q-input
          v-model="form.name"
          label="Nome Completo *"
          hint="Nome do profissional"
          filled
          dark
          color="primary"
          bg-color="dark-surface"
          lazy-rules
          :rules="[
            val => !!val || 'Nome é obrigatório',
            val => val.length >= 3 || 'Mínimo 3 caracteres',
            val => val.split(' ').length >= 2 || 'Digite nome e sobrenome'
          ]"
        >
          <template v-slot:prepend>
            <q-icon name="person" />
          </template>
        </q-input>

        <!-- Telefone -->
        <q-input
          v-model="form.phone"
          label="Telefone *"
          hint="WhatsApp ou telefone para contato"
          filled
          dark
          color="primary"
          bg-color="dark-surface"
          mask="(##) #####-####"
          fill-mask
          lazy-rules
          :rules="[
            val => !!val || 'Telefone é obrigatório',
            val => val.replace(/\D/g, '').length === 11 || 'Telefone inválido'
          ]"
        >
          <template v-slot:prepend>
            <q-icon name="phone" />
          </template>
          <template v-slot:append>
            <q-icon 
              name="whatsapp" 
              class="text-positive"
              v-if="form.phone.replace(/\D/g, '').length === 11"
            />
          </template>
        </q-input>

        <!-- Serviços Prestados -->
        <div class="q-pa-md bg-dark-surface rounded-borders">
          <div class="text-subtitle2 text-white q-mb-sm">
            Serviços Prestados *
          </div>
          <div class="text-caption text-grey-5 q-mb-md">
            Selecione um ou mais serviços que este profissional realiza
          </div>
          
          <q-list separator class="service-list">
            <q-item
              v-for="service in availableServices"
              :key="service.id || ''"
              tag="label"
              class="service-item"
            >
              <q-item-section avatar>
                <q-checkbox
                  v-model="form.services"
                  :val="service.id"
                  color="primary"
                  size="md"
                />
              </q-item-section>
              
              <q-item-section>
                <q-item-label class="text-white">{{ service.name }}</q-item-label>
                <q-item-label caption class="text-grey-5">
                  {{ service.category }} • {{ service.duration }}min • R$ {{ service.price.toFixed(2) }}
                </q-item-label>
              </q-item-section>
              
              <q-item-section side>
                <q-avatar
                  :style="{ backgroundColor: service.color }"
                  text-color="white"
                  size="35px"
                >
                  <q-icon :name="service.icon || 'content_cut'" size="18px" />
                </q-avatar>
              </q-item-section>
            </q-item>
          </q-list>

          <!-- Mensagem de erro para serviços -->
          <div
            v-if="showServiceError"
            class="text-caption text-negative q-mt-sm"
          >
            Selecione pelo menos um serviço
          </div>
        </div>

        <!-- Status do Funcionário -->
        <q-toggle
          v-model="form.active"
          label="Funcionário Ativo"
          color="primary"
          left-label
        />

        <!-- Botões -->
        <div class="row q-gutter-md q-mt-xl">
          <div class="col">
            <q-btn
              label="Cancelar"
              color="grey-7"
              flat
              class="full-width btn-cancel"
              @click="$router.back()"
            />
          </div>
          <div class="col">
            <q-btn
              label="Salvar Funcionário"
              type="submit"
              color="primary"
              unelevated
              class="full-width btn-save"
              :loading="saving"
            />
          </div>
        </div>
      </q-form>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import { useQuasar } from 'quasar';
import { ServiceForm } from '@/types/servicos';
import { funcionario } from '@/types/funcionario';




const $q = useQuasar();
const saving = ref(false);
const showServiceError = ref(false);

const form = reactive<funcionario>({
  name: '',
  phone: '',
  services: [],
  active: true,
});

const availableServices = ref<ServiceForm[]>([
  {
    id: 1,
    name: 'Corte de Cabelo',
    category: 'Cabelo',
    duration: 30,
    price: 50.00,
    color: '#6366f1',
    icon: 'content_cut',
  },
  {
    id: 2,
    name: 'Barba',
    category: 'Barba',
    duration: 20,
    price: 30.00,
    color: '#8b5cf6',
    icon: 'face',
  },
  {
    id: 3,
    name: 'Manicure',
    category: 'Unhas',
    duration: 45,
    price: 35.00,
    color: '#ec4899',
    icon: 'spa',
  },
  {
    id: 3,
    name: 'Pedicure',
    category: 'Unhas',
    duration: 50,
    price: 40.00,
    color: '#f59e0b',
    icon: 'spa',
  },
  {
    id: 4,
    name: 'Coloração',
    category: 'Cabelo',
    duration: 90,
    price: 120.00,
    color: '#10b981',
    icon: 'palette',
  },
  {
    id: 4,
    name: 'Hidratação',
    category: 'Cabelo',
    duration: 40,
    price: 60.00,
    color: '#14b8a6',
    icon: 'water_drop',
  },
]);

const onSubmit = async () => {
  // Validar serviços
  if (form.services.length === 0) {
    showServiceError.value = true;
    $q.notify({
      type: 'warning',
      message: 'Selecione pelo menos um serviço',
      position: 'top',
      timeout: 3000,
    });
    return;
  }
  
  showServiceError.value = false;
  saving.value = true;
  
  try {
    // Simular chamada API
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    console.log('Dados do funcionário:', form);
    
    $q.notify({
      type: 'positive',
      message: 'Funcionário cadastrado com sucesso!',
      position: 'top',
      timeout: 3000,
    });
    
    // Limpar formulário ou navegar
    // onReset();
    // $router.push('/funcionarios');
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Erro ao cadastrar funcionário',
      position: 'top',
      timeout: 3000,
    });
  } finally {
    saving.value = false;
  }
};

const onReset = () => {
  form.name = '';
  form.phone = '';
  form.services = [];
  form.active = true;
  showServiceError.value = false;
};
</script>

<style scoped>
.page-employee {
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

.service-list {
  background: transparent;
}

.service-item {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  margin-bottom: 4px;
  transition: all 0.2s ease;
}

.service-item:hover {
  background: rgba(255, 255, 255, 0.05);
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

:deep(.q-checkbox__bg) {
  border: 1px solid rgba(255, 255, 255, 0.1);
}
</style>