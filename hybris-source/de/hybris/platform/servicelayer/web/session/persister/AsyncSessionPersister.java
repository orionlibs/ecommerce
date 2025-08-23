package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantAwareThreadFactory;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.core.model.web.StoredHttpSessionModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.servicelayer.web.session.HybrisDeserializer;
import de.hybris.platform.servicelayer.web.session.PersistedSession;
import de.hybris.platform.servicelayer.web.session.StoredHttpSessionDao;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.serializer.Deserializer;

public class AsyncSessionPersister implements SessionPersister
{
    public static final String SPRING_SESSION_SAVE_ASYNC_SHOULD_THROW_EXCEPTION = "spring.session.save.async.should.throw.exception";
    public static final String SPRING_SESSION_SAVE_ASYNC_MAXRATIO_ADAPTIVE_ALGORITHM = "spring.session.save.async.maxratio.adaptive.algorithm";
    public static final String SPRING_SESSION_SAVE_ASYNC_THRESHOLD_ADAPTIVE_ALGORITHM = "spring.session.save.async.threshold.adaptive.algorithm";
    public static final String SPRING_SESSION_SAVE_ASYNC_TIMEOUT = "spring.session.save.async.timeout";
    public static final String SPRING_SESSION_SAVE_ASYNC_MAX_ITEMS = "spring.session.save.async.max.items";
    public static final String SPRING_SESSION_SAVE_ASYNC_QUEUE_SIZE = "spring.session.save.async.queue.size";
    public static final String SPRING_SESSION_SAVE_ASYNC_INTERVAL = "spring.session.save.async.interval";
    public static final String SPRING_SESSION_SAVE_ASYNC_USE_ADAPTIVE_ALGORITHM = "spring.session.save.async.use.adaptive.algorithm";
    private static final Logger LOG = LoggerFactory.getLogger(AsyncSessionPersister.class);
    private BlockingQueue<AsyncSessionPersisterOperation> queue;
    private AsyncSessionPersisterRunnable asyncSessionPersisterRunnable;
    private StoredHttpSessionDao storedHttpSessionDao;
    private Tenant myTenant;
    private volatile Thread workerThread;


    @PostConstruct
    public void init()
    {
        this.queue = this.asyncSessionPersisterRunnable.getQueue();
        this.myTenant = Registry.getCurrentTenant();
        registerTenantListener();
    }


    protected void registerTenantListener()
    {
        Registry.registerTenantListener((TenantListener)new Object(this));
    }


    protected void start()
    {
        if(this.workerThread == null || !this.workerThread.isAlive())
        {
            if(isAllowedToStart())
            {
                if(isAsyncSessionPersistenceConfigured())
                {
                    LOG.info("Starting async session persister since asynchronous session persistence is configured...");
                    TenantAwareThreadFactory threadFactory = new TenantAwareThreadFactory(this.myTenant);
                    this.workerThread = this.myTenant.createAndRegisterBackgroundThread((Runnable)this.asyncSessionPersisterRunnable, (ThreadFactory)threadFactory);
                    this.workerThread.setName("AsyncSessionPersister-" + this.myTenant.getTenantID() + "-" + this.workerThread.getId());
                    this.workerThread.start();
                }
                else
                {
                    LOG.info("No need to start async session persister since asynchronous session persistence is not configured.");
                }
            }
        }
        else
        {
            LOG.warn("Async session persister was already running. Cannot start it again!");
        }
    }


    protected boolean isAllowedToStart()
    {
        if(!JaloConnection.getInstance().isSystemInitialized())
        {
            LOG.info("System not initialized. Not starting async session persister.");
            return false;
        }
        if(Registry.isStandaloneMode())
        {
            LOG.info("System runs in standalone mode. Not starting async session persister.{}", Integer.valueOf(System.identityHashCode(this)));
            return false;
        }
        if(RedeployUtilities.isShutdownInProgress())
        {
            LOG.info("System about to shutdown. Not starting async session persister.");
            return false;
        }
        return true;
    }


    protected boolean isAsyncSessionPersistenceConfigured()
    {
        ConfigIntf cfg = Registry.getCurrentTenantNoFallback().getConfig();
        if(!cfg.getBoolean("spring.session.enabled", false))
        {
            return false;
        }
        Map<String, String> params = cfg.getParametersMatching("spring\\.session\\.(.*)\\.save");
        for(Map.Entry<String, String> e : params.entrySet())
        {
            if("async".equalsIgnoreCase(e.getValue()))
            {
                return true;
            }
        }
        return false;
    }


    protected void stop()
    {
        if(this.workerThread != null && this.workerThread.isAlive())
        {
            LOG.info("Stopping async session persister...");
            this.workerThread.interrupt();
        }
        else
        {
            LOG.debug("No async session persister to stop.");
        }
        this.workerThread = null;
    }


    public void persist(PersistedSession persistedSession)
    {
        long timeout = Config.getLong("spring.session.save.async.timeout", 5000L);
        boolean done = false;
        try
        {
            done = this.queue.offer(new AsyncSessionPersisterOperation(persistedSession), timeout, TimeUnit.MILLISECONDS);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        if(!done)
        {
            if(Config.getBoolean("spring.session.save.async.should.throw.exception", false))
            {
                throw new IllegalStateException("Session not persisted " + persistedSession);
            }
            LOG.warn("Session {} not persisted, timeout during persisting", persistedSession);
        }
    }


    public void remove(String id)
    {
        this.asyncSessionPersisterRunnable.scheduleSessionForRemoval(id);
        this.queue.add(new AsyncSessionPersisterOperation(id));
    }


    public PersistedSession load(String id, Deserializer deSerializer)
    {
        if(this.asyncSessionPersisterRunnable.isSessionScheduledForRemoval(id))
        {
            return null;
        }
        StoredHttpSessionModel storedHttpSessionModel = this.storedHttpSessionDao.findById(id);
        if(storedHttpSessionModel != null)
        {
            return HybrisDeserializer.deserialize((byte[])storedHttpSessionModel.getSerializedSession(), deSerializer);
        }
        return null;
    }


    @Required
    public void setAsyncSessionPersisterRunnable(AsyncSessionPersisterRunnable asyncSessionPersisterRunnable)
    {
        this.asyncSessionPersisterRunnable = asyncSessionPersisterRunnable;
    }


    @Required
    public void setStoredHttpSessionDao(StoredHttpSessionDao storedHttpSessionDao)
    {
        this.storedHttpSessionDao = storedHttpSessionDao;
    }
}
