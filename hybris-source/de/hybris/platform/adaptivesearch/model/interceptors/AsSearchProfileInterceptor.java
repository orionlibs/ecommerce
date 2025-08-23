package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsSearchProfileActivationSetModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;

public class AsSearchProfileInterceptor extends AbstractAsInterceptor implements ValidateInterceptor<AbstractAsSearchProfileModel>
{
    public void onValidate(AbstractAsSearchProfileModel searchProfile, InterceptorContext context) throws InterceptorException
    {
        String indexType = searchProfile.getIndexType();
        CatalogVersionModel catalogVersion = searchProfile.getCatalogVersion();
        AsSearchProvider searchProvider = resolveSearchProvider();
        if(searchProvider != null)
        {
            Optional<AsIndexTypeData> indexTypeDataOptional = searchProvider.getIndexTypeForCode(indexType);
            if(indexTypeDataOptional.isEmpty())
            {
                throw new InterceptorException("Index type does not exist: " + indexType);
            }
            List<String> supportedQueryContexts = ListUtils.emptyIfNull(searchProvider.getSupportedQueryContexts(indexType));
            String queryContext = searchProfile.getQueryContext();
            if(StringUtils.isNotEmpty(queryContext) && !supportedQueryContexts.contains(queryContext))
            {
                throw new InterceptorException("Query context does not exist: requested=" + queryContext + "; supported=" + (String)supportedQueryContexts
                                .stream().collect(Collectors.joining(",")));
            }
        }
        AsSearchProfileActivationSetModel activationSet = searchProfile.getActivationSet();
        if(activationSet != null)
        {
            if(!Objects.equals(indexType, activationSet.getIndexType()))
            {
                throw new InterceptorException("Cannot assign search profile to an activation set that has a different index type: " + indexType + " != " + activationSet
                                .getIndexType());
            }
            if(!Objects.equals(catalogVersion, activationSet.getCatalogVersion()))
            {
                throw new InterceptorException("Cannot assign search profile to an activation set that has a different catalogversion: " + catalogVersion + " != " + activationSet
                                .getCatalogVersion());
            }
        }
    }
}
