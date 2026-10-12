<template>
    <div class="mobile-layout">
        <div class="q-pa-sm">
            <!-- Header Mobile -->
            <div class="header-mobile q-pa-md q-mb-md">
                <div class="row items-center">
                    <q-btn flat round icon="arrow_back" class="text-white" size="md" @click="$router.back()" />
                    <div class="q-ml-sm">
                        <div class="text-subtitle1 text-white text-weight-bold">Novo Agendamento</div>
                        <div class="text-caption text-grey-5">Marque um horário</div>
                    </div>
                </div>

                <!-- Progresso Mobile -->
                <div class="row q-mt-md q-gutter-x-xs">
                    <div v-for="i in 4" :key="i" class="col">
                        <div class="progress-dot" :class="{
                            'active': step >= i,
                            'completed': step > i
                        }">
                            {{ i }}
                        </div>
                    </div>
                </div>
            </div>

            <!-- Conteúdo Mobile -->
            <div class="q-px-md q-pb-xl">
                <!-- Passo 1: Serviço -->
                <div v-if="step === 1" class="step-content">
                    <div class="text-h6 text-white text-weight-bold q-mb-sm">Escolha o Serviço</div>
                    <div class="text-caption text-grey-5 q-mb-md">Selecione um serviço abaixo</div>

                    <div class="q-gutter-y-sm">
                        <q-card v-for="service in services" :key="service.id || service.name" flat bordered class="service-card"
                            :class="{ 'selected': form.serviceId === service.id?.toString() }"
                            @click="selectService(service)">
                            <q-card-section class="q-pa-md">
                                <div class="row items-center">
                                    <q-avatar :style="{ backgroundColor: service.color }" text-color="white" size="45px"
                                        class="q-mr-sm">
                                        <q-icon :name="service.icon || 'content_cut'" size="20px" />
                                    </q-avatar>
                                    <div class="col">
                                        <div class="text-subtitle2 text-white text-weight-bold">{{ service.name }}</div>
                                        <div class="text-caption text-grey-5">
                                            {{ service.duration }}min • R$ {{ service.price.toFixed(2) }}
                                        </div>
                                    </div>
                                    <q-icon
                                        :name="form.serviceId === service.id?.toString() ? 'check_circle' : 'radio_button_unchecked'"
                                        :color="form.serviceId === service.id?.toString() ? 'primary' : 'grey-7'"
                                        size="22px" />
                                </div>
                            </q-card-section>
                        </q-card>
                    </div>
                </div>

                <!-- Passo 2: Profissional -->
                <div v-if="step === 2" class="step-content">
                    <div class="text-h6 text-white text-weight-bold q-mb-sm">Escolha o Profissional</div>
                    <div class="text-caption text-grey-5 q-mb-md">
                        Profissionais disponíveis para este serviço
                    </div>

                    <div v-if="filteredEmployees.length" class="q-gutter-y-sm">
                        <q-card v-for="employee in filteredEmployees" :key="employee.id" flat bordered
                            class="service-card" :class="{ 'selected': form.employeeId === employee.id.toString() }"
                            @click="selectEmployee(employee)">
                            <q-card-section class="q-pa-md">
                                <div class="row items-center">
                                    <q-avatar color="primary" text-color="white" size="45px"
                                        class="q-mr-sm text-weight-bold">
                                        {{ getInitials(employee.name) }}
                                    </q-avatar>
                                    <div class="col">
                                        <div class="text-subtitle2 text-white text-weight-bold">{{ employee.name }}
                                        </div>
                                        <div class="text-caption text-grey-5 flex items-center">
                                            <q-icon name="phone" size="12px" class="q-mr-xs" />
                                            {{ employee.phone }}
                                        </div>
                                    </div>
                                    <q-icon
                                        :name="form.employeeId === employee.id.toString() ? 'check_circle' : 'radio_button_unchecked'"
                                        :color="form.employeeId === employee.id.toString() ? 'primary' : 'grey-7'"
                                        size="22px" />
                                </div>
                            </q-card-section>
                        </q-card>
                    </div>

                    <div v-else class="text-center q-py-xl">
                        <q-icon name="person_off" size="4rem" class="text-grey-7 q-mb-sm" />
                        <div class="text-subtitle1 text-grey-5">Nenhum profissional disponível</div>
                        <div class="text-caption text-grey-6">Volte e escolha outro serviço</div>
                    </div>
                </div>

                <!-- Passo 3: Data e Hora -->
                <div v-if="step === 3" class="step-content">
                    <div class="text-h6 text-white text-weight-bold q-mb-sm">Data e Hora</div>
                    <div class="text-caption text-grey-5 q-mb-md">Escolha data e horário</div>

                    <div class="q-gutter-y-md">
                        <q-input v-model="form.date" label="Data *" filled dark color="primary" bg-color="dark-surface"
                            class="input-mobile" readonly>
                            <template v-slot:prepend>
                                <q-icon name="event" class="cursor-pointer">
                                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                        <q-date v-model="form.date" mask="DD/MM/YYYY" dark color="primary"
                                            :options="dateOptions" minimal class="date-picker-mobile" />
                                    </q-popup-proxy>
                                </q-icon>
                            </template>
                        </q-input>

                        <div>
                            <div class="text-subtitle2 text-white q-mb-sm">Horários Disponíveis</div>
                            <div class="row q-col-gutter-xs">
                                <div v-for="time in availableTimes" :key="time" class="col-4">
                                    <q-btn :label="time" flat no-caps class="time-btn full-width"
                                        :class="{ 'selected': form.time === time }" @click="selectTime(time)" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Passo 4: Cliente -->
                <div v-if="step === 4" class="step-content">
                    <div class="text-h6 text-white text-weight-bold q-mb-sm">Dados do Cliente</div>
                    <div class="text-caption text-grey-5 q-mb-md">Informe os dados do cliente</div>

                    <q-card flat bordered class="summary-card q-mb-md">
                        <q-card-section class="q-pa-md">
                            <div class="text-subtitle2 text-white text-weight-bold q-mb-sm">Resumo</div>

                            <div class="row q-mb-xs">
                                <div class="col-4 text-caption text-grey-5">Serviço:</div>
                                <div class="col-8 text-caption text-white">{{ getServiceName() }}</div>
                            </div>

                            <div class="row q-mb-xs">
                                <div class="col-4 text-caption text-grey-5">Profissional:</div>
                                <div class="col-8 text-caption text-white">{{ getEmployeeName() }}</div>
                            </div>

                            <div class="row q-mb-xs">
                                <div class="col-4 text-caption text-grey-5">Data:</div>
                                <div class="col-8 text-caption text-white">{{ form.date }} às {{ form.time }}</div>
                            </div>

                            <div class="row">
                                <div class="col-4 text-caption text-grey-5">Valor:</div>
                                <div class="col-8 text-caption text-primary text-weight-bold">
                                    R$ {{ getServicePrice() }}
                                </div>
                            </div>
                        </q-card-section>
                    </q-card>

                    <div class="q-gutter-y-md">
                        <q-input v-model="form.clientName" label="Nome do Cliente *" filled dark color="primary"
                            bg-color="dark-surface" class="input-mobile" lazy-rules :rules="[
                                val => !!val || 'Nome é obrigatório',
                                val => val.length >= 3 || 'Mínimo 3 caracteres'
                            ]">
                            <template v-slot:prepend>
                                <q-icon name="person" />
                            </template>
                        </q-input>

                        <q-input v-model="form.clientPhone" label="Telefone *" filled dark color="primary"
                            bg-color="dark-surface" class="input-mobile" mask="(##) #####-####" fill-mask lazy-rules
                            :rules="[
                                val => !!val || 'Telefone é obrigatório',
                                val => val.replace(/\D/g, '').length === 11 || 'Telefone inválido'
                            ]">
                            <template v-slot:prepend>
                                <q-icon name="phone" />
                            </template>
                            <template v-slot:append>
                                <q-icon name="whatsapp" class="text-positive"
                                    v-if="form.clientPhone.replace(/\D/g, '').length === 11" />
                            </template>
                        </q-input>
                    </div>
                </div>
            </div>

            <!-- Botões Fixos Mobile -->
            <!-- Botões Fixos Mobile - Substitua a parte dos botões -->
            <div class="footer-actions q-pa-md">
                <div class="row q-gutter-sm">
                    <!-- Botão Voltar (sempre visível quando step > 1) -->
                    <div v-if="step > 1" class="col">
                        <q-btn label="Voltar" color="grey-7" flat class="full-width btn-action btn-back"
                            icon="arrow_back" @click="goBack" />
                    </div>

                    <!-- Botão Salvar e Continuar / Confirmar -->
                    <div class="col">
                        <q-btn :label="step === 4 ? 'Confirmar' : 'Continuar'" color="primary" unelevated
                            class="full-width btn-action btn-primary" :loading="saving && step === 4"
                            :disable="!canContinue" :icon="step === 4 ? 'check' : 'arrow_forward'" @click="goNext" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import { useQuasar } from 'quasar';
import { useRouter } from 'vue-router';
import { funcionario } from '@/types/funcionario';
import { ServiceForm } from '@/types/servicos';
import { agendamento } from '@/types/agendamento';

const $q = useQuasar();
const router = useRouter();
const step = ref(1);
const saving = ref(false);

const form = reactive<agendamento>({
    serviceId: '',
    employeeId: '',
    date: '',
    time: '',
    clientName: '',
    clientPhone: '',
    notes: '',
});

// Mock de dados (mesmos dados do desktop - idealmente viriam de uma store)
const services = ref<ServiceForm[]>([
    { id: 1, name: 'Corte de Cabelo', duration: 30, price: 50, color: '#6366f1', icon: 'content_cut' },
    { id: 2, name: 'Barba', duration: 20, price: 30, color: '#8b5cf6', icon: 'face' },
    { id: 3, name: 'Corte + Barba', duration: 50, price: 70, color: '#10b981', icon: 'spa' },
    { id: 4, name: 'Manicure', duration: 45, price: 35, color: '#ec4899', icon: 'spa' },
    { id: 5, name: 'Pedicure', duration: 50, price: 40, color: '#f59e0b', icon: 'spa' },
]);

const employees = ref<funcionario[]>([
    { id: 1, name: 'João Silva', phone: '(11) 99999-9999', services: ['1', '2', '3'] },
    { id: 2, name: 'Maria Santos', phone: '(11) 98888-8888', services: ['1', '4', '5'] },
    { id: 3, name: 'Pedro Oliveira', phone: '(11) 97777-7777', services: ['2', '3'] },
]);

const filteredEmployees = computed(() => {
    if (!form.serviceId) return [];
    return employees.value.filter(emp => emp.services.includes(form.serviceId));
});

const availableTimes = [
    '09:00', '09:30', '10:00', '10:30', '11:00', '11:30',
    '13:00', '13:30', '14:00', '14:30', '15:00', '15:30',
    '16:00', '16:30', '17:00', '17:30', '18:00', '18:30',
];

const canContinue = computed(() => {
    switch (step.value) {
        case 1: return !!form.serviceId;
        case 2: return !!form.employeeId;
        case 3: return !!form.date && !!form.time;
        case 4: return !!form.clientName && !!form.clientPhone;
        default: return false;
    }
});

const dateOptions = (date: string) => {
    const today = new Date();
    const selectedDate = new Date(date.split('/').reverse().join('-'));
    const dayOfWeek = selectedDate.getDay();
    return selectedDate >= today && dayOfWeek !== 0;
};

const getInitials = (name: string) => {
    return name.split(' ').map(w => w[0]).join('').toUpperCase().slice(0, 2);
};

const getServiceName = () => {
    const service = services.value.find(s => s.id === Number(form.serviceId));
    return service ? service.name : '';
};

const getEmployeeName = () => {
    const employee = employees.value.find(e => e.id === Number(form.employeeId));
    return employee ? employee.name : '';
};

const getServicePrice = () => {
    const service = services.value.find(s => s.id === Number(form.serviceId));
    return service ? service.price.toFixed(2) : '0.00';
};

const selectService = (service: ServiceForm) => {
    if (service.id) {
        form.serviceId = service.id?.toString()
    }
    form.employeeId = '';
};

const selectEmployee = (employee: funcionario) => {
    if (employee.id)
        form.employeeId = employee.id.toString();
};

const selectTime = (time: string) => {
    form.time = time;
};

const goBack = () => {
    if (step.value > 1) step.value--;
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

const goNext = () => {
    if (step.value === 4) {
        confirmAppointment();
    } else {
        step.value++;
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
};

const confirmAppointment = async () => {
    if (!canContinue.value) return;

    saving.value = true;
    try {
        await new Promise(resolve => setTimeout(resolve, 1000));

        $q.notify({
            type: 'positive',
            message: 'Agendamento confirmado com sucesso!',
            position: 'top',
            timeout: 3000,
            icon: 'check_circle',
        });

        setTimeout(() => router.push('/agendamentos'), 1500);
    } catch (error) {
        $q.notify({
            type: 'negative',
            message: 'Erro ao confirmar agendamento',
            position: 'top',
            timeout: 3000,
            icon: 'error',
        });
    } finally {
        saving.value = false;
    }
};
</script>

<style scoped>
.mobile-layout {
    padding-bottom: 80px;
}

.header-mobile {
    background: linear-gradient(135deg, #0f0f1f 0%, #1a1a2e 100%);
    border-bottom: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 0 0 16px 16px;
}

.progress-dot {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    background: #1a1a2e;
    border: 2px solid rgba(255, 255, 255, 0.1);
    color: #8898aa;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: bold;
    margin: 0 auto;
    position: relative;
    z-index: 1;
    transition: all 0.3s ease;
}

.progress-dot.active {
    background: linear-gradient(135deg, #6366f1, #3b82f6);
    border-color: #6366f1;
    color: white;
    box-shadow: 0 0 20px rgba(99, 102, 241, 0.5);
}

.progress-dot.completed {
    background: #10b981;
    border-color: #10b981;
    color: white;
}

.service-card {
    background: #1a1a2e;
    border: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 16px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.service-card:active {
    transform: scale(0.97);
}

.service-card.selected {
    background: #1f1f35;
    border-color: #6366f1;
    box-shadow: 0 4px 20px rgba(99, 102, 241, 0.2);
}

.time-btn {
    background: #1a1a2e;
    border: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    color: #8898aa;
    min-height: 44px;
    transition: all 0.2s ease;
}

.time-btn.selected {
    background: linear-gradient(135deg, #6366f1, #3b82f6);
    border-color: #6366f1;
    color: white;
    box-shadow: 0 4px 15px rgba(99, 102, 241, 0.3);
}

.summary-card {
    background: #1a1a2e;
    border: 1px solid rgba(99, 102, 241, 0.2);
    border-radius: 16px;
}

.footer-actions {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(10, 10, 18, 0.95);
    backdrop-filter: blur(10px);
    border-top: 1px solid rgba(255, 255, 255, 0.05);
    z-index: 100;
    padding-bottom: calc(16px + env(safe-area-inset-bottom));
}

.btn-action {
    min-height: 52px;
    border-radius: 16px;
    font-weight: 600;
    font-size: 15px;
}

.btn-back {
    background: rgba(255, 255, 255, 0.05) !important;
    border: 1px solid rgba(255, 255, 255, 0.1);
    color: #8898aa !important;
}

.btn-primary {
    background: linear-gradient(135deg, #6366f1, #3b82f6) !important;
    box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
}

.step-content {
    animation: slideIn 0.3s ease;
}

@keyframes slideIn {
    from {
        opacity: 0;
        transform: translateX(20px);
    }

    to {
        opacity: 1;
        transform: translateX(0);
    }
}

.input-mobile :deep(.q-field__control) {
    background: #1a1a2e !important;
    border-radius: 12px !important;
    border: 1px solid rgba(255, 255, 255, 0.05);
    min-height: 56px;
}
</style>