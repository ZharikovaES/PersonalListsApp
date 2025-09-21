package com.ZharikovaES.PersonalListsApp.config;

import com.ZharikovaES.PersonalListsApp.services.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig implements WebMvcConfigurer {
  private final JwtFilter jwtFilter;

  public WebSecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      //FIXME: переделать CORS обработку
      .cors(cors -> cors.configurationSource(request -> new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues()))
      .httpBasic(basic -> basic.disable())
      .csrf(csrf -> csrf.disable())
      .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(
          authz -> authz
              .requestMatchers("/api/auth/login", "/api/auth/token", "/api/auth/registration", "/api/auth/activate")
              .permitAll()
              .anyRequest().authenticated()
      )
      .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
    }
}