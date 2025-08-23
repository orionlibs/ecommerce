package de.hybris.platform.cms2.events;

import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.servicelayer.event.events.AfterInitializationStartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import org.springframework.beans.factory.annotation.Required;

public class CmsAfterInitStartEventListener extends AbstractEventListener<AfterInitializationStartEvent>
{
    private ItemModelPrepareInterceptorService itemModelPrepareInterceptorService;


    protected void onEvent(AfterInitializationStartEvent afterInitializationStartEvent)
    {
        getItemModelPrepareInterceptorService().setEnabled(false);
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
