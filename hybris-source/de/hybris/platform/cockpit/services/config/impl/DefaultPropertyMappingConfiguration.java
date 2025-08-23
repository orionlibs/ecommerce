package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.PropertyMappingConfiguration;

public class DefaultPropertyMappingConfiguration implements PropertyMappingConfiguration
{
    private final String source;
    private final String target;


    public DefaultPropertyMappingConfiguration(String source, String target)
    {
        this.source = source;
        this.target = target;
    }


    public String getSource()
    {
        return this.source;
    }


    public String getTarget()
    {
        return this.target;
    }
}
