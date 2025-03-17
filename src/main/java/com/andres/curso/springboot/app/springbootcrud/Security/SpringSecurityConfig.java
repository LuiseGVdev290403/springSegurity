package com.andres.curso.springboot.app.springbootcrud.Security;

import com.andres.curso.springboot.app.springbootcrud.Security.filter.JwtAuthenticacionFilter;
import com.andres.curso.springboot.app.springbootcrud.Security.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;


    @Bean
    AuthenticationManager authenticationManager () throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }

    //esto genera un componente spring
    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authz) ->
                authz.requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .anyRequest()
                .authenticated())
                .addFilter(new JwtAuthenticacionFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}

