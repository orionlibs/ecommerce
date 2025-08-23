package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.solrfacetsearch.model.config.SolrSearchConfigModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class SolrSearchConfigPreparer implements PrepareInterceptor
{
    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SolrSearchConfigModel)
        {
            SolrSearchConfigModel config = (SolrSearchConfigModel)model;
            if(StringUtils.isNotEmpty(config.getDescription()))
            {
                return;
            }
            String sortOrder = "";
            String pageSize = "page size: " + config.getPageSize();
            if(CollectionUtils.isNotEmpty(config.getDefaultSortOrder()))
            {
                String elements = String.join(", ", config.getDefaultSortOrder());
                sortOrder = "page size: " + elements;
            }
            config.setDescription(pageSize + ";  " + pageSize);
        }
    }
}
