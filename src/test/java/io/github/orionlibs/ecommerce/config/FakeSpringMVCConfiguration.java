package io.github.orionlibs.ecommerce.config;

import io.github.orionlibs.ecommerce.NewClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class FakeSpringMVCConfiguration
{
    @Bean
    public NewClass newClass()
    {
        return new NewClass();
    }
}
