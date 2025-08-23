package de.hybris.platform.directpersistence.selfhealing.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.exception.ModelPersistenceException;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.DirectUpdateRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.selfhealing.ItemToHeal;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.collections.GuavaCollectors;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.threadpool.PoolableThread;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSelfHealingService implements SelfHealingService, InitializingBean, DisposableBean
{
    private static final Logger LOGGER = Logger.getLogger(DefaultSelfHealingService.class);
    public static final String CFG_SELF_HEALING_INTERVAL = "self.healing.interval";
    public static final String CFG_SELF_HEALING_BATCH_LIMIT = "self.healing.batch.limit";
    public static final String CFG_SELF_HEALING_ENABLED = "self.healing.enabled";
    public static final int DEFAULT_SELF_HEALING_INTERVAL_SECONDS = 30;
    public static final int DEFAULT_SELF_HEALING_BATCH_LIMIT = 1000;
    public static final boolean DEFAULT_SELF_HEALING_ENABLED = true;
    private Tenant myTenant;
    private WritePersistenceGateway writePersistenceGateway;
    private int batchLimit;
    private volatile boolean enabled;
    private int intervalSeconds;
    private final BlockingQueue<ItemToHeal> accumulator = new LinkedBlockingQueue<>();
    private volatile BatchSelfHealingItemsScheduledRunnable selfHealingRunnable = null;
    private volatile TestListener testListener;


    public void setTestListener(TestListener listener)
    {
        this.testListener = listener;
    }


    public void removeTestListener()
    {
        this.testListener = null;
    }


    public void addItemToHeal(ItemToHeal itemToHeal)
    {
        if(isEnabled())
        {
            Transaction current = Transaction.current();
            current.executeOnCommit((Transaction.TransactionAwareExecution)new Object(this, itemToHeal));
        }
    }


    public boolean isEnabled()
    {
        return this.enabled;
    }


    public void batchItems()
    {
        List<ItemToHeal> entitiesToPersist = Lists.newArrayList();
        this.accumulator.drainTo(entitiesToPersist, Math.min(getBatchLimit(), this.accumulator.size()));
        ImmutableList<EntityRecord> immutableList = transformItemsToEntityRecords(entitiesToPersist);
        try
        {
            flush((Collection<EntityRecord>)immutableList);
            if(this.testListener != null)
            {
                for(ItemToHeal i : entitiesToPersist)
                {
                    this.testListener.notifyDone(i);
                }
            }
        }
        catch(ModelPersistenceException e)
        {
            LOGGER.info("Some item property references in batch couldn't be repaired. Number of items in batch = " + immutableList
                            .size(), (Throwable)e);
        }
    }


    private ImmutableList<EntityRecord> transformItemsToEntityRecords(Collection<ItemToHeal> col)
    {
        return (ImmutableList<EntityRecord>)col.stream()
                        .map(i -> new DirectUpdateRecord(i.getPk(), i.getType(), i.getVersion(), createSinglePropertyHolderSet(i.getAttribute(), i.getValue())))
                        .collect(GuavaCollectors.immutableList());
    }


    private void flush(Collection<EntityRecord> records) throws ModelPersistenceException
    {
        if(CollectionUtils.isNotEmpty(records))
        {
            EntityRecordsToChangeSetAdapter entityRecordsToChangeSetAdapter = new EntityRecordsToChangeSetAdapter(records);
            this.writePersistenceGateway.persist((ChangeSet)entityRecordsToChangeSetAdapter);
        }
    }


    private Set<PropertyHolder> createSinglePropertyHolderSet(String column, Object value)
    {
        return (Set<PropertyHolder>)ImmutableSet.of(new PropertyHolder(column, value));
    }


    public void destroy() throws Exception
    {
        stopWorkerIfRunning();
    }


    public void afterPropertiesSet() throws Exception
    {
        this.myTenant = Registry.getCurrentTenantNoFallback();
        setupFromConfig();
        setupWorkerViaTenantListener();
    }


    protected void setupWorkerViaTenantListener()
    {
        Registry.registerTenantListener((TenantListener)new Object(this));
    }


    public void applyWorkerSettings()
    {
        if(isEnabled())
        {
            startWorkerIfNotRunning();
        }
        else
        {
            stopWorkerIfRunning();
        }
    }


    protected void setupFromConfig()
    {
        ConfigIntf cfg = this.myTenant.getConfig();
        setBatchLimit(cfg.getInt("self.healing.batch.limit", 1000));
        setInterval(cfg.getInt("self.healing.interval", 30));
        setEnabled(cfg.getBoolean("self.healing.enabled", true));
    }


    protected void startWorkerIfNotRunning()
    {
        if(this.selfHealingRunnable == null)
        {
            synchronized(this)
            {
                if(this.selfHealingRunnable == null)
                {
                    this.selfHealingRunnable = new BatchSelfHealingItemsScheduledRunnable(this);
                    PoolableThread thread = this.myTenant.getThreadPool().borrowThread();
                    thread.execute((Runnable)this.selfHealingRunnable);
                }
            }
        }
    }


    protected void stopWorkerIfRunning()
    {
        if(this.selfHealingRunnable != null)
        {
            synchronized(this)
            {
                if(this.selfHealingRunnable != null)
                {
                    this.accumulator.clear();
                    this.selfHealingRunnable.cancel();
                    this.selfHealingRunnable = null;
                }
            }
        }
    }


    public void setInterval(int interval)
    {
        this.intervalSeconds = interval;
    }


    public int getInterval()
    {
        return this.intervalSeconds;
    }


    @Required
    public void setWritePersistenceGateway(WritePersistenceGateway writePersistenceGateway)
    {
        this.writePersistenceGateway = writePersistenceGateway;
    }


    public void setBatchLimit(int batchLimit)
    {
        this.batchLimit = batchLimit;
    }


    public int getBatchLimit()
    {
        return this.batchLimit;
    }


    public void setEnabled(boolean enableSchedulerGlobal)
    {
        this.enabled = enableSchedulerGlobal;
    }
}
