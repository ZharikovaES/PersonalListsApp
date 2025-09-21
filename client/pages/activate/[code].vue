<template>
  <div class="flex justify-center items-center h-screen">
    <div class="text-center">
      <h1 v-if="isSuccess" class="text-2xl font-bold mb-4">{{ data?.message }}</h1>
      <h1 v-if="isError" class="text-2xl font-bold mb-4">{{ data?.message ?? 'Что-то пошло не так...' }}</h1>
      <h1 v-if="isPending" class="text-2xl font-bold mb-4">Загрузка...</h1>
    </div>
  </div>
</template>

<script setup lang="ts">
  const route = useRoute();
  const code = route.params.code as string;
  console.log(code);

  const { mutate: activate, data, isSuccess, isError, isPending } = useActivateEmail();
  console.log('data:', data);
  
  activate(code);
  
</script>
