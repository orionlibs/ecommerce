package de.hybris.platform.cms2.cmsitems.converter;

@FunctionalInterface
public interface AttributeStrategyConverterProvider<T>
{
    AttributeContentConverter<T> getContentConverter(T paramT);
}
