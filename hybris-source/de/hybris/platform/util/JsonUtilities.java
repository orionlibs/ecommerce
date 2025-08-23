package de.hybris.platform.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

public class JsonUtilities
{
    private static final ObjectMapper mapper = new ObjectMapper();


    public static String toJson(Map<String, Object> input) throws IOException
    {
        return mapper.writeValueAsString(input);
    }


    public static <T> T fromJson(String input, Class<T> clazz)
    {
        try
        {
            return (T)mapper.readValue(input, clazz);
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
