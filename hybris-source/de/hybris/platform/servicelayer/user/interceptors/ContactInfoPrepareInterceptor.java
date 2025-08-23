package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.user.AbstractContactInfoModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ContactInfoPrepareInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof AbstractContactInfoModel && ctx.isNew(model) &&
                        StringUtils.isBlank(((AbstractContactInfoModel)model).getCode()))
        {
            String contactInfoCode = (String)this.keyGenerator.generate();
            ((AbstractContactInfoModel)model).setCode(contactInfoCode);
        }
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
