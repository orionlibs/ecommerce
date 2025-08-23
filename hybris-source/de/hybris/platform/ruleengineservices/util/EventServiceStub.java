package de.hybris.platform.ruleengineservices.util;

import com.google.common.collect.Sets;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class EventServiceStub implements EventService
{
    private static final Long DELAY = new Long(1000L);
    private Set<ApplicationListener> listeners = Sets.newHashSet();


    public void publishEvent(AbstractEvent event)
    {
        this.listeners.forEach(listener -> fireWithDelayIfEventTypeMatches(listener, event));
    }


    public boolean registerEventListener(ApplicationListener applicationListener)
    {
        if(!this.listeners.contains(applicationListener))
        {
            this.listeners.add(applicationListener);
            return true;
        }
        return false;
    }


    public boolean unregisterEventListener(ApplicationListener applicationListener)
    {
        if(this.listeners.contains(applicationListener))
        {
            this.listeners.remove(applicationListener);
            return true;
        }
        return false;
    }


    public Set<ApplicationListener> getEventListeners()
    {
        return this.listeners;
    }


    private static void delay()
    {
        try
        {
            Thread.sleep(DELAY.longValue());
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }


    private void fireWithDelayIfEventTypeMatches(ApplicationListener listener, AbstractEvent event)
    {
        if(matchEventType(listener, event))
        {
            (new Thread(() -> {
                delay();
                listener.onApplicationEvent((ApplicationEvent)event);
            })).start();
        }
    }


    private boolean matchEventType(ApplicationListener listener, AbstractEvent event)
    {
        Type[] genericInterfaces = listener.getClass().getGenericInterfaces();
        for(Type genericInterface : genericInterfaces)
        {
            if(genericInterface instanceof ParameterizedType)
            {
                ParameterizedType parameterizedTypeInterface = (ParameterizedType)genericInterface;
                Type[] typeArguments = parameterizedTypeInterface.getActualTypeArguments();
                return Arrays.<Type>stream(typeArguments).anyMatch(t -> t.getTypeName().equals(event.getClass().getName()));
            }
        }
        return false;
    }
}
