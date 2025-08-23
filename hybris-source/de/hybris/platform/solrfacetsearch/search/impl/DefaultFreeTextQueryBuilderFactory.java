package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryBuilder;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryBuilderFactory;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class DefaultFreeTextQueryBuilderFactory implements FreeTextQueryBuilderFactory, BeanFactoryAware
{
    private static final Logger LOG = Logger.getLogger(DefaultFreeTextQueryBuilderFactory.class);
    private static final String DEFAULT_QUERY_BUILDER = "defaultFreeTextQueryBuilder";
    private BeanFactory beanFactory;


    public FreeTextQueryBuilder createQueryBuilder(SearchQuery searchQuery)
    {
        ServicesUtil.validateParameterNotNull(searchQuery, "Parameter 'search query' can not be null!");
        ServicesUtil.validateParameterNotNull(searchQuery.getIndexedType(), "Parameter 'index type' can not be null!");
        String queryBuilderBeanId = searchQuery.getFreeTextQueryBuilder();
        if(StringUtils.isBlank(queryBuilderBeanId))
        {
            queryBuilderBeanId = "defaultFreeTextQueryBuilder";
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Using query builder " + queryBuilderBeanId);
        }
        return (FreeTextQueryBuilder)this.beanFactory.getBean(queryBuilderBeanId, FreeTextQueryBuilder.class);
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }
}
