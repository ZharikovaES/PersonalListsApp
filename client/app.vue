<script setup lang="ts">
import { useAuthStore } from './stores/auth';


// import Particles from '@tsparticles/vue3';
// const options = {
//   background: {
//     color: {
//       value: "#000",
//     },
//   },
//   particles: {
//     color: {
//       value: "#ffffff",
//     },
//     links: {
//       enable: true,
//       color: "#ffffff",
//       distance: 150,
//     },
//     move: {
//       enable: true,
//       speed: 2,
//     },
//     number: {
//       value: 50,
//     },
//     size: {
//       value: 3,
//     },
//   },
// };

const { isAuth } = useAuthStore();

</script>

<template>
  <NuxtLayout>
    <div class="flex flex-col min-h-dvh">
      <TheHeader name="test" :is-auth="isAuth"/>
      <main class="grow flex flex-col">
        <!--<ClientOnly>
           <Particles
      id="tsparticles"
      :options="{
        
                                           background: {
                                             color: {
                                               value: '#0d47a1'
                                             }
                                           },
                                           fpsLimit: 60,
                                           interactivity: {
                                             detectsOn: 'window',
                                             events: {
                                               onClick: {
                                                 enable: true,
                                                 mode: 'push'
                                               },
                                               onHover: {
                                                 enable: true,
                                                 mode: 'repulse'
                                               },
                                               resize: true
                                             },
                                             modes: {
                                               bubble: {
                                                 distance: 400,
                                                 duration: 2,
                                                 opacity: 0.8,
                                                 size: 40,
                                                 speed: 3
                                               },
                                               push: {
                                                 quantity: 4
                                               },
                                               repulse: {
                                                 distance: 200,
                                                 duration: 0.4
                                               }
                                             }
                                           },
                                           particles: {
                                             color: {
                                               value: '#ffffff'
                                             },
                                             links: {
                                               color: '#ffffff',
                                               distance: 150,
                                               enable: true,
                                               opacity: 0.5,
                                               width: 1
                                             },
                                             collisions: {
                                               enable: true
                                             },
                                             move: {
                                               direction: 'none',
                                               enable: true,
                                               outMode: 'bounce',
                                               random: false,
                                               speed: 6,
                                               straight: false
                                             },
                                             number: {
                                               density: {
                                                 enable: true,
                                                 value_area: 800
                                               },
                                               value: 80
                                             },
                                             opacity: {
                                               value: 0.5
                                             },
                                             shape: {
                                               type: 'circle'
                                             },
                                             size: {
                                               random: true,
                                               value: 5
                                             }
                                           },
                                           detectRetina: true
                                         }"
    />
  </ClientOnly> -->
        <TheContainer class="justify-self-center">
          <NuxtPage />
        </TheContainer>
      </main>
      <TheFooter />
    </div>
  </NuxtLayout>
</template>

<style scoped>
/* img {
  width: 200px;
}
h1 {
  font-family: Arial, Helvetica, sans-serif;
}

#tsparticles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000000;
  z-index: -10;
} */
</style>