<template>
    <div class="desktop-layout">
        <div class="q-pa-xl">
            <!-- Header Desktop -->
            <div class="header-desktop q-pa-lg q-mb-xl">
                <div class="row items-center justify-between">
                    <div class="row items-center">
                        <q-btn flat round icon="arrow_back" class="text-white" size="md" @click="$router.back()" />
                        <div class="q-ml-md">
                            <div class="text-h5 text-white text-weight-bold">Novo Agendamento</div>
                            <div class="text-subtitle2 text-grey-5">Marque um horário para seu cliente</div>
                        </div>
                    </div>
                    <q-badge color="primary" class="q-px-md q-py-sm text-subtitle2" rounded>
                        Passo {{ step }} de 4
                    </q-badge>
                </div>
            </div>

            <!-- Stepper Desktop -->
            <q-stepper v-model="step" color="primary" animated flat header-nav class="bg-transparent stepper-desktop">
                <!-- Passo 1: Serviço -->
                <q-step :name="1" title="Serviço" icon="content_cut" :done="step > 1">
                    <div class="row q-col-gutter-lg">
                        <div v-for="service in services" :key="service.id" class="col-12 col-md-4">
                            <q-card flat bordered class="service-card-desktop"
                                :class="{ 'selected': form.serviceId === service.id?.toString() }" @click="selectService(service)">
                                <q-card-section class="q-pa-lg">
                                    <div class="row items-center q-mb-md">
                                        <q-avatar :style="{ backgroundColor: service.color }" text-color="white"
                                            size="56px">
                                            <q-icon :name="service.icon || 'content_cut'" size="28px" />
                                        </q-avatar>
                                        <q-space />
                                        <q-icon
                                            :name="form.serviceId === service.id?.toString() ? 'check_circle' : 'radio_button_unchecked'"
                                            :color="form.serviceId === service.id?.toString()? 'primary' : 'grey-7'" size="28px" />
                                    </div>
                                    <div class="text-h6 text-white text-weight-bold">{{ service.name }}</div>
                                    <div class="text-subtitle2 text-grey-5 q-mt-sm">
                                        {{ service.duration }}min • R$ {{ service.price.toFixed(2) }}
                                    </div>
                                </q-card-section>
                            </q-card>
                        </div>
                    </div>
                </q-step>

                <!-- Passo 2: Profissional -->
                <q-step :name="2" title="Profissional" icon="person" :done="step > 2">
                    <div class="row q-col-gutter-lg">
                        <div v-for="employee in filteredEmployees" :key="employee.id" class="col-12 col-md-4">
                            <q-card flat bordered class="service-card-desktop"
                                :class="{ 'selected': form.employeeId === employee.id?.toString() }"
                                @click="selectEmployee(employee)">
                                <q-card-section class="q-pa-lg text-center">
                                    <q-avatar color="primary" text-color="white" size="80px"
                                        class="q-mb-md text-h4 text-weight-bold">
                                        {{ getInitials(employee.name) }}
                                    </q-avatar>
                                    <div class="text-h6 text-white text-weight-bold">{{ employee.name }}</div>
                                    <div class="text-subtitle2 text-grey-5 q-mt-sm flex items-center justify-center">
                                        <q-icon name="phone" size="16px" class="q-mr-xs" />
                                        {{ employee.phone }}
                                    </div>
                                    <q-badge color="primary" class="q-mt-md" rounded>
                                        {{ employee.services.length }} serviços
                                    </q-badge>
                                </q-card-section>
                            </q-card>
                        </div>
                    </div>
                </q-step>

                <!-- Passo 3: Data e Hora -->
                <q-step :name="3" title="Data e Hora" icon="schedule" :done="step > 3">
                    <div class="row q-col-gutter-lg">
                        <div class="col-12 col-md-6">
                            <q-card flat bordered class="calendar-card">
                                <q-card-section class="q-pa-lg">
                                    <div class="text-h6 text-white text-weight-bold q-mb-md">Data</div>
                                    <q-date v-model="form.date" mask="DD/MM/YYYY" dark color="primary"
                                        :options="dateOptions" minimal class="date-picker-desktop full-width" />
                                </q-card-section>
                            </q-card>
                        </div>

                        <div class="col-12 col-md-6">
                            <q-card flat bordered class="calendar-card">
                                <q-card-section class="q-pa-lg">
                                    <div class="text-h6 text-white text-weight-bold q-mb-md">Horários Disponíveis</div>
                                    <div class="row q-col-gutter-sm">
                                        <div v-for="time in availableTimes" :key="time" class="col-4 col-md-3">
                                            <q-btn :label="time" flat no-caps
                                                class="time-btn-desktop full-width q-mb-sm"
                                                :class="{ 'selected': form.time === time }" @click="selectTime(time)" />
                                        </div>
                                    </div>
                                </q-card-section>
                            </q-card>
                        </div>
                    </div>
                </q-step>

                <!-- Passo 4: Cliente -->
                <q-step :name="4" title="Cliente" icon="person_add">
                    <div class="row q-col-gutter-lg">
                        <!-- Resumo -->
                        <div class="col-12 col-md-4">
                            <q-card flat bordered class="summary-card-desktop">
                                <q-card-section class="q-pa-lg">
                                    <div class="text-h6 text-white text-weight-bold q-mb-md">Resumo do Agendamento</div>

                                    <q-list separator class="bg-transparent">
                                        <q-item>
                                            <q-item-section avatar>
                                                <q-icon name="content_cut" color="primary" />
                                            </q-item-section>
                                            <q-item-section>
                                                <q-item-label class="text-caption text-grey-5">Serviço</q-item-label>
                                                <q-item-label class="text-white">{{ getServiceName() }}</q-item-label>
                                            </q-item-section>
                                        </q-item>

                                        <q-item>
                                            <q-item-section avatar>
                                                <q-icon name="person" color="primary" />
                                            </q-item-section>
                                            <q-item-section>
                                                <q-item-label
                                                    class="text-caption text-grey-5">Profissional</q-item-label>
                                                <q-item-label class="text-white">{{ getEmployeeName() }}</q-item-label>
                                            </q-item-section>
                                        </q-item>

                                        <q-item>
                                            <q-item-section avatar>
                                                <q-icon name="schedule" color="primary" />
                                            </q-item-section>
                                            <q-item-section>
                                                <q-item-label class="text-caption text-grey-5">Data e
                                                    Hora</q-item-label>
                                                <q-item-label class="text-white">{{ form.date }} às {{ form.time
                                                    }}</q-item-label>
                                            </q-item-section>
                                        </q-item>

                                        <q-item>
                                            <q-item-section avatar>
                                                <q-icon name="attach_money" color="primary" />
                                            </q-item-section>
                                            <q-item-section>
                                                <q-item-label class="text-caption text-grey-5">Valor</q-item-label>
                                                <q-item-label class="text-primary text-weight-bold text-h6">
                                                    R$ {{ getServicePrice() }}
                                                </q-item-label>
                                            </q-item-section>
                                        </q-item>
                                    </q-list>
                                </q-card-section>
                            </q-card>
                        </div>

                        <!-- Formulário -->
                        <div class="col-12 col-md-8">
                            <q-card flat bordered class="form-card-desktop">
                                <q-card-section class="q-pa-lg">
                                    <div class="text-h6 text-white text-weight-bold q-mb-lg">Dados do Cliente</div>

                                    <div class="q-gutter-y-md">
                                        <q-input v-model="form.clientName" label="Nome do Cliente *" filled dark
                                            color="primary" bg-color="dark-surface" class="input-desktop" lazy-rules
                                            :rules="[
                                                val => !!val || 'Nome é obrigatório',
                                                val => val.length >= 3 || 'Mínimo 3 caracteres'
                                            ]">
                                            <template v-slot:prepend>
                                                <q-icon name="person" />
                                            </template>
                                        </q-input>

                                        <q-input v-model="form.clientPhone" label="Telefone *" filled dark
                                            color="primary" bg-color="dark-surface" class="input-desktop"
                                            mask="(##) #####-####" fill-mask lazy-rules :rules="[
                                                val => !!val || 'Telefone é obrigatório',
                                                val => val.replace(/\D/g, '').length === 11 || 'Telefone inválido'
                                            ]">
                                            <template v-slot:prepend>
                                                <q-icon name="phone" />
                                            </template>
                                        </q-input>

                                        <q-input v-model="form.notes" label="Observações"
                                            hint="Alguma observação sobre o agendamento (opcional)" filled dark
                                            type="textarea" color="primary" bg-color="dark-surface"
                                            class="input-desktop" autogrow :maxlength="200" counter>
                                            <template v-slot:prepend>
                                                <q-icon name="notes" />
                                            </template>
                                        </q-input>
                                    </div>
                                </q-card-section>
                            </q-card>
                        </div>
                    </div>
                </q-step>

                <!-- Navegação Desktop -->
                <template v-slot:navigation>
                    <div class="row justify-end q-gutter-md q-pa-lg">
                        <q-btn v-if="step > 1" label="Voltar" color="grey-7" flat class="btn-desktop btn-back"
                            @click="goBack" />
                        <q-btn :label="step === 4 ? 'Confirmar Agendamento' : 'Continuar'" color="primary" unelevated
                            class="btn-desktop btn-primary-desktop" :loading="saving && step === 4"
                            :disable="!canContinue" @click="goNext" />
                    </div>
                </template>
            </q-stepper>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import { useQuasar } from 'quasar';
import { useRouter } from 'vue-router';
import { ServiceForm } from '@/types/servicos';
import { funcionario } from '@/types/funcionario';
import { agendamento } from '@/types/agendamento';

const $q = useQuasar();
const router = useRouter();
// const store = useAppointmentStore();

const step = ref(1);
const saving = ref(false);

// Usar store ou estado local
const form = reactive<agendamento>({
    serviceId: '',
    employeeId: '',
    date: '',
    time: '',
    clientName: '',
    clientPhone: '',
    notes: '',
});

// Dados mockados (depois virão da store/API)
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
.desktop-layout {
    max-width: 1200px;
    margin: 0 auto;
}

.header-desktop {
    background: linear-gradient(135deg, #0f0f1f 0%, #1a1a2e 100%);
    border-bottom: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 16px;
}

.stepper-desktop {
    background: transparent;
}

.service-card-desktop {
    background: #1a1a2e;
    border: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 16px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    min-height: 200px;
}

.service-card-desktop:hover {
    transform: translateY(-4px);
    border-color: rgba(99, 102, 241, 0.3);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
}

.service-card-desktop.selected {
    background: #1f1f35;
    border-color: #6366f1;
    box-shadow: 0 8px 30px rgba(99, 102, 241, 0.2);
}

.calendar-card,
.summary-card-desktop,
.form-card-desktop {
    background: #1a1a2e;
    border: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 16px;
}

.time-btn-desktop {
    background: #0f0f1f;
    border: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    color: #8898aa;
    min-height: 44px;
    transition: all 0.2s ease;
}

.time-btn-desktop:hover {
    background: #1a1a2e;
    border-color: rgba(99, 102, 241, 0.3);
}

.time-btn-desktop.selected {
    background: linear-gradient(135deg, #6366f1, #3b82f6);
    border-color: #6366f1;
    color: white;
    box-shadow: 0 4px 15px rgba(99, 102, 241, 0.3);
}

.btn-desktop {
    min-width: 150px;
    min-height: 48px;
    border-radius: 12px;
    font-weight: 600;
    font-size: 16px;
}

.btn-primary-desktop {
    background: linear-gradient(135deg, #6366f1, #3b82f6) !important;
    box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
}

.input-desktop :deep(.q-field__control) {
    background: #0f0f1f !important;
    border-radius: 12px !important;
    border: 1px solid rgba(255, 255, 255, 0.05);
    min-height: 56px;
}
</style>