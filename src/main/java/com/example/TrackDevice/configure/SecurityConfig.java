package com.example.TrackDevice.configure;

import com.example.TrackDevice.Exception.CustomAccessDeniedHandler;
import com.example.TrackDevice.Exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/resources/**").permitAll()
                        .requestMatchers("/static/js/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/img/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/regUser").hasRole("ADMIN")
                        .requestMatchers("/device").permitAll()
                        .requestMatchers("/device/model").permitAll()
                        .requestMatchers("/model").permitAll()
                        .requestMatchers("/type").permitAll()
                        .requestMatchers("/editTypeDev").permitAll()
                        .requestMatchers("/editTypeDev/*").permitAll()
                        .requestMatchers("/editTypeDev/{id}").permitAll()
                        .requestMatchers("/edit-model-dev").permitAll()
                        .requestMatchers("/addDevice").permitAll()
                        .requestMatchers("/csa").permitAll()
                        .requestMatchers("/addOrder/*").permitAll()
                        .requestMatchers("/addOrder/Add").permitAll()
                        .requestMatchers("/Acts/*").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout(config -> config.logoutSuccessUrl("/")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler()))

                // .authenticationEntryPoint(authenticationEntryPoint())

        .build();


    }
//                .headers(headers -> headers
//                        .frameOptions(frameOptions -> frameOptions.sameOrigin()))
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(); // Ваш обработчик доступа
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();}

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
