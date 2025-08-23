package de.hybris.platform.servicelayer.web.session;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.web.session.internal.SessionUtils;
import de.hybris.platform.servicelayer.web.session.persister.SessionPersister;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Deserializer;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

public class CachedPersistedSessionRepository implements SessionRepository
{
    private static final ThreadLocal<Boolean> CLOSE_SESSION_ON_REMOVE = ThreadLocal.withInitial(() -> Boolean.valueOf(true));
    private static final Logger LOG = LoggerFactory.getLogger(CachedPersistedSessionRepository.class);
    private final Deserializer deserializer;
    private final SessionPersister sessionPersister;
    private final String tenantID;
    private final int clusterId;
    private final String extension;
    private final String contextRoot;
    public static final String SESSION = "__SESSION__";


    public CachedPersistedSessionRepository(Deserializer deserializer, SessionPersister sessionPersister, String extension, String contextRoot)
    {
        this.deserializer = deserializer;
        this.sessionPersister = sessionPersister;
        this.tenantID = Registry.getCurrentTenantNoFallback().getTenantID();
        this.clusterId = Registry.getClusterID();
        this.extension = extension;
        this.contextRoot = contextRoot;
    }


    public Session createSession()
    {
        return (Session)createCachedPersistedSession();
    }


    public void save(Session session)
    {
        saveCachedPersistedSession(session);
    }


    public Session getSession(String id)
    {
        PersistedSession cachedPersistedSession = getCachedPersistedSession(id);
        if(cachedPersistedSession != null && cachedPersistedSession.isExpired())
        {
            removeCachedPersistedSession(id);
            return null;
        }
        if(cachedPersistedSession == null)
        {
            Cache cache = Registry.getCurrentTenant().getCache();
            cache.invalidate(SessionUtils.createSessionCacheKey(id), 2);
            return null;
        }
        return (Session)cachedPersistedSession;
    }


    public void delete(String id)
    {
        removeCachedPersistedSession(id);
    }


    public static void executeWithoutClosingJaloSessionOnDelete(Runnable runnable)
    {
        try
        {
            CLOSE_SESSION_ON_REMOVE.set(Boolean.valueOf(false));
            runnable.run();
        }
        finally
        {
            CLOSE_SESSION_ON_REMOVE.set(Boolean.valueOf(true));
        }
    }


    protected PersistedSession createCachedPersistedSession()
    {
        String newSessionUUID = "Y" + this.clusterId + "-" + UUID.randomUUID().toString();
        PersistedSession newSession = createNewSession(newSessionUUID);
        LOG.debug("Created new persisted session {}", newSession);
        return newSession;
    }


    protected void saveCachedPersistedSession(Session session)
    {
        this.sessionPersister.persist((PersistedSession)session);
        putInCache(session);
        LOG.debug("Saved persisted session {}", session);
    }


    private void putInCache(Session session)
    {
        Cache cache = Registry.getCurrentTenant().getCache();
        Object object = new Object(this, session.getId(), this.tenantID, cache, session);
        cache.getOrAddUnit((AbstractCacheUnit)object);
    }


    protected PersistedSession getCachedPersistedSession(String id)
    {
        AbstractCacheUnit unit = getFromCache(id);
        try
        {
            PersistedSession ret = (PersistedSession)unit.get();
            if(ret != null)
            {
                LOG.debug("Retrieved persisted session {} from cache", ret);
            }
            else
            {
                LOG.debug("No persisted session for id {} in cache", id);
            }
            return ret;
        }
        catch(Exception e)
        {
            LOG.error("Failed to retrieve session", e);
            return null;
        }
    }


    private AbstractCacheUnit getFromCache(String id)
    {
        Cache cache = Registry.getCurrentTenant().getCache();
        return (AbstractCacheUnit)new Object(this, id, this.tenantID, cache, id);
    }


    protected void removeCachedPersistedSession(String id)
    {
        if(((Boolean)CLOSE_SESSION_ON_REMOVE.get()).booleanValue())
        {
            LOG.debug("Closed jalo session for session with id: {}", id);
            closeJaloSession(id);
        }
        this.sessionPersister.remove(id);
        Cache cache = Registry.getCurrentTenant().getCache();
        cache.invalidate(SessionUtils.createSessionCacheKey(id), 2);
        LOG.debug("Removed persisted session with id: {}", id);
    }


    protected void closeJaloSession(String id)
    {
        PersistedSession cachedPersistedSession = getCachedPersistedSession(id);
        if(cachedPersistedSession != null)
        {
            JaloSession jaloSession = (JaloSession)cachedPersistedSession.getAttribute("jalosession");
            if(jaloSession != null)
            {
                jaloSession.close();
            }
        }
    }


    protected PersistedSession loadFromPersistenceAndDeserialize(String id)
    {
        return this.sessionPersister.load(id, this.deserializer);
    }


    protected PersistedSession createNewSession(String id)
    {
        return new PersistedSession(id, this.clusterId, this.extension, this.contextRoot, getSessionTimeoutConfiguration());
    }


    private int getSessionTimeoutConfiguration()
    {
        Tenant tenant = Registry.getCurrentTenant();
        int extensionTimeout = tenant.getConfig().getInt(this.extension + ".session.timeout", -1);
        if(extensionTimeout < 0)
        {
            return tenant.getConfig().getInt("default.session.timeout", 86400);
        }
        return extensionTimeout;
    }
}
