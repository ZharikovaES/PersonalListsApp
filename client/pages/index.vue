<script setup lang='ts'>
  import { ref } from 'vue';

  const features = ref(['Добавлять, редактировать и удалять списки',
    'Добавлять, редактировать и удалять заметки',
    'Добавлять изображения к заметкам',
    'Добавлять теги к спискам и заметкам',
    'Добавлять теги к спискам и заметкам',
    'Фильтровать записи по тегам',
    'Сортировать записи по дате'
  ]);
</script>
<template>
  <div class='my-3'>
    <p>
      Приветствую!
    </p>
    <p>
      Перед Вами главная страница сервиса для ведения личных списков и заметок.
    </p>
    <p>
      Как Вы можете использовать это приложение?
    </p>
    <ul class="list-disc marker:text-gray-600 pl-6">
      <li v-for="feature in features" :key="feature">
        {{ feature }}
      </li>
    </ul>
  
    <span>
        Ссылка на <NuxtLink href="https://github.com/ZharikovaES/PersonalListsApp.git" class='text-lime-700'>Github-репозиторий</NuxtLink>
    </span>
  </div>
</template>
