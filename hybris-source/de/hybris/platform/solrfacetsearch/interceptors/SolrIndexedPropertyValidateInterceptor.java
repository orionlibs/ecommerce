package de.hybris.platform.solrfacetsearch.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexedPropertyTypeRegistry;
import org.springframework.beans.factory.annotation.Required;

public class SolrIndexedPropertyValidateInterceptor implements ValidateInterceptor<SolrIndexedPropertyModel>
{
    private SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry;


    public void onValidate(SolrIndexedPropertyModel model, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(model.isFacet() && !this.solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(model.getType().getCode()).isAllowFacet())
        {
            throw new InterceptorException("The indexed property " + model
                            .getName() + " is of type " + model.getType().getCode() + " and cannot be facet.");
        }
    }


    public SolrIndexedPropertyTypeRegistry getSolrIndexedPropertyTypeRegistry()
    {
        return this.solrIndexedPropertyTypeRegistry;
    }


    @Required
    public void setSolrIndexedPropertyTypeRegistry(SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry)
    {
        this.solrIndexedPropertyTypeRegistry = solrIndexedPropertyTypeRegistry;
    }
}
