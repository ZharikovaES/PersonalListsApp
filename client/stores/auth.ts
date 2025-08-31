import { defineStore } from 'pinia';

interface AuthState {
  isAuth: boolean;
  accessToken?: string | null;
  refreshToken?: string | null;
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    isAuth: false,
    accessToken: null,
    refreshToken: null,
  }),
  getters: { },
  actions: {
    setIsAuth(isAuth: boolean) {
      this.isAuth = isAuth;
    },
    setTokens(accessToken: string, refreshToken: string) {
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
    },
    clearTokens() {
      this.accessToken = null;
      this.refreshToken = null;
      this.isAuth = false;
    }
  }
})