package de.hybris.y2ysync.model;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.apache.commons.lang.StringUtils;

public class Y2YStreamConfigurationContainerPrepareInterceptor implements PrepareInterceptor<Y2YStreamConfigurationContainerModel>
{
    private final String FEED_SUFFIX = "_feed";
    private final String POOL_SUFFIX = "_pool";


    public void onPrepare(Y2YStreamConfigurationContainerModel configurationContainer, InterceptorContext ctx) throws InterceptorException
    {
        assignFeedAndPoolIfNotSet(configurationContainer);
    }


    private void assignFeedAndPoolIfNotSet(Y2YStreamConfigurationContainerModel configurationContainer)
    {
        if(StringUtils.isBlank(configurationContainer.getFeed()) && StringUtils.isBlank(configurationContainer.getPool()))
        {
            configurationContainer.setFeed(configurationContainer.getId() + "_feed");
            configurationContainer.setPool(configurationContainer.getId() + "_pool");
        }
    }
}
