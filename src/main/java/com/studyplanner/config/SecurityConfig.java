package com.studyplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration // ici, on dit à Spring que la classe contient des beans
@EnableWebSecurity // activation du module de sécurité de Spring
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //on désactive le csrf
        http.csrf(csrf -> csrf.disable());
        // on autorise l'accès libre à /h2-console/ et /actuator/
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
        );
        //on active HTTP basic auth
        http.httpBasic(Customizer.withDefaults());
        //on autorise H2 dans un iframe
        http.headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin())
        );

        return http.build();
    }

    // définition des users autorisés à se connecter (en utilisant le pattern Builder)
    @Bean
    public UserDetailsService userDetailsService() {
        var alice = User.builder()
                .username("alice")
                .password(passwordEncoder().encode("alice123"))
                .roles("USER")
                .build();

        var bob = User.builder()
                .username("bob")
                .password(passwordEncoder().encode("bob123"))
                .roles("USER")
                .build();

        var charlie = User.builder()
                .username("charlie")
                .password(passwordEncoder().encode("charlie123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(alice, bob, charlie);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // à partir de "password123", on passe par BCrypt et on obtient le mdp encrypté
    }
}
