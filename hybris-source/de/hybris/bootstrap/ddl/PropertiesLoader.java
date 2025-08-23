package de.hybris.bootstrap.ddl;

import java.util.Collections;
import java.util.Map;

public interface PropertiesLoader
{
    String getProperty(String paramString);


    String getProperty(String paramString1, String paramString2);


    default Map<String, String> getAllProperties()
    {
        return Collections.emptyMap();
    }
}
