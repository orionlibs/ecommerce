package de.hybris.platform.media.storage.impl;

import de.hybris.platform.media.storage.ConfigValueConverter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigValueMappingRegistry
{
    @Autowired
    private Map<String, ConfigValueMappingRegistrar> registrars;
    private final Map<String, ConfigValueConverter> mappings = new HashMap<>();


    @PostConstruct
    public void init()
    {
        for(Map.Entry<String, ConfigValueMappingRegistrar> entry : this.registrars.entrySet())
        {
            ConfigValueMappingRegistrar registrar = entry.getValue();
            this.mappings.put(registrar.getKey(), registrar.getValue());
        }
    }


    public Map<String, ConfigValueConverter> getMappings()
    {
        return Collections.unmodifiableMap(this.mappings);
    }


    public String toString()
    {
        return "ConfigValueMappingRegistry [mappings=" + this.mappings + "]";
    }
}
