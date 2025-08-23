package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.event.EventDecorator;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.logging.Logs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;

public class HybrisApplicationEventMulticaster extends SimpleApplicationEventMulticaster
{
    private static Logger LOG = Logger.getLogger(HybrisApplicationEventMulticaster.class.getName());
    private List<EventDecorator<AbstractEvent>> allDecorators;
    private final ConcurrentHashMap<Class<? extends AbstractEvent>, List<EventDecorator<AbstractEvent>>> decoratorCacheMap = new ConcurrentHashMap<>();


    public void multicastEvent(ApplicationEvent event, ResolvableType eventType)
    {
        ApplicationEvent evt = decorate(event);
        if(evt != null)
        {
            notifyListeners(event, eventType);
        }
    }


    protected void notifyListeners(ApplicationEvent event)
    {
        notifyListeners(event, null);
    }


    protected void notifyListeners(ApplicationEvent event, ResolvableType eventType)
    {
        ResolvableType type = (eventType != null) ? eventType : ResolvableType.forInstance(event);
        for(ApplicationListener listener : getApplicationListeners(event, type))
        {
            Executor executor = getTaskExecutor();
            if(executor != null)
            {
                executor.execute((Runnable)new Object(this, listener, event));
                continue;
            }
            try
            {
                listener.onApplicationEvent(event);
            }
            catch(RuntimeException e)
            {
                handleListenerError(e, event);
            }
        }
    }


    protected void handleListenerError(RuntimeException e, ApplicationEvent event)
    {
        if(event instanceof AbstractEvent)
        {
            if(RedeployUtilities.isShutdownInProgress())
            {
                Logs.debug(LOG, () -> e.getMessage(), e);
            }
            else
            {
                LOG.error(e.getMessage(), e);
            }
        }
        else
        {
            throw e;
        }
    }


    public Collection<ApplicationListener<?>> getApplicationListeners()
    {
        return super.getApplicationListeners();
    }


    protected ApplicationEvent decorate(ApplicationEvent original)
    {
        if(hasDecorators() && original instanceof AbstractEvent)
        {
            AbstractEvent event = (AbstractEvent)original;
            for(EventDecorator<AbstractEvent> decorator : getDecorators((AbstractEvent)original))
            {
                event = decorator.decorate(event);
                if(event == null)
                {
                    break;
                }
            }
            return (ApplicationEvent)event;
        }
        return original;
    }


    protected boolean hasDecorators()
    {
        return CollectionUtils.isNotEmpty(this.allDecorators);
    }


    protected List<EventDecorator<AbstractEvent>> getDecorators(AbstractEvent event)
    {
        Class<? extends AbstractEvent> eventClass = (Class)event.getClass();
        List<EventDecorator<AbstractEvent>> cached = this.decoratorCacheMap.get(eventClass);
        if(cached == null)
        {
            List<EventDecorator<AbstractEvent>> decorators4Type = null;
            for(EventDecorator<AbstractEvent> decorator : this.allDecorators)
            {
                if(supportsEventType(decorator, eventClass))
                {
                    if(decorators4Type == null)
                    {
                        decorators4Type = new ArrayList<>();
                    }
                    decorators4Type.add(decorator);
                }
            }
            if(decorators4Type == null)
            {
                decorators4Type = Collections.EMPTY_LIST;
            }
            List<EventDecorator<AbstractEvent>> previous = this.decoratorCacheMap.putIfAbsent(eventClass, decorators4Type);
            cached = (previous == null) ? decorators4Type : previous;
        }
        return cached;
    }


    public boolean supportsEventType(EventDecorator<AbstractEvent> decorator, Class<? extends AbstractEvent> eventType)
    {
        Class<?> typeArg = GenericTypeResolver.resolveTypeArgument(decorator.getClass(), EventDecorator.class);
        if(typeArg == null || typeArg.equals(AbstractEvent.class))
        {
            Class<?> targetClass = AopUtils.getTargetClass(decorator);
            if(targetClass != decorator.getClass())
            {
                typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, EventDecorator.class);
            }
        }
        return (typeArg == null || typeArg.isAssignableFrom(eventType));
    }


    @Autowired(required = false)
    public void setAllDecorators(Collection<EventDecorator<AbstractEvent>> allDecorators)
    {
        if(CollectionUtils.isNotEmpty(allDecorators))
        {
            List<EventDecorator<AbstractEvent>> tmp = new ArrayList<>(allDecorators);
            Collections.sort(tmp, ComparatorUtils.nullHighComparator((Comparator)new Object(this)));
            this.allDecorators = tmp;
        }
        else
        {
            this.allDecorators = null;
        }
    }
}
