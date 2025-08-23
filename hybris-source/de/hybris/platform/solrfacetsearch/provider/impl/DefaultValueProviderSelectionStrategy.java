package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.ValueProviderSelectionStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultValueProviderSelectionStrategy implements ValueProviderSelectionStrategy, ApplicationContextAware
{
    private String defaultValueProviderId;
    private ApplicationContext applicationContext;


    public String getDefaultValueProviderId()
    {
        return this.defaultValueProviderId;
    }


    @Required
    public void setDefaultValueProviderId(String defaultValueProviderId)
    {
        this.defaultValueProviderId = defaultValueProviderId;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public Object getValueProvider(String valueProviderId)
    {
        return this.applicationContext.getBean(valueProviderId);
    }


    public String resolveValueProvider(IndexedType indexedType, IndexedProperty indexedProperty)
    {
        if(!StringUtils.isBlank(indexedProperty.getFieldValueProvider()))
        {
            return indexedProperty.getFieldValueProvider();
        }
        if(!StringUtils.isBlank(indexedType.getDefaultFieldValueProvider()))
        {
            return indexedType.getDefaultFieldValueProvider();
        }
        return this.defaultValueProviderId;
    }


    public Map<String, Collection<IndexedProperty>> resolveValueProviders(IndexedType indexedType, Collection<IndexedProperty> indexedProperties)
    {
        Map<String, Collection<IndexedProperty>> valueProviders = new HashMap<>();
        for(IndexedProperty indexedProperty : indexedProperties)
        {
            String valueProviderId = resolveValueProvider(indexedType, indexedProperty);
            Collection<IndexedProperty> valueProviderEntries = valueProviders.get(valueProviderId);
            if(valueProviderEntries == null)
            {
                valueProviderEntries = new ArrayList<>();
                valueProviders.put(valueProviderId, valueProviderEntries);
            }
            valueProviderEntries.add(indexedProperty);
        }
        return valueProviders;
    }
}
