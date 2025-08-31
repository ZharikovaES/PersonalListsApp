import { ApiError } from "~/api/ApiError";

export default defineNuxtPlugin(() => {
  const storeAuth = useAuthStore();
  const config = useRuntimeConfig();

  const api = $fetch.create({
    baseURL: config.public.apiBaseUrl,
    onRequest({ options }) {
      if (storeAuth.accessToken) {
        options.headers.set('Authorization', `Bearer ${storeAuth.accessToken}`)
      }
    },
    async onResponseError({ response }) {
      const router = useRouter();
      if (response.status === 401) {
        const currentPath = router.currentRoute.value.path;
        storeAuth.clearTokens();
        
        if (currentPath !== '/login') {
          await navigateTo('/login');
        } else {
          const errorApi = new ApiError('Пользователь не найден. Зарегистрируйтесь', response.status, response._data);
          return Promise.reject(errorApi);
        }
      }
    }
  });

  return {
    provide: {
      api
    }
  }
});