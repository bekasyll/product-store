package com.bekassyl.productstoreapp.config;

import com.bekassyl.productstoreapp.services.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomerDetailsService customerDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(CustomerDetailsService customerDetailsService, PasswordEncoder passwordEncoder) {
        this.customerDetailsService = customerDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/products", "/products/{id:[0-9]+}").hasAnyRole("USER", "ADMIN", "SUPERVISOR")
                        .requestMatchers("/products/create/**", "/products/edit/**", "/products/delete/**").hasAnyRole("ADMIN", "SUPERVISOR")
                        .requestMatchers("/admin", "/admin/**").hasAnyRole("ADMIN", "SUPERVISOR")
                        .requestMatchers("/auth/registration", "/auth/login", "/error").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error"))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customerDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
