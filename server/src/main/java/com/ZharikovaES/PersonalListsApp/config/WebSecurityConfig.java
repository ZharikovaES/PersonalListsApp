package com.ZharikovaES.PersonalListsApp.config;

import com.ZharikovaES.PersonalListsApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf
                    .ignoringRequestMatchers("/**"))
                .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/", "/main", "/registration")
                    .permitAll()
                    .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(logout -> logout.deleteCookies("JSESSIONID"))
                .userDetailsService(userService)
                .rememberMe(rm -> rm.key("uniqueAndSecret").userDetailsService(userService));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true).ignoring().requestMatchers("/css/**", "/js/**", "/img/**", "/activate/*", "/favicon.ico");
    }
}