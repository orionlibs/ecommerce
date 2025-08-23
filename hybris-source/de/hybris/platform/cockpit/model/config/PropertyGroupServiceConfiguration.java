package de.hybris.platform.cockpit.model.config;

public class PropertyGroupServiceConfiguration extends PropertyGroupConfiguration
{
    protected final String qualifier;


    public PropertyGroupServiceConfiguration(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }
}
