package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class CMSRelationInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private KeyGenerator processCodeGenerator;


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CMSRelationModel)
        {
            ((PersistentKeyGenerator)this.processCodeGenerator).setKey("CMSRelation");
            ((CMSRelationModel)model).setUid(String.valueOf(this.processCodeGenerator.generate()));
        }
    }


    @Required
    public void setProcessCodeGenerator(KeyGenerator keyGenerator)
    {
        this.processCodeGenerator = keyGenerator;
    }
}
