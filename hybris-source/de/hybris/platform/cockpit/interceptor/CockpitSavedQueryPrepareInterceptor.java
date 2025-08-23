package de.hybris.platform.cockpit.interceptor;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class CockpitSavedQueryPrepareInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CockpitSavedQueryModel)
        {
            CockpitSavedQueryModel query = (CockpitSavedQueryModel)model;
            prepareUniqueId(query);
        }
    }


    private void prepareUniqueId(CockpitSavedQueryModel query)
    {
        if(query.getCode() == null)
        {
            query.setCode((String)this.keyGenerator.generate());
        }
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
