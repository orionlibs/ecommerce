package com.hybris.backoffice.solrsearch.validators;

import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import java.util.Optional;

public class BackofficeIndexedTypeToSolrFacetSearchConfigValidator implements ValidateInterceptor<BackofficeIndexedTypeToSolrFacetSearchConfigModel>
{
    public void onValidate(BackofficeIndexedTypeToSolrFacetSearchConfigModel config, InterceptorContext ctx) throws InterceptorException
    {
        if(Objects.nonNull(config.getSolrFacetSearchConfig()) && Objects.nonNull(config.getIndexedType()))
        {
            Optional<ComposedTypeModel> result = config.getSolrFacetSearchConfig().getSolrIndexedTypes().stream().map(it -> it.getType()).filter(ct -> ct.equals(config.getIndexedType())).findAny();
            if(!result.isPresent())
            {
                throw new InterceptorException("Configuration does not contain indexed type [" + config.getIndexedType().getCode() + "]");
            }
        }
    }
}
