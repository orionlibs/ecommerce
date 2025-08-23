package de.hybris.platform.admincockpit.services.impl;

import de.hybris.platform.admincockpit.services.ResourceConfiguration;

public class StringUriResourceConfiguration implements ResourceConfiguration<String, String>
{
    private final String key;
    private final String resource;


    public String getKey()
    {
        return this.key;
    }


    public String getResource()
    {
        return this.resource;
    }


    public StringUriResourceConfiguration(String key, String res)
    {
        this.key = key;
        this.resource = res;
    }
}
