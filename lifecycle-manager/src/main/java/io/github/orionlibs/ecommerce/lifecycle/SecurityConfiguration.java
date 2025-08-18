package io.github.orionlibs.ecommerce.lifecycle;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

@Configuration
public class SecurityConfiguration
{
    @Bean
    public Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer()
    {
        return cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            //config.setAllowedOrigins(Arrays.asList("*"));
            config.setAllowedOriginPatterns(Arrays.asList("*"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "websocket", "ws"));
            config.setAllowedHeaders(Arrays.asList("*"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        });
    }


    @Bean
    public Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer()
    {
        return csrf -> csrf.disable();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.cors(corsCustomizer())
                        .csrf(csrfCustomizer())
                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        //.authenticationProvider(apiKeyProvider)
                        //.addFilterBefore(apiKeyAuthFilter, BearerTokenAuthenticationFilter.class)
                        .authorizeHttpRequests(authorize -> authorize.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                        .requestMatchers("/health/**", "/api/**", "/v1/**").permitAll()
                                        .anyRequest().authenticated());
        return http.build();
    }
}
