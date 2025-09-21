<script setup lang="ts">
  interface HeaderProps {
    isAuth: boolean;
    name?: string;
  }
  
  defineProps<HeaderProps>();

  const { mutate: logout } = useLogout();
  const handleLogoutButton = () => {
    logout(undefined, {
      onSuccess: () => {
        navigateTo('/auth')
      }
    });
  }

  const mounted = ref(false);

  onMounted(() => {
    mounted.value = true;
  });
</script>

<template>
<!-- <Transition
    enter-active-class="transition duration-1000"
    enter-from-class="-translate-y-[100%] opacity-0"
    mode="out-in"
    > -->
    <header :class="['py-5 bg-lime-600 border-b border-yellow-900 shadow-xl text-stone-200 transition duration-1000', mounted ? 'opacity-100 translate-y-0' : 'opacity-0 -translate-y-full']">
      <TheContainer>
        <div class="flex justify-between items-center">
          <h1 class="font-['Caveat'] font-bold text-2xl text-stone-200">Очередной TODO 📝🗒️✏️</h1>
          <button v-if="isAuth" type="button" title="Выход" class="text-xl text-stone-200" @click="handleLogoutButton">
            <font-awesome icon="fa-arrow-right-to-bracket" />
          </button>
          <NuxtLink v-else to="/home" title="Авторизация" class="text-xl text-stone-200">
            <font-awesome icon="fa-user" />
          </NuxtLink>
        </div>
      </TheContainer>
    </header>
  <!-- </Transition> -->
</template>
