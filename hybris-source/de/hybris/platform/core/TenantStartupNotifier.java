package de.hybris.platform.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TenantStartupNotifier
{
    public static final Logger LOG = LoggerFactory.getLogger(TenantStartupNotifier.class);
    public static final Class<? extends TenantStartupNotifier> DEFAULT_IMPL_CLASS = (Class)BlockingTenantStartupNotifier.class;


    static TenantStartupNotifier createTenantStartupNotifier(AbstractTenant tenant)
    {
        Objects.requireNonNull(tenant, "tenant mustn't be null.");
        Class<? extends TenantStartupNotifier> implementationClass = getImplementationClass(tenant);
        return instantiateImplementtion(implementationClass, tenant);
    }


    static TenantStartupNotifier instantiateImplementtion(Class<? extends TenantStartupNotifier> implementationClass, AbstractTenant tenant)
    {
        Constructor<?> ctor;
        try
        {
            ctor = implementationClass.getConstructor(new Class[] {Tenant.class});
            if(!Modifier.isPublic(ctor.getModifiers()))
            {
                LOG.warn("Couldn't use {} for tenant {} because of not public constructor. Default implementation will be used.", implementationClass
                                .getName(), tenant);
                ctor = DEFAULT_IMPL_CLASS.getConstructor(new Class[] {Tenant.class});
            }
        }
        catch(NoSuchMethodException | SecurityException e)
        {
            LOG.warn("Couldn't use {} implementation for tenant {} because of {}. Default implementation will be used.", new Object[] {implementationClass
                            .getName(), tenant, e});
            try
            {
                ctor = DEFAULT_IMPL_CLASS.getConstructor(new Class[] {Tenant.class});
            }
            catch(NoSuchMethodException | SecurityException ex)
            {
                throw new IllegalStateException("Couldn't access constructor of default implementation", ex);
            }
        }
        LOG.info("Instantiating {} for tenant {}.", ctor.getDeclaringClass().getName(), tenant);
        try
        {
            return (TenantStartupNotifier)ctor.newInstance(new Object[] {tenant});
        }
        catch(InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e)
        {
            throw new IllegalStateException("Couldn't instantiate " + ctor.getDeclaringClass().getName(), e);
        }
    }


    static Class<? extends TenantStartupNotifier> getImplementationClass(AbstractTenant tenant)
    {
        String notifierType = System.getProperty("tenant.startup.notifier", "default");
        if("default".equalsIgnoreCase(notifierType))
        {
            LOG.info("Using {} implementation of {} for tenant {}", new Object[] {notifierType, TenantStartupNotifier.class.getSimpleName(), tenant});
            return DEFAULT_IMPL_CLASS;
        }
        if("async".equalsIgnoreCase(notifierType))
        {
            LOG.info("Using {} implementation of {} for tenant {}", new Object[] {notifierType, TenantStartupNotifier.class.getSimpleName(), tenant});
            return (Class)AsyncTenantStartupNotifier.class;
        }
        if("blocking".equalsIgnoreCase(notifierType))
        {
            LOG.info("Using {} implementation of {} for tenant {}", new Object[] {notifierType, TenantStartupNotifier.class.getSimpleName(), tenant});
            return DEFAULT_IMPL_CLASS;
        }
        try
        {
            Class<? extends TenantStartupNotifier> configuredClass = Class.forName(notifierType).asSubclass(TenantStartupNotifier.class);
            return configuredClass;
        }
        catch(ClassNotFoundException | ClassCastException e)
        {
            LOG.warn("Couldn't use configured {} implementation for tenant {} because of {}. Default implementation will be used.", new Object[] {notifierType, tenant, e});
            return DEFAULT_IMPL_CLASS;
        }
    }


    void scheduleNotifyTenantListenersAboutStartup(List<TenantListener> paramList);


    void executeStartupNotify();
}
