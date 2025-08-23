package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.config.ValueRanges;
import de.hybris.platform.solrfacetsearch.model.config.SolrValueRangeSetModel;

public class SolrRangeSetInterceptor implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SolrValueRangeSetModel)
        {
            String type = ((SolrValueRangeSetModel)model).getType();
            if(!ValueRanges.ALLOWEDTYPES.contains(type))
            {
                throw new InterceptorException("Possible range set types are : " + ValueRanges.getAllowedRangeTypes());
            }
        }
    }
}
