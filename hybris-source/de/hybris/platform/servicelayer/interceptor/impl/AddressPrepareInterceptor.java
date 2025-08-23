package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

public class AddressPrepareInterceptor implements PrepareInterceptor<AddressModel>
{
    public void onPrepare(AddressModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(model.getProperty("duplicate") == null)
        {
            model.setDuplicate(Boolean.valueOf((model.getOriginal() != null)));
        }
    }
}
