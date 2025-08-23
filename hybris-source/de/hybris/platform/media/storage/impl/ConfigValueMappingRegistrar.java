package de.hybris.platform.media.storage.impl;

import de.hybris.platform.media.storage.ConfigValueConverter;

public class ConfigValueMappingRegistrar
{
    private String key;
    private ConfigValueConverter value;


    public void setValue(ConfigValueConverter value)
    {
        this.value = value;
    }


    public void setKey(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return this.key;
    }


    public ConfigValueConverter getValue()
    {
        return this.value;
    }
}
