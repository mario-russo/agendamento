<template>
    <q-page class="">
        <DashboardWidget />
    </q-page>
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar';
import DashboardWidget from '@/components/dashboard/DashboardWidget.vue';

const $q = useQuasar();

const openCreateDialog = () => {
    // Implement dialog for creating new appointment
    $q.notify({
        type: 'info',
        message: 'Funcionalidade em desenvolvimento',
        position: 'top',
    });
};
</script>

<style scoped>
.q-page {
    background: #f5f7fa;
}
</style>