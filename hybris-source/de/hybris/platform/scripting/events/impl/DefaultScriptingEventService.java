package de.hybris.platform.scripting.events.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.events.ScriptingEventService;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.tenant.TenantService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;

public class DefaultScriptingEventService implements ScriptingEventService
{
    private ScriptingLanguagesService scriptingLanguagesService;
    private EventService eventService;
    private TenantService tenantService;
    private ClusterService clusterService;
    private final Set<ScriptingListenerWrapper> registeredWrappers = Collections.synchronizedSet(new HashSet<>());
    private final InvalidationListener invalidationListener = (InvalidationListener)new Object(this);


    @PostConstruct
    public void init()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener(this.invalidationListener);
    }


    public boolean registerScriptingEventListener(String scriptURI)
    {
        Preconditions.checkNotNull(scriptURI, "The scriptURI has not been set. Make sure it points to correct script");
        ScriptingListenerWrapper wrapper = wrapScriptingListener(scriptURI);
        boolean registered = this.eventService.registerEventListener((ApplicationListener)wrapper);
        if(registered)
        {
            this.registeredWrappers.add(wrapper);
        }
        return registered;
    }


    public boolean unregisterScriptingEventListener(String scriptURI)
    {
        Preconditions.checkNotNull(scriptURI, "The scriptURI has not been set. Make sure it points to correct script");
        ScriptingListenerWrapper wrapper = ScriptingListenerWrapper.createEmptyWrapperWith(scriptURI);
        boolean unregistered = this.eventService.unregisterEventListener((ApplicationListener)wrapper);
        if(unregistered)
        {
            this.registeredWrappers.remove(wrapper);
        }
        return unregistered;
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    @Required
    public void setScriptingLanguagesService(ScriptingLanguagesService scriptingLanguagesService)
    {
        this.scriptingLanguagesService = scriptingLanguagesService;
    }


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }


    private ScriptingListenerWrapper wrapScriptingListener(String scriptURI)
    {
        return new ScriptingListenerWrapper(scriptURI, this);
    }


    AbstractEventListener prepareListenerWithDependencies(String scriptURI)
    {
        ScriptExecutable executable = this.scriptingLanguagesService.getExecutableByURI(scriptURI);
        AbstractEventListener listener = (AbstractEventListener)executable.getAsInterface(AbstractEventListener.class);
        Preconditions.checkState((listener != null), "Cannot create listener instance for script: " + scriptURI + " .The requested interface could not be found, probably the script contains errors. ");
        listener.setClusterService(this.clusterService);
        listener.setTenantService(this.tenantService);
        return listener;
    }


    Collection<ScriptingListenerWrapper> getRegisteredWrappers()
    {
        return Collections.unmodifiableSet(this.registeredWrappers);
    }
}
