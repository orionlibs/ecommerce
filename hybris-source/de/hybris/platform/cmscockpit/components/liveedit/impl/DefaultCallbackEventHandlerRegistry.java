package de.hybris.platform.cmscockpit.components.liveedit.impl;

import de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler;
import de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandlerRegistry;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultCallbackEventHandlerRegistry implements CallbackEventHandlerRegistry<LiveEditView>, InitializingBean, ApplicationContextAware
{
    private Map<String, CallbackEventHandler> callbackEventHandlers;
    private ApplicationContext applicationContext;


    public void afterPropertiesSet() throws Exception
    {
        this.callbackEventHandlers = new HashMap<>();
        for(CallbackEventHandler<LiveEditView> handler : (Iterable<CallbackEventHandler<LiveEditView>>)this.applicationContext.getBeansOfType(CallbackEventHandler.class)
                        .values())
        {
            this.callbackEventHandlers.put(handler.getEventId(), handler);
        }
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public CallbackEventHandler getHandlerById(String id)
    {
        return this.callbackEventHandlers.get(id);
    }
}
