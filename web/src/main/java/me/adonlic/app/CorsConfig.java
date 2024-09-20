package me.adonlic.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Adjust the path if necessary
                        .allowedOrigins("http://localhost:3000") // Allow the frontend's origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods allowed
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow credentials if needed (e.g., cookies)
//                registry.addMapping("/ws/**")
//                        .allowedOrigins("http://localhost:3000")
//                        .allowedMethods("*");
            }
        };
    }
}
