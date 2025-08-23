package de.hybris.platform.solrfacetsearch.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexedPropertyTypeRegistry;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class SolrSearchQueryPropertyValidateInterceptor implements ValidateInterceptor<SolrSearchQueryPropertyModel>
{
    private SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry;


    public void onValidate(SolrSearchQueryPropertyModel solrSearchQueryProperty, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(solrSearchQueryProperty.getSearchQueryTemplate() == null || solrSearchQueryProperty.getIndexedProperty() == null)
        {
            return;
        }
        SolrIndexedTypeModel indexedType = solrSearchQueryProperty.getSearchQueryTemplate().getIndexedType();
        SolrIndexedTypeModel solrIndexedType = solrSearchQueryProperty.getIndexedProperty().getSolrIndexedType();
        if(!Objects.equals(indexedType, solrIndexedType))
        {
            throw new InterceptorException("The indexed property " + solrSearchQueryProperty.getIndexedProperty().getName() + "does not belong to the indexed type " + indexedType
                            .getIdentifier());
        }
        if(solrSearchQueryProperty.isFacet() &&
                        !this.solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(solrSearchQueryProperty.getIndexedProperty().getType().getCode()).isAllowFacet())
        {
            throw new InterceptorException("The indexed property " + solrSearchQueryProperty.getIndexedProperty().getName() + " is of type " + solrSearchQueryProperty
                            .getIndexedProperty().getType().getCode() + " and cannot be facet.");
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
