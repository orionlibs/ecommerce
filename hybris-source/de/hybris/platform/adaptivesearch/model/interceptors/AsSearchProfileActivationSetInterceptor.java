package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.model.AsSearchProfileActivationSetModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Optional;

public class AsSearchProfileActivationSetInterceptor extends AbstractAsInterceptor implements ValidateInterceptor<AsSearchProfileActivationSetModel>
{
    public void onValidate(AsSearchProfileActivationSetModel searchProfileActivationSet, InterceptorContext context) throws InterceptorException
    {
        String indexType = searchProfileActivationSet.getIndexType();
        AsSearchProvider searchProvider = resolveSearchProvider();
        if(searchProvider != null)
        {
            Optional<AsIndexTypeData> indexTypeDataOptional = searchProvider.getIndexTypeForCode(indexType);
            if(indexTypeDataOptional.isEmpty())
            {
                throw new InterceptorException("Index type does not exist: " + indexType);
            }
        }
    }
}
