package de.hybris.platform.solrfacetsearch.provider;

public interface Qualifier
{
    <U> U getValueForType(Class<U> paramClass);


    String toFieldQualifier();
}
