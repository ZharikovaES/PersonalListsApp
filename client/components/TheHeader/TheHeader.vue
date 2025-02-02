<script setup lang="ts">
  interface HeaderProps {
    isAuth: boolean;
    name?: string;
  }

  const props = defineProps<HeaderProps>();
</script>

<template>
  <header class="py-5 bg-lime-600 border-b border-yellow-900 shadow-xl">
    <div class="max-w-screen-xl px-3">
      {{ name }}{{ isAuth }}
      <div class="flex justify-between items-center">
        <h1 class="font-['Caveat'] font-bold text-2xl">Очередной TODO 📝🗒️✏️</h1>
        <NuxtLink to="/" title="Авторизация" class="text-xl">
          <font-awesome icon="fa-arrow-right-to-bracket" />
        </NuxtLink>
      </div>
    </div>
  </header>
</template>
