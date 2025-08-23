package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class CustomerIDPrepareInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CustomerModel && ((CustomerModel)model).getCustomerID() == null)
        {
            ((CustomerModel)model).setCustomerID((String)this.keyGenerator.generate());
        }
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
