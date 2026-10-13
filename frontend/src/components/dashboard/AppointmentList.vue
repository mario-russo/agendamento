<template>
  <q-card flat bordered class="list-card-modern">
    <!-- Header -->
    <q-card-section class="row items-center q-pa-md">
      <div class="col">
        <div class="text-subtitle1 text-weight-bold text-white">
          Agendamentos do Dia
          <q-badge color="primary" class="q-ml-xs" rounded>
            {{ appointments.length }}
          </q-badge>
        </div>
        <div class="text-caption text-grey-6 q-mt-xs">
          Lista de compromissos de hoje
        </div>
      </div>
      <div class="col-auto">
        <q-btn
          flat
          round
          dense
          icon="refresh"
          color="grey-6"
          :loading="loading"
          @click="$emit('refresh')"
          class="refresh-btn"
        >
          <q-tooltip class="bg-grey-9">Atualizar lista</q-tooltip>
        </q-btn>
      </div>
    </q-card-section>

    <q-separator dark />

    <!-- Loading State -->
    <div v-if="loading" class="text-center q-py-xl">
      <q-spinner color="primary" size="40px" class="q-mb-sm" />
      <div class="text-caption text-grey-6">Carregando agendamentos...</div>
    </div>

    <!-- Lista de Agendamentos -->
    <div v-else>
      <q-list class="q-pa-sm" separator>
        <q-item
          v-for="appointment in appointments"
          :key="appointment.id"
          class="appointment-item q-my-xs q-pa-sm"
          clickable
          @click="showAppointmentDetails(appointment)"
        >
          <q-item-section top avatar class="q-pr-sm">
            <q-avatar
              :class="getAvatarBg(appointment.clientName)"
              text-color="white"
              class="avatar-gradient text-weight-bold"
              size="42px"
            >
              {{ getInitials(appointment.clientName) }}
            </q-avatar>
          </q-item-section>

          <q-item-section>
            <q-item-label class="text-subtitle2 text-weight-bold text-white q-mb-xs">
              {{ appointment.clientName }}
            </q-item-label>
            
            <div class="column q-gutter-y-xs">
              <span class="text-caption text-grey-6 flex items-center">
                <q-icon name="schedule" size="13px" class="q-mr-xs text-grey-7" />
                {{ appointment.time }}
                <span class="q-mx-sm text-grey-8">•</span>
                <q-icon name="content_cut" size="13px" class="q-mr-xs text-grey-7" />
                {{ appointment.service }}
              </span>
              <span class="text-caption text-grey-6 flex items-center">
                <q-icon name="phone" size="13px" class="q-mr-xs text-grey-7" />
                {{ formatPhone(appointment.clientPhone) }}
              </span>
            </div>
          </q-item-section>

          <q-item-section side top class="column items-end justify-between">
            <div class="text-subtitle2 text-weight-bold text-primary">
              R$ {{ appointment.price.toFixed(2) }}
            </div>
            <div
              :class="`status-pill pill-${appointment.status} q-mt-sm text-caption text-weight-bold text-center`"
            >
              {{ getStatusLabel(appointment.status) }}
            </div>
          </q-item-section>
        </q-item>
      </q-list>

      <!-- Empty State -->
      <div v-if="appointments.length === 0" class="text-center q-py-xl q-px-md">
        <q-icon name="event_available" size="56px" class="text-grey-7 q-mb-sm" />
        <div class="text-h6 text-weight-medium text-grey-6">Tudo limpo por aqui</div>
        <div class="text-caption text-grey-6 q-mt-xs q-mb-md">
          Nenhum agendamento encontrado para hoje
        </div>
        <q-btn
          color="primary"
          label="Criar agendamento"
          icon="add"
          unelevated
          rounded
          class="btn-create"
          @click="$emit('refresh')"
        />
      </div>
    </div>
  </q-card>
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar';
import type { Appointment } from '../../types/dashboard';

interface Props {
  appointments: Appointment[];
  loading?: boolean;
}

interface Emits {
  (e: 'refresh'): void;
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
});

const emit = defineEmits<Emits>();

const $q = useQuasar();

const getInitials = (name: string): string => {
  return name
    .split(' ')
    .map((w) => w[0])
    .join('')
    .toUpperCase()
    .slice(0, 2);
};

const getAvatarBg = (name: string): string => {
  const hash = name
    .split('')
    .reduce((acc, char) => acc + char.charCodeAt(0), 0);
  const colors = [
    'gradient-purple',
    'gradient-blue',
    'gradient-green',
    'gradient-orange',
    'gradient-pink',
  ];
  return colors[hash % colors.length] ||"";
};

const formatPhone = (phone: string): string => {
  const cleaned = phone.replace(/\D/g, '');
  if (cleaned.length === 11) {
    return `(${cleaned.slice(0, 2)}) ${cleaned.slice(2, 7)}-${cleaned.slice(7)}`;
  }
  if (cleaned.length === 10) {
    return `(${cleaned.slice(0, 2)}) ${cleaned.slice(2, 6)}-${cleaned.slice(6)}`;
  }
  return phone;
};

const getStatusLabel = (status: Appointment['status']): string => {
  const labels = {
    pending: 'Pendente',
    confirmed: 'Confirmado',
    completed: 'Concluído',
    cancelled: 'Cancelado',
  };
  return labels[status] || status;
};

const showAppointmentDetails = (appointment: Appointment) => {
  $q.dialog({
    title: appointment.clientName,
    message: `
      Serviço: ${appointment.service}
      Horário: ${appointment.time}
      Telefone: ${formatPhone(appointment.clientPhone)}
      Valor: R$ ${appointment.price.toFixed(2)}
      Status: ${getStatusLabel(appointment.status)}
    `,
    persistent: false,
    ok: {
      label: 'Fechar',
      color: 'primary',
      unelevated: true,
    },
  });
};
</script>

<style scoped>
.list-card-modern {
  background: #14141f !important;
  border-color: rgba(255, 255, 255, 0.05) !important;
  border-radius: 16px !important;
  overflow: hidden;
}

.appointment-item {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.04);
  border-radius: 12px;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  margin-bottom: 4px;
}

.appointment-item:active {
  background: rgba(255, 255, 255, 0.05);
  transform: scale(0.98);
  border-color: rgba(255, 255, 255, 0.08);
}

.avatar-gradient {
  font-weight: 700;
  font-size: 14px;
}

.gradient-purple {
  background: linear-gradient(135deg, #8b5cf6, #7c3aed);
}

.gradient-blue {
  background: linear-gradient(135deg, #3b82f6, #2563eb);
}

.gradient-green {
  background: linear-gradient(135deg, #10b981, #059669);
}

.gradient-orange {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.gradient-pink {
  background: linear-gradient(135deg, #ec4899, #db2777);
}

.status-pill {
  padding: 3px 10px;
  border-radius: 50px;
  display: inline-block;
  min-width: 70px;
  font-size: 10px;
  letter-spacing: 0.3px;
  text-transform: uppercase;
}

.pill-confirmed {
  background: rgba(59, 130, 246, 0.15);
  color: #60a5fa;
  border: 1px solid rgba(59, 130, 246, 0.2);
}

.pill-pending {
  background: rgba(245, 158, 11, 0.15);
  color: #fbbf24;
  border: 1px solid rgba(245, 158, 11, 0.2);
}

.pill-completed {
  background: rgba(16, 185, 129, 0.15);
  color: #34d399;
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.pill-cancelled {
  background: rgba(239, 68, 68, 0.15);
  color: #f87171;
  border: 1px solid rgba(239, 68, 68, 0.2);
}

.btn-create {
  background: linear-gradient(135deg, #6366f1, #3b82f6) !important;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.3);
}

.text-white {
  color: #ffffff !important;
}

.text-grey-6 {
  color: #8898aa !important;
}

.text-primary {
  color: #6366f1 !important;
}

.refresh-btn {
  transition: all 0.3s ease;
}

.refresh-btn:active {
  transform: rotate(180deg);
}

/* Ajustes para telas muito pequenas */
@media (max-width: 360px) {
  .appointment-item {
    padding: 8px;
  }
  
  .avatar-gradient {
    width: 36px !important;
    height: 36px !important;
  }
}
</style>