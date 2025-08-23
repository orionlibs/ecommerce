package de.hybris.platform.personalizationfacades.converters.impl;

import de.hybris.platform.personalizationfacades.converters.ConfigurableSubtypeConverter;

public class DefaultConfigurableSubtypeConverter<SOURCE, TARGET, OPTION> extends DefaultConfigurableConverter<SOURCE, TARGET, OPTION> implements ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION>
{
    private final Class<SOURCE> sourceClass;
    private final Class<?> markerClass;


    public DefaultConfigurableSubtypeConverter(Class<SOURCE> sourceClass, Class<?> markerClass)
    {
        this.markerClass = markerClass;
        this.sourceClass = sourceClass;
    }


    public Class<SOURCE> getSourceClass()
    {
        return this.sourceClass;
    }


    public Class<?> getMarkerClass()
    {
        return this.markerClass;
    }
}
