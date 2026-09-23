<template>
  <q-header elevated class="bg-cyan-8">
    <q-toolbar>
      <q-btn flat dense round icon="menu" aria-label="Menu" @click="toggleLeftDrawer" />
      <q-toolbar-title> Quasar App </q-toolbar-title>
      <div>Quasar v{{ $q.version }}</div>
    </q-toolbar>
  </q-header>

  <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
    <q-list>
      <q-item-label header> Menu de Links </q-item-label>
      <EssentialLink v-for="link in linksList" :key="link.label" v-bind="link" @click="leftDrawerOpen = false"
        @click-children="leftDrawerOpen = false" />
    </q-list>
  </q-drawer>
</template>

<script setup lang="ts">
import { ref } from "vue";
import EssentialLink, {
  type EssentialLinkProps
} from "@/components/EssentialLink.vue";
import { useQuasar } from "quasar";

const $q = useQuasar();

$q.dark.set(true);

const linksList: EssentialLinkProps[] = [
  {
    label: "Novo Agendamento",
    caption: "Marcar um novo serviço",
    icon: "add_task",
    link: "/agendamento",
  },
  {
    label: "Nova venda",
    caption: "Pagemento do agendamento",
    icon: "add_task",
    link: "/venda",
  },

  {
    label: "Agendamentos",
    caption: "Visão geral do calendário",
    icon: "calendar_month",
    link: "/agendamento",
    children: [

      {
        label: "Diário",
        caption: "Compromissos de hoje",
        icon: "view_day",
        link: "/agendamento/dia",
      },
      {
        label: "Semanal",
        caption: "Grade dos próximos dias",
        icon: "view_week",
        link: "/agendamento/semana",
      },

    ]
  },

  {
    label: "Cadastros",
    caption: "Gerenciar recursos do sistema",
    icon: "app_registration",
    children: [
      {
        label: "Funcionários",
        caption: "Equipe e permissões",
        icon: "badge",
        link: "/funcionario",
      },
      {
        label: "Serviços",
        caption: "Catálogo de valores e tempo",
        icon: "design_services",
        link: "/servicos",
      }
    ]
  },
  {
    label: "DashBoard",
    caption: "Gerenciar recursos do sistema",
    icon: "app_registration",
    link: "/dashboard",
  },

];

const leftDrawerOpen = ref(false);

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value;
}
</script>