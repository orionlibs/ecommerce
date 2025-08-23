package de.hybris.platform.solrfacetsearch.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryTemplateModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Objects;

public class SolrSearchQueryTemplateValidateInterceptor implements ValidateInterceptor<SolrSearchQueryTemplateModel>
{
    public void onValidate(SolrSearchQueryTemplateModel solrSearchQueryTemplate, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(solrSearchQueryTemplate.getGroupProperty() == null || solrSearchQueryTemplate.getIndexedType() == null)
        {
            return;
        }
        SolrIndexedTypeModel groupIndexedType = solrSearchQueryTemplate.getGroupProperty().getSolrIndexedType();
        SolrIndexedTypeModel indexedType = solrSearchQueryTemplate.getIndexedType();
        if(!Objects.equals(groupIndexedType, indexedType))
        {
            throw new InterceptorException("The group property " + solrSearchQueryTemplate
                            .getGroupProperty().getName() + " does note belong to the indexed type: " + indexedType.getIdentifier());
        }
    }
}
