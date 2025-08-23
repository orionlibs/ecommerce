package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.web.StoredHttpSessionModel;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.web.session.PersistedSession;
import de.hybris.platform.servicelayer.web.session.StoredHttpSessionDao;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AsyncSessionPersisterRunnable extends AbstractSessionPersister implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(AsyncSessionPersisterRunnable.class);
    private BlockingQueue<AsyncSessionPersisterOperation> queue;
    private long saveInterval;
    private int sessionQueueCapacity;
    private int maxSessionsToPersist;
    private StoredHttpSessionDao storedHttpSessionDao;
    private ModelService modelService;
    private Tenant tenant;
    private DrainingAdaptiveAlgorithm drainingAdaptiveAlgorithm;
    private final ConcurrentHashSet sessionsToBeRemoved = new ConcurrentHashSet();


    @PostConstruct
    public void init()
    {
        ConfigIntf config = this.tenant.getConfig();
        this.saveInterval = config.getLong("spring.session.save.async.interval", 1000L);
        this.sessionQueueCapacity = config.getInt("spring.session.save.async.queue.size", 10000);
        this.maxSessionsToPersist = config.getInt("spring.session.save.async.max.items", 1000);
        this.queue = new ArrayBlockingQueue<>(this.sessionQueueCapacity);
    }


    public void run()
    {
        try
        {
            OperationInfo operationInfo = OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build();
            OperationInfo.updateThread(operationInfo);
            if(LOG.isInfoEnabled())
            {
                LOG.info("Async session persister started for tenant {} (queue size:{}, delay:{}", new Object[] {this.tenant.getTenantID(),
                                Integer.toString(this.sessionQueueCapacity), Long.toString(this.saveInterval)});
            }
            do
            {
                try
                {
                    long startTime = System.currentTimeMillis();
                    persistsSessionsFromQueue();
                    Thread.sleep(getDelay(startTime));
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                catch(Exception ex)
                {
                    LOG.error("Async session persister encountered an exception while trying to persist sessions asynchronously!", ex);
                }
            }
            while(processingAllowed());
        }
        finally
        {
            if(LOG.isDebugEnabled())
            {
                LOG.info("Async session persister has stopped. Pending changes are discarded. (queue length: {})",
                                Integer.toString(this.queue.size()));
            }
            this.queue.clear();
        }
    }


    private long getDelay(long startTime)
    {
        long delay = this.saveInterval - System.currentTimeMillis() - startTime;
        return (delay >= 0L) ? delay : 0L;
    }


    protected boolean processingAllowed()
    {
        return (!Thread.currentThread().isInterrupted() && !RedeployUtilities.isShutdownInProgress() &&
                        !Initialization.isTenantInitializingLocally(this.tenant));
    }


    protected void persistsSessionsFromQueue()
    {
        List<AsyncSessionPersisterOperation> queuedSessionsToPersist = drainQueue();
        if(!queuedSessionsToPersist.isEmpty())
        {
            Set<String> sessionsToRemove = new LinkedHashSet<>();
            Collection<PersistedSession> toUpdate = deduplicateQueuedSessions(queuedSessionsToPersist, sessionsToRemove);
            persistSessions(toUpdate);
            removeSessions(sessionsToRemove);
            this.modelService.detachAll();
        }
    }


    protected Collection<PersistedSession> deduplicateQueuedSessions(List<AsyncSessionPersisterOperation> fifoSessionHolders, Set<String> sessionsToRemove)
    {
        Map<String, PersistedSession> sessionsToUpdate = new LinkedHashMap<>();
        for(AsyncSessionPersisterOperation operation : fifoSessionHolders)
        {
            if(operation.isRemoval())
            {
                sessionsToRemove.add(operation.getSessionIdToRemove());
                continue;
            }
            sessionsToUpdate.put(operation.getSessionToUpdate().getId(), operation.getSessionToUpdate());
        }
        for(String removedSession : sessionsToRemove)
        {
            sessionsToUpdate.remove(removedSession);
            this.sessionsToBeRemoved.remove(removedSession);
        }
        if(LOG.isDebugEnabled())
        {
            int before = fifoSessionHolders.size();
            int after = sessionsToRemove.size() + sessionsToRemove.size();
            int saved = before - after;
            LOG.debug("Async session persister session changes de-duplication before:{} after:{} saved:{} removals:{}", new Object[] {Integer.toString(before), Integer.toString(after), Integer.toString(saved),
                            Integer.toString(sessionsToRemove.size())});
        }
        return sessionsToUpdate.values();
    }


    protected List<AsyncSessionPersisterOperation> drainQueue()
    {
        int drainSize = this.drainingAdaptiveAlgorithm.getDrainSize(this.queue.size());
        int sizeBefore = this.queue.size();
        int remainingCapacityBefore = this.queue.remainingCapacity();
        List<AsyncSessionPersisterOperation> sessionHoldersToPersist = new ArrayList<>(drainSize);
        int drained = this.queue.drainTo(sessionHoldersToPersist, drainSize);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Queue.remainingCapacity(): {} -> {} drain size: {} drained:{} queue.size() {} -> {} ",
                            new Object[] {Integer.valueOf(remainingCapacityBefore), Integer.valueOf(this.queue.remainingCapacity()), Integer.valueOf(drainSize), Integer.valueOf(drained), Integer.valueOf(sizeBefore), Integer.valueOf(this.queue.size())});
        }
        return sessionHoldersToPersist;
    }


    protected void removeSessions(Set<String> ids)
    {
        this.storedHttpSessionDao.deleteSessions(ids);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Async session persister removed {} sessions.", Integer.toString(ids.size()));
        }
    }


    protected void persistSessions(Collection<PersistedSession> withoutDuplicates)
    {
        List<StoredHttpSessionModel> toSave = new ArrayList<>(withoutDuplicates.size());
        Map<String, StoredHttpSessionModel> storedHttpSessionModelMap = provideStoredHttpSessionModelMap(
                        getStoredHttpSessionModelIds(withoutDuplicates));
        for(PersistedSession session : withoutDuplicates)
        {
            StoredHttpSessionModel storedHttpSession = storedHttpSessionModelMap.get(session.getId());
            if(updateModelIfNecessary(storedHttpSession, session))
            {
                toSave.add(storedHttpSession);
            }
        }
        if(CollectionUtils.isNotEmpty(toSave))
        {
            this.modelService.saveAll(toSave);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Async session persister saved {} session changes.", Integer.toString(toSave.size()));
            }
        }
    }


    protected Map<String, StoredHttpSessionModel> provideStoredHttpSessionModelMap(Collection<String> ids)
    {
        Map<String, StoredHttpSessionModel> httpSessionModelMap = createStoredHttpSessionModelMap(this.storedHttpSessionDao
                        .findByIds(ids));
        int idsSize = ids.size();
        int httpSessionModelMapSize = httpSessionModelMap.size();
        if(idsSize != httpSessionModelMapSize)
        {
            ids.forEach(e -> {
                if(!httpSessionModelMap.containsKey(e))
                {
                    StoredHttpSessionModel newStoredHttpSession = (StoredHttpSessionModel)this.modelService.create(StoredHttpSessionModel.class);
                    newStoredHttpSession.setSessionId(e);
                    httpSessionModelMap.put(e, newStoredHttpSession);
                }
            });
        }
        return httpSessionModelMap;
    }


    private Map<String, StoredHttpSessionModel> createStoredHttpSessionModelMap(Collection<StoredHttpSessionModel> sessionModels)
    {
        return (Map<String, StoredHttpSessionModel>)sessionModels.stream().collect(Collectors.toMap(StoredHttpSessionModel::getSessionId, v -> v));
    }


    protected Collection<String> getStoredHttpSessionModelIds(Collection<PersistedSession> storedHttpSessionModels)
    {
        return (Collection<String>)storedHttpSessionModels.stream().map(PersistedSession::getId).collect(Collectors.toList());
    }


    public BlockingQueue<AsyncSessionPersisterOperation> getQueue()
    {
        return this.queue;
    }


    public boolean isSessionScheduledForRemoval(String id)
    {
        return this.sessionsToBeRemoved.contains(id);
    }


    public void scheduleSessionForRemoval(String id)
    {
        this.sessionsToBeRemoved.add(id);
    }


    @Required
    public void setStoredHttpSessionDao(StoredHttpSessionDao storedHttpSessionDao)
    {
        this.storedHttpSessionDao = storedHttpSessionDao;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTenant(Tenant tenant)
    {
        this.tenant = tenant;
    }


    @Required
    public void setDrainingAdaptiveAlgorithm(DrainingAdaptiveAlgorithm drainingAdaptiveAlgorithm)
    {
        this.drainingAdaptiveAlgorithm = drainingAdaptiveAlgorithm;
    }
}
