package de.hybris.platform.servicelayer.i18n.interceptors;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

public class RemoveBaseCurrencyInterceptor implements RemoveInterceptor
{
    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CurrencyModel)
        {
            if(((CurrencyModel)model).getBase().booleanValue())
            {
                throw new InterceptorException("Base currency cannot be removed.");
            }
        }
    }
}
