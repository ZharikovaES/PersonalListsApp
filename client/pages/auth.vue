<template>
  <div class='my-3 justify-center'>
    <div class="w-96 h-[540px] mx-auto">
      <div class="flex mb-6">
        <button
          v-for="tab in tabs"
          :key="tab"
          type="button"
          :class="['w-1/2 px-5 py-2 border-solid border-2 rounded-3xl', activeTab === tab ? 'border-green-800' : 'border-transparent']"
          @click="activeTab = tab"
        >{{ tab === Tabs.LOGIN ? 'Войти' : 'Регистрация' }}</button>
      </div>
      <TheLogin v-if="activeTab === Tabs.LOGIN" />
      <TheRegistration v-if="activeTab === Tabs.REGISTRATION" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  enum Tabs {
    LOGIN = 'login',
    REGISTRATION = 'registration'
  }
  const tabs = [Tabs.LOGIN, Tabs.REGISTRATION];
  const activeTab = ref(tabs[0]);
</script>