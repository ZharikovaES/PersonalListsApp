import { defineNuxtPlugin } from '#app'
import Particles from "particles.vue3";

export default defineNuxtPlugin(async (nuxtApp) => {
  nuxtApp.vueApp.use(Particles)
})