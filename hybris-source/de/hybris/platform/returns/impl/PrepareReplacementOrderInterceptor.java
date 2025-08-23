package de.hybris.platform.returns.impl;

import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class PrepareReplacementOrderInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ReplacementOrderModel)
        {
            ReplacementOrderModel orderModel = (ReplacementOrderModel)model;
            if(orderModel.getCode() == null)
            {
                orderModel.setCode((String)this.keyGenerator.generate());
            }
        }
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
