package de.hybris.platform.orderhistory.impl;

import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Date;

public class HistoryEntryInterceptor implements InitDefaultsInterceptor, PrepareInterceptor
{
    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof OrderHistoryEntryModel)
        {
            ((OrderHistoryEntryModel)model).setTimestamp(new Date());
        }
    }


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof OrderHistoryEntryModel)
        {
            ((OrderHistoryEntryModel)model).setTimestamp(new Date());
        }
    }
}
