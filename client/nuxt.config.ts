// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: true },
  css: ['~/assets/css/main.css'],
  modules: [
    '@nuxt/image',
    '@pinia/nuxt',
    '@nuxtjs/tailwindcss',
    '@vesp/nuxt-fontawesome',
    [
      '@nuxtjs/google-fonts',
      {
        families: {
          Roboto: [100, 300, 400, 700, 900],
          Caveat: [400, 700]
        },
      },
    ],
    '@vee-validate/nuxt',
  ],
  veeValidate: {
    autoImports: true,
    componentNames: {
      Form: 'VeeForm',
      Field: 'VeeField',
      FieldArray: 'VeeFieldArray',
      ErrorMessage: 'VeeErrorMessage',
    },
  },
  fontawesome: {
    icons: {
      solid: ['user', 'arrow-right-to-bracket', 'spinner'],
    }
  },
  runtimeConfig: {
    public: {
      apiBaseUrl: process.env.API_URL || 'http://localhost:8080/api',
    }
  }
});