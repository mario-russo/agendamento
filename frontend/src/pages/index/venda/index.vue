<template>
    <q-page class="q-pa-md" :class="{ 'dark-mode': $q.dark.isActive }">
        <!-- Cabeçalho -->
        <div class="row items-center q-mb-md">
            <div class="col">
                <div class="text-h5 text-bold">Pagamentos</div>
                <div class="text-caption text-grey-7">Gerencie os pagamentos dos agendamentos</div>
            </div>
            <div class="col-auto">
                <q-btn color="primary" icon="refresh" round dense @click="loadData">
                    <q-tooltip>Atualizar</q-tooltip>
                </q-btn>
            </div>
        </div>

        <!-- Tabs de filtro -->
        <div class="row q-mb-md">
            <q-tabs v-model="activeTab" dense class="bg-transparent full-width" active-color="primary"
                indicator-color="primary" align="justify" narrow-indicator>
                <q-tab name="all" label="Todos" />
                <q-tab name="pending" label="Pendentes" />
                <q-tab name="confirmed" label="Agendados" />
                <q-tab name="paid" label="Pagos" />
            </q-tabs>
        </div>

        <!-- Lista de agendamentos -->
        <div class="appointments-list">
            <div v-if="filteredAppointments.length === 0" class="text-center q-pa-xl">
                <q-icon name="event_busy" size="64px" color="grey-5" />
                <div class="text-h6 q-mt-md text-grey-6">Nenhum agendamento encontrado</div>
                <div class="text-caption text-grey-5">Todos os agendamentos estão em dia</div>
            </div>

            <div v-for="appointment in filteredAppointments" :key="appointment.id" class="appointment-card q-mb-md"
                :class="{
                    'dark-card': $q.dark.isActive,
                    'paid-card': appointment.paymentStatus === 'paid',
                    'pending-card': appointment.paymentStatus === 'pending',
                    'confirmed-card': appointment.paymentStatus === 'confirmed'
                }">
                <div class="row items-center q-col-gutter-sm">
                    <!-- Avatar/Ícone -->
                    <div class="col-auto">
                        <q-avatar :color="getStatusColor(appointment.paymentStatus)" text-color="white" size="48px">
                            <q-icon :name="getStatusIcon(appointment.paymentStatus)" />
                        </q-avatar>
                    </div>

                    <!-- Informações -->
                    <div class="col">
                        <div class="row items-center">
                            <div class="col">
                                <div class="text-subtitle1 text-weight-bold">
                                    {{ appointment.clientName }}
                                </div>
                                <div class="text-caption text-grey-7">
                                    <q-icon name="event" size="14px" class="q-mr-xs" />
                                    {{ formatDate(appointment.date) }} - {{ formatTime(appointment.time) }}
                                </div>
                                <div class="text-caption text-grey-7">
                                    <q-icon name="business_center" size="14px" class="q-mr-xs" />
                                    {{ getServiceName(appointment.serviceId) }}
                                </div>
                                <div class="text-caption text-grey-7">
                                    <q-icon name="person" size="14px" class="q-mr-xs" />
                                    {{ getEmployeeName(appointment.employeeId) }}
                                </div>
                            </div>
                            <div class="col-auto text-right">
                                <div class="text-h6 text-weight-bold text-primary">
                                    {{ formatPrice(getServicePrice(appointment.serviceId)) }}
                                </div>
                                <q-badge :color="getPaymentStatusColor(appointment.paymentStatus)" class="q-mt-xs"
                                    rounded>
                                    {{ getPaymentStatusLabel(appointment.paymentStatus) }}
                                </q-badge>
                            </div>
                        </div>

                        <!-- Observações -->
                        <div v-if="appointment.notes" class="q-mt-sm">
                            <q-chip size="sm" icon="description" class="bg-grey-2"
                                :class="{ 'dark-chip': $q.dark.isActive }">
                                {{ appointment.notes }}
                            </q-chip>
                        </div>

                        <!-- Ações -->
                        <div class="row q-mt-sm q-gutter-xs">
                            <q-btn
                                v-if="appointment.paymentStatus === 'pending' || appointment.paymentStatus === 'confirmed'"
                                color="primary" label="Confirmar Pagamento" size="sm" icon="payment"
                                @click="confirmPayment(appointment)" />
                            <q-btn v-if="appointment.paymentStatus === 'pending'" color="negative" label="Cancelar"
                                size="sm" flat icon="close" @click="cancelAppointment(appointment)" />
                            <q-btn v-if="appointment.paymentStatus === 'paid'" color="positive" label="Pago" size="sm"
                                flat icon="check_circle" disable />
                            <q-btn color="grey" label="Detalhes" size="sm" flat icon="info"
                                @click="showDetails(appointment)" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Dialog de confirmação de pagamento -->
        <q-dialog v-model="showPaymentDialog" persistent>
            <q-card :class="{ 'dark-card': $q.dark.isActive }" style="min-width: 350px; max-width: 500px;">
                <q-card-section class="row items-center">
                    <q-avatar color="primary" text-color="white" icon="payment" />
                    <span class="q-ml-sm text-h6">Confirmar Pagamento</span>
                </q-card-section>

                <q-card-section>
                    <div class="q-mb-md">
                        <div class="text-subtitle2 text-weight-bold">{{ selectedAppointment?.clientName }}</div>
                        <div class="text-caption text-grey-7">
                            {{ formatDate(selectedAppointment?.date || '') }} - {{ formatTime(selectedAppointment?.time
                                || '') }}
                        </div>
                        <div class="text-caption text-grey-7">
                            Serviço: {{ getServiceName(selectedAppointment?.serviceId || '') }}
                        </div>
                        <div class="text-h6 text-primary q-mt-sm">
                            Valor: {{ formatPrice(getServicePrice(selectedAppointment?.serviceId || '')) }}
                        </div>
                    </div>

                    <q-input v-model="paymentMethod" label="Método de Pagamento" outlined dense>
                        <template v-slot:prepend>
                            <q-icon name="credit_card" />
                        </template>
                    </q-input>

                    <q-input v-model="paymentNotes" label="Observações do Pagamento" outlined dense type="textarea"
                        rows="2" />
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat label="Cancelar" v-close-popup />
                    <q-btn color="primary" label="Confirmar Pagamento" icon="check" @click="processPayment" />
                </q-card-actions>
            </q-card>
        </q-dialog>

        <!-- Dialog de detalhes -->
        <q-dialog v-model="showDetailsDialog">
            <q-card :class="{ 'dark-card': $q.dark.isActive }" style="min-width: 350px; max-width: 600px;">
                <q-card-section>
                    <div class="text-h6">Detalhes do Agendamento</div>
                </q-card-section>

                <q-card-section v-if="selectedAppointment">
                    <div class="row q-col-gutter-md">
                        <div class="col-12 col-md-6">
                            <div class="text-caption text-grey-7">Cliente</div>
                            <div class="text-subtitle1 text-weight-bold">{{ selectedAppointment.clientName }}</div>
                        </div>
                        <div class="col-12 col-md-6">
                            <div class="text-caption text-grey-7">Telefone</div>
                            <div class="text-subtitle1">{{ selectedAppointment.clientPhone || 'Não informado' }}</div>
                        </div>
                        <div class="col-12 col-md-6">
                            <div class="text-caption text-grey-7">Data</div>
                            <div class="text-subtitle1">{{ formatDate(selectedAppointment.date) }}</div>
                        </div>
                        <div class="col-12 col-md-6">
                            <div class="text-caption text-grey-7">Horário</div>
                            <div class="text-subtitle1">{{ formatTime(selectedAppointment.time) }}</div>
                        </div>
                        <div class="col-12 col-md-6">
                            <div class="text-caption text-grey-7">Serviço</div>
                            <div class="text-subtitle1">{{ getServiceName(selectedAppointment.serviceId) }}</div>
                        </div>
                        <div class="col-12 col-md-6">
                            <div class="text-caption text-grey-7">Funcionário</div>
                            <div class="text-subtitle1">{{ getEmployeeName(selectedAppointment.employeeId) }}</div>
                        </div>
                        <div class="col-12">
                            <div class="text-caption text-grey-7">Valor</div>
                            <div class="text-h6 text-primary">{{
                                formatPrice(getServicePrice(selectedAppointment.serviceId)) }}
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="text-caption text-grey-7">Status do Pagamento</div>
                            <q-badge :color="getPaymentStatusColor(selectedAppointment.paymentStatus)" class="q-mt-xs"
                                rounded>
                                {{ getPaymentStatusLabel(selectedAppointment.paymentStatus) }}
                            </q-badge>
                        </div>
                        <div v-if="selectedAppointment.notes" class="col-12">
                            <div class="text-caption text-grey-7">Observações</div>
                            <div class="text-subtitle2">{{ selectedAppointment.notes }}</div>
                        </div>
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat label="Fechar" v-close-popup />
                </q-card-actions>
            </q-card>
        </q-dialog>
    </q-page>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useQuasar } from 'quasar'

// Importando os tipos
import type { agendamento } from '@/types/agendamento'
import type { funcionario } from '@/types/funcionario'
import type { ServiceForm } from '@/types/servicos'

// Composables
const $q = useQuasar()

// Extendendo o tipo agendamento para incluir campos adicionais
interface Appointment extends agendamento {
    id: number
    paymentStatus: 'pending' | 'confirmed' | 'paid'
    createdAt: string
}

// Estado
const appointments = ref<Appointment[]>([])
const funcionarios = ref<funcionario[]>([])
const services = ref<ServiceForm[]>([])
const activeTab = ref('all')
const showPaymentDialog = ref(false)
const showDetailsDialog = ref(false)
const selectedAppointment = ref<Appointment | null>(null)
const paymentMethod = ref('')
const paymentNotes = ref('')
const loading = ref(false)

// Dados de exemplo
const mockFuncionarios: funcionario[] = [
    { id: 1, name: 'João Silva', phone: '(11) 99999-9999', services: ['1', '2', '3'], active: true },
    { id: 2, name: 'Maria Santos', phone: '(11) 88888-8888', services: ['1', '4'], active: true },
    { id: 3, name: 'Pedro Oliveira', phone: '(11) 77777-7777', services: ['2', '3', '5'], active: true }
]

const mockServices: ServiceForm[] = [
    { id: 1, name: 'Corte de Cabelo', price: 50, duration: 60, color: '#4CAF50', active: true, icon: 'content_cut' },
    { id: 2, name: 'Barba', price: 30, duration: 30, color: '#FF9800', active: true, icon: 'face' },
    { id: 3, name: 'Manicure', price: 40, duration: 45, color: '#E91E63', active: true, icon: 'spa' },
    { id: 4, name: 'Pedicure', price: 45, duration: 50, color: '#9C27B0', active: true, icon: 'spa' },
    { id: 5, name: 'Massagem', price: 80, duration: 90, color: '#2196F3', active: true, icon: 'massage' }
]

const mockAppointments: Appointment[] = [
    {
        id: 1,
        serviceId: '1',
        employeeId: '1',
        date: '2026-08-29',
        time: '10:30',
        clientName: 'João Silva',
        clientPhone: '(11) 99999-9999',
        notes: 'Primeira vez',
        paymentStatus: 'pending',
        createdAt: '2026-08-20T10:00:00Z'
    },
    {
        id: 2,
        serviceId: '3',
        employeeId: '2',
        date: '2026-08-29',
        time: '14:00',
        clientName: 'Maria Santos',
        clientPhone: '(11) 88888-8888',
        notes: 'Alergia a esmaltes',
        paymentStatus: 'confirmed',
        createdAt: '2026-08-21T14:00:00Z'
    },
    {
        id: 3,
        serviceId: '5',
        employeeId: '3',
        date: '2026-08-30',
        time: '11:00',
        clientName: 'Pedro Oliveira',
        clientPhone: '(11) 77777-7777',
        notes: '',
        paymentStatus: 'paid',
        createdAt: '2026-08-22T11:00:00Z'
    },
    {
        id: 4,
        serviceId: '2',
        employeeId: '1',
        date: '2026-09-03',
        time: '15:30',
        clientName: 'Ana Costa',
        clientPhone: '(11) 66666-6666',
        notes: 'Barba completa',
        paymentStatus: 'pending',
        createdAt: '2026-08-25T15:30:00Z'
    },
    {
        id: 5,
        serviceId: '4',
        employeeId: '2',
        date: '2026-09-08',
        time: '09:00',
        clientName: 'Carlos Souza',
        clientPhone: '(11) 55555-5555',
        notes: '',
        paymentStatus: 'confirmed',
        createdAt: '2026-08-26T09:00:00Z'
    },
    {
        id: 6,
        serviceId: '1',
        employeeId: '3',
        date: '2026-09-10',
        time: '16:00',
        clientName: 'Fernanda Lima',
        clientPhone: '(11) 44444-4444',
        notes: 'Corte infantil',
        paymentStatus: 'pending',
        createdAt: '2026-08-27T16:00:00Z'
    }
]

// Computed
const filteredAppointments = computed(() => {
    let filtered = [...appointments.value]

    switch (activeTab.value) {
        case 'pending':
            filtered = filtered.filter(a => a.paymentStatus === 'pending')
            break
        case 'confirmed':
            filtered = filtered.filter(a => a.paymentStatus === 'confirmed')
            break
        case 'paid':
            filtered = filtered.filter(a => a.paymentStatus === 'paid')
            break
        default:
            // 'all' - mostra todos
            break
    }

    // Ordenar por data e hora
    return filtered.sort((a, b) => {
        const dateA = new Date(`${a.date}T${a.time}`)
        const dateB = new Date(`${b.date}T${b.time}`)
        return dateA.getTime() - dateB.getTime()
    })
})

// Methods
const getServiceName = (serviceId: string) => {
    const service = services.value.find(s => String(s.id) === serviceId)
    return service?.name || 'Serviço não encontrado'
}

const getServicePrice = (serviceId: string) => {
    const service = services.value.find(s => String(s.id) === serviceId)
    return service?.price || 0
}

const getEmployeeName = (employeeId: string) => {
    const employee = funcionarios.value.find(f => String(f.id) === employeeId)
    return employee?.name || 'Funcionário não encontrado'
}

const getStatusColor = (status: string) => {
    const colors = {
        pending: 'orange',
        confirmed: 'blue',
        paid: 'green'
    }
    return colors[status as keyof typeof colors] || 'grey'
}

const getStatusIcon = (status: string) => {
    const icons = {
        pending: 'pending',
        confirmed: 'event_available',
        paid: 'check_circle'
    }
    return icons[status as keyof typeof icons] || 'event'
}

const getPaymentStatusColor = (status: string) => {
    const colors = {
        pending: 'orange',
        confirmed: 'blue',
        paid: 'green'
    }
    return colors[status as keyof typeof colors] || 'grey'
}

const getPaymentStatusLabel = (status: string) => {
    const labels = {
        pending: 'Pagamento Pendente',
        confirmed: 'Agendado - Aguardando Pagamento',
        paid: 'Pago'
    }
    return labels[status as keyof typeof labels] || status
}

const formatDate = (date: string) => {
    if (!date) return ''
    const [year, month, day] = date.split('-')
    return new Date(Number(year), Number(month) - 1, Number(day)).toLocaleDateString('pt-BR', {
        weekday: 'long',
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    })
}

const formatTime = (time: string) => {
    if (!time) return ''
    const [hours, minutes] = time.split(':')
    return new Date(2024, 0, 1, Number(hours), Number(minutes)).toLocaleTimeString('pt-BR', {
        hour: '2-digit',
        minute: '2-digit'
    })
}

const formatPrice = (price: number) => {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(price)
}

const confirmPayment = (appointment: Appointment) => {
    selectedAppointment.value = appointment
    paymentMethod.value = ''
    paymentNotes.value = ''
    showPaymentDialog.value = true
}
const updateAppointmentPayment = (appointment: Appointment, paymentMethod: string, notes: string): Appointment => {
    const paymentInfo = `Pagamento: ${paymentMethod} - ${notes || 'Sem observações'}`

    // Retornar objeto com todas as propriedades obrigatórias
    return {
        id: appointment.id,
        serviceId: appointment.serviceId,
        employeeId: appointment.employeeId,
        date: appointment.date,
        time: appointment.time,
        clientName: appointment.clientName,
        clientPhone: appointment.clientPhone,
        notes: appointment.notes ? `${appointment.notes} | ${paymentInfo}` : paymentInfo,
        paymentStatus: 'paid' as const,
        createdAt: appointment.createdAt
    }
}
const processPayment = () => {
  if (!selectedAppointment.value) return

  // Encontrar o índice do agendamento
  const index = appointments.value.findIndex(a => a.id === selectedAppointment.value?.id)
  
  if (index !== -1) {
    // Criar uma cópia do agendamento atualizado
    const currentAppointment = appointments.value[index]
    const paymentInfo = `Pagamento: ${paymentMethod.value} - ${paymentNotes.value || 'Sem observações'}`
    
    // Atualizar o agendamento
    appointments.value[index] = {
      ...currentAppointment,
      paymentStatus: 'paid' as const,
      notes: currentAppointment.notes 
        ? `${currentAppointment.notes} | ${paymentInfo}`
        : paymentInfo
    }
  } else {
    // Caso não encontre o agendamento
    $q.notify({
      type: 'warning',
      message: 'Agendamento não encontrado',
      position: 'top',
      timeout: 3000
    })
    return
  }

  // Mostrar notificação de sucesso
  $q.notify({
    type: 'positive',
    message: 'Pagamento confirmado com sucesso!',
    caption: `Cliente: ${selectedAppointment.value.clientName} - Valor: ${formatPrice(getServicePrice(selectedAppointment.value.serviceId))}`,
    position: 'top',
    timeout: 3000
  })

  showPaymentDialog.value = false
  selectedAppointment.value = null
}


const showDetails = (appointment: Appointment) => {
    selectedAppointment.value = appointment
    showDetailsDialog.value = true
}

const loadData = async () => {
    loading.value = true
    try {
        // Simular chamada API
        await new Promise(resolve => setTimeout(resolve, 500))

        // Carregar dados
        funcionarios.value = mockFuncionarios
        services.value = mockServices
        appointments.value = mockAppointments

        $q.notify({
            type: 'positive',
            message: 'Dados atualizados com sucesso!',
            position: 'bottom',
            timeout: 2000
        })
    } catch (error) {
        $q.notify({
            type: 'negative',
            message: 'Erro ao carregar dados',
            caption: String(error),
            position: 'bottom',
            timeout: 3000
        })
    } finally {
        loading.value = false
    }
}

// Lifecycle
onMounted(() => {
    loadData()
})
</script>

<style scoped>
.appointments-list {
    max-width: 900px;
    margin: 0 auto;
}

.appointment-card {
    background: white;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    transition: all 0.3s ease;
    border-left: 4px solid transparent;
}

.appointment-card:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
}

.paid-card {
    border-left-color: #4CAF50;
}

.pending-card {
    border-left-color: #FF9800;
}

.confirmed-card {
    border-left-color: #2196F3;
}

/* Dark Mode */
.dark-mode .appointment-card {
    background: #1e1e1e;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.dark-mode .appointment-card:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.5);
}

.dark-card {
    background: #1e1e1e !important;
    color: #e0e0e0 !important;
}

.dark-mode .appointment-card.paid-card {
    border-left-color: #66BB6A;
}

.dark-mode .appointment-card.pending-card {
    border-left-color: #FFA726;
}

.dark-mode .appointment-card.confirmed-card {
    border-left-color: #42A5F5;
}

.dark-chip {
    background: #2d2d2d !important;
    color: #e0e0e0 !important;
}

/* Dark Mode para dialogs */
.dark-mode .q-dialog .q-card {
    background: #1e1e1e;
    color: #e0e0e0;
}

.dark-mode .q-dialog .q-card .text-grey-7 {
    color: #9e9e9e !important;
}

.dark-mode .q-dialog .q-card .text-primary {
    color: #42A5F5 !important;
}

/* Responsive */
@media (max-width: 600px) {
    .appointment-card {
        padding: 12px;
    }

    .text-h5 {
        font-size: 1.25rem;
    }

    .text-subtitle1 {
        font-size: 0.95rem;
    }

    .q-avatar {
        width: 40px !important;
        height: 40px !important;
    }

    .q-avatar .q-icon {
        font-size: 20px !important;
    }

    .row.q-col-gutter-sm {
        margin: 0;
    }
}

@media (min-width: 601px) and (max-width: 1024px) {
    .appointments-list {
        max-width: 100%;
        padding: 0 16px;
    }
}

/* Animações */
.appointment-card {
    animation: fadeInUp 0.3s ease;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>