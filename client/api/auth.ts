import { useNuxtApp } from '#app';

interface TokensResponse {
  accessToken: string;
  refreshToken: string;
}

interface LoginForm {
  login: string;
  password: string;
  remember: boolean;
}

interface MailResponse {
  message: string;
}

export const register = async (form: {
  username: string;
  email: string;
  password: string;
}) => {
  const { $api } = useNuxtApp();
  const res: TokensResponse = await $api('/auth/registration', {
    method: 'POST',
    body: form
  });

  // const auth = useAuthStore();
  // auth.setTokens(res.accessToken, res.refreshToken);
  return res;
}

export const login = async (form: LoginForm) => {
  const { $api } = useNuxtApp();
  const res: TokensResponse = await $api('/auth/login', {
    method: 'POST',
    body: form
  });
  
  const auth = useAuthStore();
  auth.setTokens(res.accessToken, res.refreshToken);
  auth.setIsAuth(true);
  return res;
}

export const logout = async () => {
  const auth = useAuthStore();
  const { $api } = useNuxtApp();

  const res = await $api('/auth/logout', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${auth.accessToken}`
    },
    body: {
      refreshToken: auth.refreshToken
    }
  });
  auth.clearTokens();
  return res;
}

export const refreshAccessToken = async () => {
  const auth = useAuthStore();
  const { $api } = useNuxtApp();

  const res: TokensResponse = await $api('/auth/refresh', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${auth.accessToken}`
    },
    body: {
      refreshToken: auth.refreshToken
    }
  });
  auth.setTokens(res.accessToken, res.refreshToken);
  return res;
}

export const activateEmail = async (code: string) => {
  const { $api } = useNuxtApp();
  const res: MailResponse = await $api('/auth/activate', {
    method: 'POST',
    body: {
      code
    }
  });
  return res;
}