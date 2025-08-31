import { register, login, logout } from '~/api/auth'
import { useMutation } from "@tanstack/vue-query"

export const useRegistration = () => {
  return useMutation({
    mutationFn: register
  });
}

export const useLogin = () => {
  return useMutation({
    mutationFn: login,
    onError: (error) => {
      console.error(error);
    }
  });
}

export const useLogout = () => {
  return useMutation({
    mutationFn: logout
  });
}