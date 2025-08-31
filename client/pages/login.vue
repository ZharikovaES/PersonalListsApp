<template>
  <div class='my-3 justify-center'>
    <div class="w-96 mx-auto">
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
          <label for="remember-me" class="mt-1 flex">
            <Field
              id="remember-me"
              name="remember"
              as="input"
              type="checkbox"
              class="mr-4 ml-1 scale-150"
              :value="true"
              :unchecked-value="false"
            />
            <span class="inline-block">Запомни меня</span>
          </label>
        </fieldset>
        <div>
          <button
            type="submit"
            :disabled="isPending"
            class="w-full mt-2 border-2 border-solid border-green-800 rounded-3xl px-5 py-2">
            <font-awesome v-if="isPending" icon="fa-spinner" spin/>
            <span v-else >Войти</span>
          </button>
        </div>
      </form>
      <div class="min-h-12 mt-2">
        <p v-if="isError && Boolean(error)" class=" text-red-500 text-sm">{{ error && error.message }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { Field, ErrorMessage } from 'vee-validate';

  interface LoginFormValues {
    username: string;
    password: string;
    remember: boolean;
  }

  const initialValues: LoginFormValues = {
    username: '',
    password: '',
    remember: false,
  };

  const { mutate, error, isPending, isError } = useLogin();

  const { handleSubmit } = useForm<LoginFormValues>({
    initialValues,
    validationSchema: {
      username: 'required|min:3|max:30',
      password: 'required|min:6|max:30',
    },
  });

  const onSubmit = handleSubmit((values: LoginFormValues) => {
    mutate(values);
  });
</script>
