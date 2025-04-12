package com.ZharikovaES.PersonalListsApp.config;

import com.ZharikovaES.PersonalListsApp.services.JwtFilter;
import com.ZharikovaES.PersonalListsApp.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    // @Autowired
    // private UserService userService;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // @Bean
    // public PasswordEncoder getPasswordEncoder() {
    //     return new BCryptPasswordEncoder(8);
    // }


    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.csrf(csrf -> csrf.disable());
    //     http.authorizeRequests(requests -> requests
    //             .antMatchers( "/", "/css/**", "/js/**", "/img/**", "/api/activate/*").permitAll()
    //             .anyRequest().authenticated())
    //             .formLogin(login -> login
    //                     .loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/home").failureUrl("/login?error")
    //                     .permitAll())
    //             .logout(logout -> logout.deleteCookies("JSESSIONID"))
    //             .rememberMe(me -> me.key("uniqueAndSecret"));
    // }

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userService)
    //             .passwordEncoder(passwordEncoder);
    // }

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(
                        authz -> authz
                                .antMatchers("/api/auth/login", "/api/auth/token", "/api/auth/registration", "/api/activate/{code}").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                ).build();
    }
}