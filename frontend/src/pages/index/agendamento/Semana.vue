<template>
  <q-page class="q-pa-sm" :class="{ 'dark-mode': $q.dark.isActive }">
    <!-- Cabeçalho do calendário -->
    <div class="row items-center justify-between q-mb-sm q-pa-sm">
      <div class="text-subtitle1 text-bold text-primary">Agenda</div>
      <div class="row items-center q-gutter-xs">
        <q-btn
          flat
          round
          dense
          icon="chevron_left"
          size="sm"
          @click="previousMonth"
        />
        <div class="text-subtitle2 text-bold" style="min-width: 120px; text-align: center;">
          {{ currentMonthName }}
        </div>
        <q-btn
          flat
          round
          dense
          icon="chevron_right"
          size="sm"
          @click="nextMonth"
        />
        <q-btn
          flat
          round
          dense
          icon="today"
          size="sm"
          @click="goToToday"
        >
          <q-tooltip>Hoje</q-tooltip>
        </q-btn>
      </div>
    </div>

    <!-- Grid do calendário -->
    <div class="calendar-grid">
      <!-- Dias da semana -->
      <div
        v-for="day in weekDays"
        :key="day"
        class="weekday-header text-center text-caption q-py-xs"
        :class="isWeekend(day) ? 'text-negative' : 'text-grey-7'"
      >
        {{ day }}
      </div>

      <!-- Dias do mês -->
      <div
        v-for="(day, index) in calendarDays"
        :key="index"
        class="calendar-day relative-position"
        :class="{
          'cursor-pointer': day && !isPastDay(day.date),
          'bg-grey-3 dark-bg-grey-8': day && isPastDay(day.date),
          'today-highlight': isToday(day?.date),
          'has-appointments': day && hasAppointments(day.date) && !isPastDay(day.date),
          'empty-day': !day
        }"
        @click="day && !isPastDay(day.date) ? openDayModal(day.date) : null"
      >
        <template v-if="day">
          <div class="day-number text-center" 
               :class="{
                 'today-text': isToday(day.date),
                 'text-grey-5': isPastDay(day.date),
                 'text-primary': !isPastDay(day.date) && !isToday(day.date),
                 'text-weight-bold': isToday(day.date)
               }">
            {{ day.dayNumber }}
          </div>
          
          <!-- Indicador de agendamentos -->
          <div v-if="hasAppointments(day.date) && !isPastDay(day.date)" class="appointment-dots">
            <q-badge 
              :color="getAppointmentStatus(day.date)" 
              rounded 
              class="appointment-badge"
              text-color="white"
            >
              {{ getAppointmentCount(day.date) }}
            </q-badge>
          </div>
        </template>
      </div>
    </div>

    <!-- Lista de agendamentos do dia -->
    <q-dialog v-model="showDayModal" position="bottom" full-width>
      <q-card class="rounded-borders-0" :class="{ 'dark-card': $q.dark.isActive }">
        <q-card-section class="row items-center q-pb-none">
          <div class="text-h6 text-bold" :class="{ 'text-white': $q.dark.isActive }">
            {{ formatDate(selectedDate) }}
          </div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-separator />

        <q-card-section class="q-pa-md">
          <div v-if="dayAppointments.length > 0" class="appointment-list">
            <div
              v-for="appointment in dayAppointments"
              :key="appointment.id"
              class="appointment-item q-pa-sm q-mb-sm"
              :class="{
                'bg-green-1': appointment.status === 'confirmed',
                'bg-orange-1': appointment.status === 'pending',
                'dark-bg-green-9': appointment.status === 'confirmed' && $q.dark.isActive,
                'dark-bg-orange-9': appointment.status === 'pending' && $q.dark.isActive
              }"
            >
              <div class="row items-center">
                <div class="col-auto q-mr-sm">
                  <q-avatar 
                    :color="appointment.status === 'confirmed' ? 'green' : 'orange'" 
                    text-color="white" 
                    icon="event" 
                    size="36px"
                  />
                </div>
                <div class="col">
                  <div class="text-subtitle2 text-weight-bold">{{ appointment.clientName }}</div>
                  <div class="text-caption text-grey-7">
                    <q-icon name="schedule" size="14px" class="q-mr-xs" />
                    {{ formatTime(appointment.time) }} - {{ appointment.service }}
                  </div>
                </div>
                <div class="col-auto">
                  <q-badge 
                    :color="appointment.status === 'confirmed' ? 'green' : 'orange'"
                    rounded
                  >
                    {{ appointment.status === 'confirmed' ? '✓ Confirmado' : '⏳ Pendente' }}
                  </q-badge>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="text-center q-pa-xl text-grey-7">
            <q-icon name="event_busy" size="56px" />
            <div class="text-h6 q-mt-sm">Nenhum agendamento</div>
            <div class="text-caption">Este dia está livre</div>
          </div>
        </q-card-section>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useQuasar } from 'quasar'

// Composables
const $q = useQuasar()

// Estado
const currentDate = ref(new Date())
const showDayModal = ref(false)
const selectedDate = ref(null)
const appointments = ref([])

// Dados de exemplo (substitua pela sua API)
const mockAppointments = [
  {
    id: 1,
    clientName: 'João Silva',
    service: 'Corte de cabelo',
    time: new Date(2026, 7, 29, 10, 30),
    status: 'confirmed'
  },
  {
    id: 2,
    clientName: 'Maria Santos',
    service: 'Manicure',
    time: new Date(2026, 7, 29, 14, 0),
    status: 'pending'
  },
  {
    id: 3,
    clientName: 'Pedro Oliveira',
    service: 'Massagem',
    time: new Date(2026, 7, 30, 11, 0),
    status: 'confirmed'
  },
  {
    id: 20,
    clientName: 'Pedro Oliveira',
    service: 'Massagem',
    time: new Date(2026, 7, 30, 11, 0),
    status: 'confirmed'
  },
  {
    id: 234,
    clientName: 'Pedro Oliveira',
    service: 'Massagem',
    time: new Date(2026, 7, 30, 11, 0),
    status: 'confirmed'
  },
  {
    id: 321,
    clientName: 'Pedro Oliveira',
    service: 'Massagem',
    time: new Date(2026, 7, 30, 11, 0),
    status: 'confirmed'
  },
  {
    id: 4,
    clientName: 'Ana Costa',
    service: 'Pedicure',
    time: new Date(2026, 8, 3, 15, 30),
    status: 'confirmed'
  },
  {
    id: 5,
    clientName: 'Carlos Souza',
    service: 'Barba',
    time: new Date(2026, 8, 8, 9, 0),
    status: 'pending'
  },
  {
    id: 6,
    clientName: 'Carlos Souza',
    service: 'Barba',
    time: new Date(2026, 8, 8, 9, 0),
    status: 'pending'
  },
  
]

// Constantes
const weekDays = ['D', 'S', 'T', 'Q', 'Q', 'S', 'S']

// Computed
const currentMonthName = computed(() => {
  const monthNames = [
    'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
    'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
  ]
  return monthNames[currentDate.value.getMonth()]
})

const currentYear = computed(() => currentDate.value.getFullYear())

const calendarDays = computed(() => {
  const year = currentDate.value.getFullYear()
  const month = currentDate.value.getMonth()
  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)
  const daysInMonth = lastDay.getDate()
  const startingDay = firstDay.getDay()

  const days = []
  
  for (let i = 0; i < startingDay; i++) {
    days.push(null)
  }

  for (let day = 1; day <= daysInMonth; day++) {
    days.push({
      dayNumber: day,
      date: new Date(year, month, day)
    })
  }

  return days
})

const dayAppointments = computed(() => {
  if (!selectedDate.value) return []
  
  return appointments.value.filter(appointment => {
    const appointmentDate = appointment.time
    return appointmentDate.getFullYear() === selectedDate.value.getFullYear() &&
           appointmentDate.getMonth() === selectedDate.value.getMonth() &&
           appointmentDate.getDate() === selectedDate.value.getDate() &&
           appointmentDate >= new Date()
  })
})

// Methods
const previousMonth = () => {
  currentDate.value = new Date(
    currentDate.value.getFullYear(),
    currentDate.value.getMonth() - 1,
    1
  )
}

const nextMonth = () => {
  currentDate.value = new Date(
    currentDate.value.getFullYear(),
    currentDate.value.getMonth() + 1,
    1
  )
}

const goToToday = () => {
  currentDate.value = new Date()
}

const isPastDay = (dayDate) => {
  if (!dayDate) return true
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return dayDate < today
}

const isToday = (dayDate) => {
  if (!dayDate) return false
  const today = new Date()
  return dayDate.getFullYear() === today.getFullYear() &&
         dayDate.getMonth() === today.getMonth() &&
         dayDate.getDate() === today.getDate()
}

const hasAppointments = (dayDate) => {
  if (!dayDate || isPastDay(dayDate)) return false
  
  return appointments.value.some(appointment => {
    const appointmentDate = appointment.time
    return appointmentDate.getFullYear() === dayDate.getFullYear() &&
           appointmentDate.getMonth() === dayDate.getMonth() &&
           appointmentDate.getDate() === dayDate.getDate() &&
           appointmentDate >= new Date()
  })
}

const getAppointmentCount = (dayDate) => {
  if (!dayDate) return 0
  
  return appointments.value.filter(appointment => {
    const appointmentDate = appointment.time
    return appointmentDate.getFullYear() === dayDate.getFullYear() &&
           appointmentDate.getMonth() === dayDate.getMonth() &&
           appointmentDate.getDate() === dayDate.getDate() &&
           appointmentDate >= new Date()
  }).length
}

const getAppointmentStatus = (dayDate) => {
  if (!dayDate) return 'grey'
  
  const dayAppointments = appointments.value.filter(appointment => {
    const appointmentDate = appointment.time
    return appointmentDate.getFullYear() === dayDate.getFullYear() &&
           appointmentDate.getMonth() === dayDate.getMonth() &&
           appointmentDate.getDate() === dayDate.getDate() &&
           appointmentDate >= new Date()
  })
  
  if (dayAppointments.some(a => a.status === 'pending')) {
    return 'orange'
  }
  return 'green'
}

const openDayModal = (dayDate) => {
  selectedDate.value = dayDate
  showDayModal.value = true
}

const formatDate = (date) => {
  if (!date) return ''
  return date.toLocaleDateString('pt-BR', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  })
}

const formatTime = (time) => {
  if (!time) return ''
  return time.toLocaleTimeString('pt-BR', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

const isWeekend = (day) => {
  return day === 'S' || day === 'D'
}

// API Functions
const loadAppointments = async () => {
  try {
    // Substitua pela chamada real da API
    // const response = await api.get('/appointments')
    // appointments.value = response.data
    
    // Usando dados mockados
    appointments.value = mockAppointments
  } catch (error) {
    console.error('Erro ao carregar agendamentos:', error)
  }
}

// Lifecycle
onMounted(() => {
  loadAppointments()
})
</script>

<style scoped>
.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 2px;
  border-radius: 12px;
  overflow: hidden;
}

.weekday-header {
  background-color: rgba(0, 0, 0, 0.03);
  font-weight: 600;
  padding: 8px 0;
  font-size: 12px;
}

.calendar-day {
  aspect-ratio: 1;
  background-color: var(--q-color-background, white);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  border-radius: 8px;
  position: relative;
  min-height: 48px;
}

.empty-day {
  background-color: transparent !important;
}

.calendar-day:not(.empty-day):not(.bg-grey-3):hover {
  background-color: rgba(25, 118, 210, 0.08);
  transform: scale(1.02);
}

.calendar-day.bg-grey-3 {
  opacity: 0.6;
}

.today-highlight {
  background-color: var(--q-primary) !important;
  color: white !important;
}

.today-highlight .day-number {
  color: white !important;
}

.today-text {
  color: white !important;
}

.has-appointments:not(.today-highlight):not(.bg-grey-3) {
  background-color: rgba(76, 175, 80, 0.08);
}

.day-number {
  font-size: 15px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.appointment-dots {
  display: flex;
  gap: 3px;
  margin-top: 2px;
}

.appointment-badge {
  font-size: 10px;
  padding: 2px 6px;
  min-width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Dark Mode */
.dark-mode .weekday-header {
  background-color: rgba(255, 255, 255, 0.05);
}

.dark-mode .calendar-day:not(.empty-day):not(.bg-grey-3):not(.today-highlight) {
  background-color: #1e1e1e;
}

.dark-mode .calendar-day:not(.empty-day):not(.bg-grey-3):hover {
  background-color: rgba(25, 118, 210, 0.15);
}

.dark-mode .calendar-day.bg-grey-3 {
  background-color: #2d2d2d;
}

.dark-mode .has-appointments:not(.today-highlight):not(.bg-grey-3) {
  background-color: rgba(76, 175, 80, 0.15);
}

.dark-bg-grey-8 {
  background-color: #2d2d2d !important;
}

.dark-card {
  background-color: #1e1e1e !important;
}

.dark-bg-green-9 {
  background-color: #1b5e20 !important;
}

.dark-bg-orange-9 {
  background-color: #e65100 !important;
}

/* Lista de agendamentos */
.appointment-list {
  max-height: 60vh;
  overflow-y: auto;
}

.appointment-item {
  border-radius: 8px;
  transition: all 0.2s ease;
}

.appointment-item:active {
  transform: scale(0.98);
}

/* Responsive */
@media (max-width: 400px) {
  .calendar-day {
    min-height: 40px;
  }
  
  .day-number {
    font-size: 13px;
  }
  
  .appointment-badge {
    font-size: 8px;
    padding: 1px 4px;
    min-width: 16px;
    height: 16px;
  }
}
</style>