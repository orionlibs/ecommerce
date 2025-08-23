package io.github.orionlibs.ecommerce.core.lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class LifecycleDefinitionParser
{
    @Resource(name = "yamlObjectMapper") private ObjectMapper yamlMapper;


    @SneakyThrows
    public LifecycleDefinition parseDefinition(String yamlPayload)
    {
        return yamlMapper.readValue(yamlPayload, LifecycleDefinition.class);
    }
}
