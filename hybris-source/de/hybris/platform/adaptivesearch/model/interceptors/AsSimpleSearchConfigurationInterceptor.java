package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AsSimpleSearchConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.apache.commons.lang3.StringUtils;

public class AsSimpleSearchConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AsSimpleSearchConfigurationModel>
{
    public void onPrepare(AsSimpleSearchConfigurationModel searchConfiguration, InterceptorContext context) throws InterceptorException
    {
        updateUniqueIdx(searchConfiguration);
    }


    protected void updateUniqueIdx(AsSimpleSearchConfigurationModel searchConfiguration)
    {
        String previousUniqueIdx = searchConfiguration.getUniqueIdx();
        String uniqueIdx = getAsItemModelHelper().generateSimpleSearchConfigurationUniqueIdx(searchConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            searchConfiguration.setUniqueIdx(uniqueIdx);
        }
    }
}
