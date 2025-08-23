package de.hybris.platform.ordercancel.impl.interceptor;

import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class PrepareOrderCancelRecordInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof OrderCancelRecordModel)
        {
            OrderCancelRecordModel cancelModel = (OrderCancelRecordModel)model;
            if(cancelModel.getIdentifier() == null)
            {
                cancelModel.setIdentifier((String)this.keyGenerator.generate());
            }
        }
    }
}
