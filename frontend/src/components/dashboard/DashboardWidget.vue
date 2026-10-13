<template>
  <q-page class="dashboard-page bg-dark">
    <div class="q-pa-md">
      <!-- Header -->
      <div class="row items-center justify-between q-mb-lg">
        <div>
          <div class="text-h5 text-weight-bold text-white">Dashboard</div>
          <div class="text-caption text-grey-5">Agendamentos do dia</div>
        </div>
        <q-btn round color="primary" icon="add" size="md" class="btn-add" @click="openCreateDialog">
          <q-tooltip>Novo Agendamento</q-tooltip>
        </q-btn>
      </div>

      <!-- Stats Grid -->
      <div class="row q-col-gutter-md q-mb-lg">
        <div class="col-6">
          <StatsCard title="Total Hoje" :value="stats.totalAppointments" icon="calendar_today" gradient="gradient-blue"
            trend="up" trendValue="12%" />
        </div>
        <div class="col-6">
          <StatsCard title="Faturamento" :value="`R$ ${stats.totalRevenue.toFixed(2)}`" icon="attach_money"
            gradient="gradient-green" trend="up" trendValue="8%" />
        </div>
        <div class="col-6">
          <StatsCard title="Concluídos" :value="stats.completedAppointments" icon="check_circle"
            gradient="gradient-teal" trend="up" trendValue="5%" />
        </div>
        <div class="col-6">
          <StatsCard title="Pendentes" :value="stats.pendingAppointments" icon="pending" gradient="gradient-orange"
            trend="down" trendValue="3%" />
        </div>
      </div>

      <!-- Lista de Agendamentos -->
      <AppointmentList :appointments="todayAppointments" :loading="loading" @refresh="fetchAppointments" />
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useQuasar } from 'quasar';
import StatsCard from './StatsCard.vue';
import AppointmentList from './AppointmentList.vue';
import type { Appointment, DashboardStats } from '../../types/dashboard';

const $q = useQuasar();
const appointments = ref<Appointment[]>([]);
const loading = ref(false);

// Ativar dark mode
$q.dark.set(true);

const todayAppointments = computed(() => {
  const today = new Date().toISOString().split('T')[0];
  return appointments.value.filter(apt => apt.date === today);
});

const stats = computed<DashboardStats>(() => {
  const total = todayAppointments.value;
  const completed = total.filter(apt => apt.status === 'completed');
  const totalRevenue = total.reduce((sum, apt) => sum + apt.price, 0);

  return {
    totalAppointments: total.length,
    totalRevenue,
    completedAppointments: completed.length,
    pendingAppointments: total.length - completed.length,
  };
});

const openCreateDialog = () => {
  $q.dialog({
    title: 'Novo Agendamento',
    message: 'Deseja criar um novo agendamento?',
    persistent: true,
    ok: {
      label: 'Criar',
      color: 'primary',
      unelevated: true,
    },
    cancel: {
      label: 'Cancelar',
      flat: true,
      color: 'grey',
    },
  }).onOk(() => {
    $q.notify({
      type: 'positive',
      message: 'Funcionalidade em desenvolvimento',
      position: 'top',
      timeout: 2000,
    });
  });
};

const fetchAppointments = async () => {
  loading.value = true;
  try {
    await new Promise(resolve => setTimeout(resolve, 800));
    const today = new Date().toISOString().split('T')[0];

    appointments.value = [
      {
        id: '1',
        clientName: 'João Silva',
        clientPhone: '11999999999',
        service: 'Corte de Cabelo',
        price: 50.00,
        date: today || "",
        time: '09:00',
        status: 'confirmed',
      },
      {
        id: '2',
        clientName: 'Maria Santos',
        clientPhone: '11888888888',
        service: 'Manicure',
        price: 35.00,
        date: today || '',
        time: '10:30',
        status: 'pending',
      },
      {
        id: '3',
        clientName: 'Pedro Oliveira',
        clientPhone: '11777777777',
        service: 'Barba',
        price: 30.00,
        date: today || '',
        time: '14:00',
        status: 'completed',
      },
    ];
  } catch (error) {
    console.error(error);
    $q.notify({
      type: 'negative',
      message: 'Erro ao carregar agendamentos',
      position: 'top',
      timeout: 3000,
    });
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchAppointments();
});
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
}

.btn-add {
  background: linear-gradient(135deg, #6366f1, #3b82f6) !important;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
  transition: all 0.3s ease;
}

.btn-add:active {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(99, 102, 241, 0.5);
}

/* Ajustes para tablets */
@media (min-width: 600px) {
  .q-pa-md {
    padding: 24px;
  }

  .text-h5 {
    font-size: 1.8rem;
  }
}

/* Ajustes para telas muito pequenas */
@media (max-width: 360px) {
  .q-pa-md {
    padding: 12px;
  }

  .q-col-gutter-md {
    margin-left: -8px;
    margin-right: -8px;
  }

  .q-col-gutter-md>* {
    padding-left: 8px;
    padding-right: 8px;
  }
}
</style>