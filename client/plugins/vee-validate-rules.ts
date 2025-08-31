import { configure, defineRule } from 'vee-validate';
import { required, min, max, email } from '@vee-validate/rules';
import { localize, setLocale } from '@vee-validate/i18n';

export default defineNuxtPlugin(() => {
  defineRule('required', required);
  defineRule('min', min);
  defineRule('max', max);
  defineRule('email', email);

  configure({
    validateOnInput: true,
    generateMessage: localize({
      ru: {
        messages: {
          required: 'Обязательно для заполнения',
          min: 'Поле должно содержать минимум 0:{length} символов',
          max: 'Поле должно содержать максимум 0:{length} символов',
          email: 'Поле должно быть действительным электронным адресом',
        },
        names: {
          email: 'Электронная почта',
          password: 'Пароль',
          username: 'Имя пользователя',
        },
      },
    })
  });

  setLocale('ru');
});