package de.hybris.platform.personalizationfacades.converters;

public interface ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION> extends ConfigurableConverter<SOURCE, TARGET, OPTION>
{
    Class<SOURCE> getSourceClass();


    Class<TARGET> getTargetClass();


    Class<?> getMarkerClass();
}
