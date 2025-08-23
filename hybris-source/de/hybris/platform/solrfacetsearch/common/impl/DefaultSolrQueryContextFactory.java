package de.hybris.platform.solrfacetsearch.common.impl;

import de.hybris.platform.solrfacetsearch.common.SolrQueryContextFactory;
import de.hybris.platform.solrfacetsearch.common.SolrQueryContextProvider;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultSolrQueryContextFactory implements SolrQueryContextFactory, ApplicationContextAware, InitializingBean
{
    private ApplicationContext applicationContext;
    private List<SolrQueryContextProvider> queryContextProviders;


    public void afterPropertiesSet()
    {
        loadQueryContextProviders();
    }


    protected void loadQueryContextProviders()
    {
        Map<String, DefaultSolrQueryContextProviderDefinition> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)this.applicationContext, DefaultSolrQueryContextProviderDefinition.class);
        this
                        .queryContextProviders = (List<SolrQueryContextProvider>)beans.values().stream().sorted().map(DefaultSolrQueryContextProviderDefinition::getQueryContextProvider).collect(Collectors.toUnmodifiableList());
    }


    protected List<SolrQueryContextProvider> getQueryContextProviders()
    {
        return this.queryContextProviders;
    }


    public List<String> getQueryContexts()
    {
        Set<String> collectedQueryContexts = new LinkedHashSet<>();
        for(SolrQueryContextProvider queryContextProvider : this.queryContextProviders)
        {
            List<String> queryContexts = queryContextProvider.getQueryContexts();
            if(CollectionUtils.isNotEmpty(queryContexts))
            {
                collectedQueryContexts.addAll(queryContexts);
            }
        }
        return List.copyOf(collectedQueryContexts);
    }


    protected ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
