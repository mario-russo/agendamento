<template>
  <div>
    <q-expansion-item v-if="hasChildren" expand-separator :icon="icon" :label="label" :caption="caption"
      :header-class="headerClass">
      <q-list padding class="q-pl-lg">
        <span @click="$emit('click', { label, caption, link, icon })">
          
          <EssentialLink v-for="child in children" :key="child.label" v-bind="child" :depth="depth + 1" />
        </span>
      </q-list>
    </q-expansion-item>

    <q-item v-else clickable tag="a" :to="link" :class="itemClass"
      @click="$emit('click', { label, caption, link, icon })">
      <q-item-section v-if="icon" avatar>
        <q-icon :name="icon" />
      </q-item-section>

      <q-item-section>
        <q-item-label>{{ label }}</q-item-label>
        <q-item-label v-if="caption" caption>{{ caption }}</q-item-label>
      </q-item-section>
    </q-item>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

export interface EssentialLinkProps {
  label: string;
  caption?: string;
  link?: string;
  icon?: string;
  children?: EssentialLinkProps[];
  depth?: number;
  indentSize?: number; // em pixels
}

const props = withDefaults(defineProps<EssentialLinkProps>(), {
  caption: '',
  link: '#',
  icon: '',
  children: () => [],
  depth: 0,
  indentSize: 16,
});

defineEmits<{
  click: [payload: EssentialLinkProps];
  clickChildren: [payload: EssentialLinkProps];
}>();

const hasChildren = computed(() => props.children && props.children.length > 0);

const itemClass = computed(() => {
  if (props.depth === 0) return '';
  const indent = props.depth * props.indentSize;
  return `q-pl-${indent}`;
});

const headerClass = computed(() => {
  if (props.depth === 0) return '';
  const indent = props.depth * props.indentSize;
  return `q-pl-${indent}`;
});
</script>