package de.hybris.platform.scripting.events.impl;

import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

class ScriptingListenerWrapper implements SmartApplicationListener
{
    private static final Logger LOG = Logger.getLogger(ScriptingListenerWrapper.class);
    private final String scriptURI;
    private Class eventType;
    private DefaultScriptingEventService scriptingEventService;
    private volatile boolean needsReload;
    private volatile AbstractEventListener listener;


    public ScriptingListenerWrapper(String scriptURI, DefaultScriptingEventService scriptingEventService)
    {
        this.scriptingEventService = scriptingEventService;
        this.scriptURI = scriptURI;
        this.listener = scriptingEventService.prepareListenerWithDependencies(scriptURI);
        this.eventType = determineEventType(this.listener);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Listener Wrapper created for listener with scriptURI=" + scriptURI + " and event type=" + this.eventType);
        }
    }


    private ScriptingListenerWrapper(String scriptURI)
    {
        this.scriptURI = scriptURI;
    }


    public static ScriptingListenerWrapper createEmptyWrapperWith(String scriptUri)
    {
        return new ScriptingListenerWrapper(scriptUri);
    }


    public void onApplicationEvent(ApplicationEvent event)
    {
        if(this.needsReload)
        {
            reloadListener();
        }
        if(this.listener != null)
        {
            this.listener.onApplicationEvent((AbstractEvent)event);
        }
    }


    private synchronized void reloadListener()
    {
        if(!this.needsReload)
        {
            return;
        }
        try
        {
            this.listener = this.scriptingEventService.prepareListenerWithDependencies(this.scriptURI);
        }
        catch(ScriptNotFoundException e)
        {
            this.scriptingEventService.unregisterScriptingEventListener(this.scriptURI);
            this.listener = null;
        }
        this.needsReload = false;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ScriptingListenerWrapper that = (ScriptingListenerWrapper)o;
        if((this.scriptURI != null) ? !this.scriptURI.equals(that.scriptURI) : (that.scriptURI != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        return (this.scriptURI != null) ? this.scriptURI.hashCode() : 0;
    }


    public void markAsNeedsReload()
    {
        this.needsReload = true;
    }


    public String getScriptURI()
    {
        return this.scriptURI;
    }


    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass)
    {
        return this.eventType.isAssignableFrom(aClass);
    }


    public boolean supportsSourceType(Class<?> aClass)
    {
        return true;
    }


    public int getOrder()
    {
        return 0;
    }


    private Class determineEventType(AbstractEventListener listener)
    {
        Type genericSuperclass = listener.getClass().getGenericSuperclass();
        if(ParameterizedType.class.isAssignableFrom(genericSuperclass.getClass()))
        {
            Type[] parameterizedTypes = ((ParameterizedType)genericSuperclass).getActualTypeArguments();
            return (Class)parameterizedTypes[0];
        }
        return AbstractEvent.class;
    }
}
