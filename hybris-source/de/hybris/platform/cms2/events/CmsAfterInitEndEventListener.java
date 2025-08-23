package de.hybris.platform.cms2.events;

import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import org.springframework.beans.factory.annotation.Required;

public class CmsAfterInitEndEventListener extends AbstractEventListener<AfterInitializationEndEvent>
{
    private ItemModelPrepareInterceptorService itemModelPrepareInterceptorService;


    protected void onEvent(AfterInitializationEndEvent afterInitializationEndEvent)
    {
        getItemModelPrepareInterceptorService().setEnabled(true);
    }


    protected ItemModelPrepareInterceptorService getItemModelPrepareInterceptorService()
    {
        return this.itemModelPrepareInterceptorService;
    }


    @Required
    public void setItemModelPrepareInterceptorService(ItemModelPrepareInterceptorService itemModelPrepareInterceptorService)
    {
        this.itemModelPrepareInterceptorService = itemModelPrepareInterceptorService;
    }
}
