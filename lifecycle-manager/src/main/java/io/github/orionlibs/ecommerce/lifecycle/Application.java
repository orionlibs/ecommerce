package io.github.orionlibs.ecommerce.lifecycle;

import io.github.orionlibs.ecommerce.core.api.error.GlobalExceptionHandler;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "io.github.orionlibs")
@Configuration
@ConfigurationPropertiesScan
@Import(GlobalExceptionHandler.class)
@EnableJpaRepositories(basePackages = {"io.github.orionlibs"})
@EntityScan({"io.github.orionlibs"})
public class Application extends SpringBootServletInitializer implements WebMvcConfigurer
{
    @Value("${version}")
    private String version;
    @Value("${environment}")
    private String environment;


    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "websocket", "ws")
                        .allowedHeaders("*");
    }
}