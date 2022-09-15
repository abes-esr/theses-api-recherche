package fr.abes.thesesapirecherche.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().exceptionHandling().and().authorizeRequests().antMatchers("/api/v1/recherche/**").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
