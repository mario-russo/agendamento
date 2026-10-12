<template>
  <q-card flat bordered class="stats-card-modern">
    <q-card-section>
      <div class="row items-center">
        <div class="col">
          <div class="text-subtitle2 text-grey-6 q-mb-xs">
            {{ title }}
          </div>
          <div class="text-h6 text-weight-bold text-white q-mb-none">
            {{ value }}
          </div>
          <div v-if="trend" class="row items-center q-mt-xs">
            <q-icon :name="trend === 'up' ? 'trending_up' : 'trending_down'"
              :color="trend === 'up' ? 'positive' : 'negative'" size="16px" class="q-mr-xs" />
            <span class="text-caption" :class="trend === 'up' ? 'text-positive' : 'text-negative' ">
              {{ trendValue }}
            </span>
            <span class="text-caption text-grey-6 q-ml-xs">
              vs semana passada
            </span>
          </div>
        </div>
        <div class="col-auto">
          <div :class="`icon-wrapper ${gradient}`">
            <q-icon :name="icon" size="28px" color="white" />
          </div>
        </div>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup lang="ts">
interface Props {
  title: string;
  value: string | number;
  icon: string;
  gradient?: string;
  color?: string;
  trend?: 'up' | 'down';
  trendValue?: string;
}

withDefaults(defineProps<Props>(), {
  gradient: 'gradient-blue',
  color: 'primary',
});
</script>

<style scoped>
.stats-card-modern {
  background: #14141f !important;
  border-color: rgba(255, 255, 255, 0.05) !important;
  border-radius: 16px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: default;
  overflow: hidden;
}

.stats-card-modern::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, transparent, var(--gradient-color, #6366f1), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.stats-card-modern:hover {
  transform: translateY(-6px);
  border-color: rgba(255, 255, 255, 0.1) !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
}

.stats-card-modern:hover::before {
  opacity: 1;
}

.icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.gradient-blue {
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  --gradient-color: #6366f1;
}

.gradient-green {
  background: linear-gradient(135deg, #10b981, #059669);
  --gradient-color: #10b981;
}

.gradient-teal {
  background: linear-gradient(135deg, #14b8a6, #0d9488);
  --gradient-color: #14b8a6;
}

.gradient-orange {
  background: linear-gradient(135deg, #f59e0b, #d97706);
  --gradient-color: #f59e0b;
}

.stats-card-modern:hover .icon-wrapper {
  transform: scale(1.1) rotate(-5deg);
}

.text-white {
  color: #ffffff !important;
}

.text-grey-6 {
  color: #8898aa !important;
}

.text-positive {
  color: #00d27a !important;
}

.text-negative {
  color: #f44336 !important;
}
@media (max-width: 360px) {
  .icon-wrapper{
    display: none;
  } 
  .text-value{
    font-size: 14px !important;
  }
}
</style>