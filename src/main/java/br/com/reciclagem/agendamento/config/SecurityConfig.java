package br.com.reciclagem.agendamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // RN002.2: Protege as rotas administrativas
                .requestMatchers("/admin/**", "/materiais/**").authenticated()
                // Permite acesso público às outras rotas
                .anyRequest().permitAll()
            )
            // RN002.3: Configura o formulário de login
            .formLogin(form -> form
                .loginPage("/login") // Define a URL da página de login
                .defaultSuccessUrl("/admin/agendamentos", true) // Redireciona para a lista após o login
                .permitAll()
            )
            .logout(logout -> logout.permitAll());
        return http.build();
    }
}
