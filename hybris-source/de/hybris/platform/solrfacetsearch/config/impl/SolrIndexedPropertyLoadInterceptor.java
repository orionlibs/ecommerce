package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import org.springframework.beans.factory.annotation.Required;

public class SolrIndexedPropertyLoadInterceptor implements LoadInterceptor
{
    private String defaultFacetSortProvider;


    public void onLoad(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SolrIndexedPropertyModel)
        {
            SolrIndexedPropertyModel indexedProperty = (SolrIndexedPropertyModel)model;
            if(indexedProperty.getCustomFacetSortProvider() == null)
            {
                indexedProperty.setCustomFacetSortProvider(this.defaultFacetSortProvider);
            }
        }
    }


    @Required
    public void setDefaultFacetSortProvider(String defaultFacetSortProvider)
    {
        this.defaultFacetSortProvider = defaultFacetSortProvider;
    }
}
