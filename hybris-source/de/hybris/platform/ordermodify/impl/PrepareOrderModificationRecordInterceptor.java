package de.hybris.platform.ordermodify.impl;

import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class PrepareOrderModificationRecordInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof OrderModificationRecordModel)
        {
            OrderModificationRecordModel modificationModel = (OrderModificationRecordModel)model;
            if(modificationModel.getIdentifier() == null)
            {
                modificationModel.setIdentifier((String)this.keyGenerator.generate());
            }
        }
    }
}
