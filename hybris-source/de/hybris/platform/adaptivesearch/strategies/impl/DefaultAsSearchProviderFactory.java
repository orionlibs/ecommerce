package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.AsRuntimeException;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultAsSearchProviderFactory implements AsSearchProviderFactory, ApplicationContextAware
{
    private ApplicationContext applicationContext;


    public AsSearchProvider getSearchProvider()
    {
        Map<String, AsSearchProvider> searchProviders = this.applicationContext.getBeansOfType(AsSearchProvider.class);
        if(MapUtils.isEmpty(searchProviders))
        {
            throw new AsRuntimeException("No search provider found");
        }
        if(searchProviders.size() == 1)
        {
            return searchProviders.values().iterator().next();
        }
        return (AsSearchProvider)new CombinedSearchProvider(searchProviders.values());
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
