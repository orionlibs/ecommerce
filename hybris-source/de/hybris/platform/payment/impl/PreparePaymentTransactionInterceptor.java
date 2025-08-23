package de.hybris.platform.payment.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class PreparePaymentTransactionInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof PaymentTransactionModel)
        {
            PaymentTransactionModel authModel = (PaymentTransactionModel)model;
            if(authModel.getCode() == null)
            {
                authModel.setCode((String)this.keyGenerator.generate());
            }
            if(authModel.getOrder() instanceof OrderModel)
            {
                OrderModel order = (OrderModel)authModel.getOrder();
                if(ctx.isNew(order) && order.getVersionID() != null && order.getOriginalVersion() != null)
                {
                    authModel.setVersionID(order.getVersionID());
                }
            }
        }
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
