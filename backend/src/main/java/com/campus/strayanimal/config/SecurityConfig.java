// backend/src/main/java/com/campus/strayanimal/config/SecurityConfig.java
package com.campus.strayanimal.config;

import com.campus.strayanimal.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/animals").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/animals/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/animals/**").hasRole("ADMIN")
                        .requestMatchers("/api/animals/**").authenticated()
                        .requestMatchers("/api/check-ins/**").authenticated()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new BasicAuthFilter(userRepository), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    static class BasicAuthFilter extends OncePerRequestFilter {
        private final UserRepository userRepository;

        BasicAuthFilter(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                try {
                    String decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
                    String[] parts = decoded.split(":", 2);
                    if (parts.length == 2) {
                        userRepository.findByUsername(parts[0]).ifPresent(user -> {
                            if (user.getPassword().equals(parts[1])) {
                                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                        user.getUsername(), null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
                                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(token);
                            }
                        });
                    }
                } catch (Exception ignored) {}
            }
            chain.doFilter(request, response);
        }
    }
}