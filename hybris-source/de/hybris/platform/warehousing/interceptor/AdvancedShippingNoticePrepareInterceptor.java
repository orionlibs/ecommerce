package de.hybris.platform.warehousing.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AdvancedShippingNoticePrepareInterceptor implements PrepareInterceptor<AdvancedShippingNoticeModel>
{
    private KeyGenerator keyGenerator;


    public void onPrepare(AdvancedShippingNoticeModel advancedShippingNotice, InterceptorContext context) throws InterceptorException
    {
        if(context.isNew(advancedShippingNotice) && StringUtils.isEmpty(advancedShippingNotice.getInternalId()))
        {
            advancedShippingNotice.setInternalId(getKeyGenerator().generate().toString());
        }
    }


    protected KeyGenerator getKeyGenerator()
    {
        return this.keyGenerator;
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
