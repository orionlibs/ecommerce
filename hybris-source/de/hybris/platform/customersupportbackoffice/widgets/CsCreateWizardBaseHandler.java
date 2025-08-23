package de.hybris.platform.customersupportbackoffice.widgets;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.context.Context;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class CsCreateWizardBaseHandler
{
    private CockpitEventQueue cockpitEventQueue;


    protected void publishEvent(String eventName, Object object, Context ctx)
    {
        if(!isCockpitEventNotificationDisabledInCtx(ctx))
        {
            DefaultCockpitEvent event = new DefaultCockpitEvent(eventName, object, null);
            populateEventContext(ctx, event);
            this.cockpitEventQueue.publishEvent((CockpitEvent)event);
        }
    }


    protected void populateEventContext(Context source, DefaultCockpitEvent destination)
    {
        if(source != null)
        {
            source.getAttributeNames().stream().forEach(a -> destination.getContext().put(a, source.getAttribute(a)));
        }
    }


    protected boolean isCockpitEventNotificationDisabledInCtx(Context ctx)
    {
        return (ctx != null &&
                        BooleanUtils.isTrue((Boolean)ctx.getAttribute("suppress_event")));
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    @Required
    public void setCockpitEventQueue(CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }
}
