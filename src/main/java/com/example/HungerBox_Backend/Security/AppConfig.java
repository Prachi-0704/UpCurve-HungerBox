package com.example.HungerBox_Backend.Security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig implements WebMvcConfigurer {

    /**
     * Configures security settings for the application.
     *
     * @param http the HttpSecurity object for configuring security
     * @return SecurityFilterChain object
     * @throws Exception if configuration fails
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   // Set session policy to stateless
                .authorizeHttpRequests(Authorize ->
                        Authorize.anyRequest().permitAll())     // Allow all requests (modify as needed)
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)      // Add JWT filter before basic auth filter
                .csrf(csrf->csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));     // Enable CORS with custom configuration

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtTokenValidator(), UsernamePasswordAuthenticationFilter.class);

        return http.build();    // Build and return the SecurityFilterChain
    }

    /**
     * Provides a bean for the JwtTokenValidator filter.
     *
     * @return JwtTokenValidator object
     */
    @Bean
    public JwtTokenValidator jwtTokenValidator(){
        return new JwtTokenValidator();
    }

    /**
     * Configures CORS settings.
     *
     * @return CorsConfigurationSource object
     */
    private CorsConfigurationSource corsConfigurationSource(){

        return new CorsConfigurationSource(){

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request){

                CorsConfiguration cfg = new CorsConfiguration();

                cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000"));   // Allowed origins

                cfg.setAllowedMethods(Collections.singletonList("*"));   // Allowed methods

                cfg.setAllowCredentials(true);

                cfg.setAllowedHeaders(Collections.singletonList("*"));   //Allowed headers

                cfg.setExposedHeaders(Arrays.asList("Authorization"));   // Expose Authorization header

                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }

    /**
     * Provides a bean for password encoding using BCrypt.
     *
     * @return PasswordEncoder object
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures resource handling for static resources.
     *
     * @param registry the ResourceHandlerRegistry object for adding resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");     // Map /images/** to classpath:/static/images/
    }
}