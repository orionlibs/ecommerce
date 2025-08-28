package io.github.orionlibs.ecommerce.core.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JSONService
{
    private static ObjectMapper mapper;

    static
    {
        mapper = new Jackson2ObjectMapperBuilder().serializationInclusion(Include.NON_NULL)
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                                        SerializationFeature.FAIL_ON_SELF_REFERENCES)
                        .build();
        //mapper = mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper = mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }

    public static String convertObjectToJSON(Object objectToConvert)
    {
        try
        {
            return mapper.writeValueAsString(objectToConvert);
        }
        catch(JsonProcessingException e)
        {
            return "";
        }
    }


    public static Object convertJSONToObject(String JSONData, Class<?> classToConvertTo)
    {
        try
        {
            return mapper.readValue(JSONData, classToConvertTo);
        }
        catch(JsonProcessingException e)
        {
            return "";
        }
    }
}
