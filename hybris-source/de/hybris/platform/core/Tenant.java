package de.hybris.platform.core;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ItemLifecycleListener;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.numberseries.SerialNumberGenerator;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;

public interface Tenant extends DataSourceProvider
{
    ConfigIntf getConfig();


    String getTenantID();


    List<String> getTenantSpecificExtensionNames();


    Locale getTenantSpecificLocale();


    TimeZone getTenantSpecificTimeZone();


    InvalidationManager getInvalidationManager();


    Cache getCache();


    PersistencePool getPersistencePool();


    PersistenceManager getPersistenceManager();


    SystemEJB getSystemEJB();


    ThreadPool getThreadPool();


    ThreadPool getWorkersThreadPool();


    SingletonCreator getSingletonCreator();


    JaloConnection getJaloConnection();


    SerialNumberGenerator getSerialNumberGenerator();


    JaloSession getActiveSession();


    void resetTenantRestartMarker();


    Thread createAndRegisterBackgroundThread(Runnable paramRunnable, ThreadFactory paramThreadFactory);


    default List<ItemLifecycleListener> getAllItemLifecycleListeners()
    {
        return new LinkedList<>(getJaloConnection().getExtensionManager().getExtensions());
    }
}
