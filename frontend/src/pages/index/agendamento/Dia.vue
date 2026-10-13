<template>
    <AppointmentList :appointments="lista"></AppointmentList>
</template>
<script setup lang="ts">
import AppointmentList from '@/components/dashboard/AppointmentList.vue';
import { Appointment } from '@/types/dashboard';
import { ref } from 'vue';

const lista = ref<Appointment[]>([]);
const today = new Date().toISOString().split('T')[0];
lista.value = [
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
</script>
