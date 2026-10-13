<template>
  <q-layout view="lHh Lpr lFf">
    <Menu></Menu>
    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>
<script setup lang="ts">
import Menu from '@/components/Menu.vue';


</script>
