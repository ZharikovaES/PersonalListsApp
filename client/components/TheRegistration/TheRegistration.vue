<template>
  <form :initial-values="initialValues" @submit="onSubmit">
    <fieldset>
      <label class="mb-1" for="username">Имя пользователя:</label>
      <Field
        id="username"
        name="username"
        as="input"
        class="w-full border-2 border-solid border-green-800 rounded-3xl px-5 py-2"
      />
      <div class="min-h-6">
        <ErrorMessage
          name="username"
          class="text-red-500 text-sm"
        />
      </div>
    </fieldset>
    <fieldset>
      <label class="mb-1" for="email">Электронная почта:</label>
      <Field
        id="email"
        name="email"
        as="input"
        type="email"
        class="w-full border-2 border-solid border-green-800 rounded-3xl px-5 py-2"
      />
      <div class="min-h-6">
        <ErrorMessage
          name="email"
          class="text-red-500 text-sm"
        />
      </div>
    </fieldset>
    <fieldset>
      <label class="mb-1" for="password">Пароль:</label>
      <Field
        id="password"
        name="password"
        as="input"
        type="password"
        class="w-full border-2 border-solid border-green-800 rounded-3xl px-5 py-2"
      />
      <div class="min-h-6">
        <ErrorMessage
          name="password"
          class="text-red-500 text-sm"
        />
      </div>
    </fieldset>
    <fieldset>
      <label class="mb-1" for="confirmPassword">Повторите пароль:</label>
      <Field
        id="confirmPassword"
        name="confirmPassword"
        as="input"
        type="password"
        class="w-full border-2 border-solid border-green-800 rounded-3xl px-5 py-2"
      />
      <div class="min-h-6">
        <ErrorMessage
          name="confirmPassword"
          class="text-red-500 text-sm"
        />
      </div>
    </fieldset>
    <div>
      <button
        type="submit"
        :disabled="isPending"
        class="w-full mt-2 border-2 border-solid border-green-800 rounded-3xl px-5 py-2">
        <font-awesome v-if="isPending" icon="fa-spinner" spin/>
        <span v-else >Зарегистрироваться</span>
      </button>
    </div>
  </form>
  <div class="min-h-12 mt-2">
    <p v-if="isError && Boolean(error)" class=" text-red-500 text-sm">{{ error && error.message }}</p>
  </div>
</template>

<script setup lang="ts">
  import { Field, ErrorMessage, defineRule } from 'vee-validate';

  defineRule('passwordsMatch', (value: string, [ target ]: [string]) => {
    if (value === target) {
      return true;
    }
    return 'Пароли не совпадают';
  });

  interface RegistrationFormValues {
    username: string;
    email: string;
    password: string;
    confirmPassword: string;
  }

  const initialValues: RegistrationFormValues = {
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  };

  const { mutate, error, isPending, isError } = useRegistration();

  const { handleSubmit } = useForm<RegistrationFormValues>({
    initialValues,
    validationSchema: {
      username: 'required|min:3|max:30',
      email: 'required|email',
      password: 'required|min:6|max:30',
      confirmPassword: 'required|passwordsMatch:@password'
    },
  });

  const onSubmit = handleSubmit((values: RegistrationFormValues) => {
    const { username, email, password } = values;
    mutate({ username, email, password }, {
      onSuccess: () => {
        navigateTo('/success');
      },
    });
  });
</script>
