package io.github.orionlibs.ecommerce.core.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class JSONObjectMapper
{
    private ObjectMapper mapper;


    public JSONObjectMapper()
    {
        this.mapper = new Jackson2ObjectMapperBuilder().serializationInclusion(Include.NON_NULL)
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                                        SerializationFeature.FAIL_ON_SELF_REFERENCES)
                        .build();
        //this.mapper = mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.mapper = mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }


    public ObjectMapper getMapper()
    {
        return mapper;
    }
}
