package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.apache.commons.lang3.StringUtils;

public class AsConfigurationInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AbstractAsConfigurationModel>
{
    public void onPrepare(AbstractAsConfigurationModel configuration, InterceptorContext context) throws InterceptorException
    {
        if(context.isNew(configuration) && StringUtils.isBlank(configuration.getUid()))
        {
            configuration.setUid(getAsUidGenerator().generateUid());
        }
    }
}
