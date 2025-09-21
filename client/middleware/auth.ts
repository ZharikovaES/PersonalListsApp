import { useAuthStore } from "../stores/auth"

export default defineNuxtRouteMiddleware(() => {
  const { isAuth } = useAuthStore();
  if (!isAuth) {
    return navigateTo('/auth');
  }
})