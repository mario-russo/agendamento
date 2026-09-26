<template>
  <q-page class="page-appointment">
    <DesktopLayout v-if="$q.screen.gt.sm" />
    <MobileLayout v-else />
  </q-page>
</template>

<script setup lang="ts">
import DesktopLayout from '@/components/agendamento/DesktopLayout.vue';
import MobileLayout from '@/components/agendamento/MobileLayout.vue';
import { useQuasar } from 'quasar';

const $q = useQuasar();
</script>

<style scoped>
.page-appointment {
  background: #0a0a12;
  min-height: 100vh;
}
</style>