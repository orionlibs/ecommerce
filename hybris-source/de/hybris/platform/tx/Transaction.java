package de.hybris.platform.tx;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationKey;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.TransactionAwareCache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jdbcwrapper.ConnectionImpl;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.framework.EntityInstance;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Key;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.support.JdbcUtils;

public abstract class Transaction
{
    private static final Logger LOG = Logger.getLogger(Transaction.class.getName());
    public static String CFG_ROLLBACK_ON_COMMIT_ERROR = "transaction.rollbackOnCommitError";
    static final String LOG_BEGIN_CONFIG_KEY = "transaction.monitor.begin";
    public static final String CFG_ENABLE_TX_CACHE = "transaction.local.cache.enabled";
    public static final String CFG_CONNECTION_RETRY_ON_BIND_COUNT = "transaction.connection.retryOnBind.count";
    public static boolean captureInvalidationStackTraces = false;
    private static final ThreadLocal utEnabled = (ThreadLocal)new Object();
    public static final ThreadLocal<Long> lastStartTL = new ThreadLocal<>();
    private static final ThreadLocal<TransactionFactory> currentTransactionFactory = new ThreadLocal<>();


    protected static void setTransactionFactory(TransactionFactory transactionFactory)
    {
        currentTransactionFactory.set(transactionFactory);
        Transaction current = currentTransaction.get();
        if(current != null && !current.isRunning())
        {
            currentTransaction.set(null);
        }
    }


    protected static void unsetTransactionFactory()
    {
        currentTransactionFactory.remove();
        Transaction current = currentTransaction.get();
        if(current != null && !current.isRunning())
        {
            currentTransaction.set(null);
        }
    }


    protected static TransactionFactory getTransactionsFactory()
    {
        return currentTransactionFactory.get();
    }


    private static final ThreadLocal<Transaction> currentTransaction = new ThreadLocal<>();
    private static final AtomicLong OBJECT_ID_COUNTER = new AtomicLong(0L);
    private Set<TransactionAwareExecution> onCommitExecutions = null;
    private Set<TransactionAwareExecution> onRollbackExecutions = null;
    private InvalidationSet transactionInvalidationSet = null;
    private LocalCache transactionLocalCache = null;
    private Map<PK, EntityInstance> entityInstances;
    private Map<ItemKey, Set<Item.ItemConstraint>> delayedConstraints;
    private int _openTransactionCount;
    private boolean rollbackOnly;
    private boolean invalidationPhase = false;
    private boolean currentlyCommittingOrRollbacking = false;
    private Map<Object, Object> _context = null;


    private Map<Object, Object> context(boolean forUpdate)
    {
        if(this._context == null)
        {
            if(forUpdate)
            {
                this._context = new HashMap<>();
            }
            else
            {
                return Collections.EMPTY_MAP;
            }
        }
        return this._context;
    }


    private boolean cacheActivated = true;
    private boolean localTxCacheEnabled;
    private boolean delayedStore;
    private boolean rollbackOnCommitError = false;
    Boolean addTxStackTrace = null;
    private final long objectID;
    private Tenant tenant = null;
    private AfterSaveListenerRegistry afterSaveListenerRegistry = null;
    private ConnectionImpl _txBoundConnection = null;
    private Integer txIsolationLevel = Integer.valueOf(2);
    private AfterSaveEventChangesCollector afterSaveEventCollector;
    private TransactionAwareCache localCacheAdapter;
    protected final ArrayList<Object> attachedObjects = new ArrayList();


    protected AfterSaveEventChangesCollector getEntityChangesCollector()
    {
        if(this.afterSaveEventCollector == null)
        {
            this.afterSaveEventCollector = new AfterSaveEventChangesCollector();
        }
        return this.afterSaveEventCollector;
    }


    private final Queue<Throwable> beginTransactionStack = new ConcurrentLinkedQueue<>();


    public void printContextInfo()
    {
        LOG.info("context:" + context(false).size() + " entities:" + getEntityMap(false).size() + " invalidations:" +
                        invalidations(false).recordedInvalidationsSize() + "/" + invalidations(false)
                        .effectiveInvalidationsSize());
    }


    public Queue<Throwable> getBeginTransactionStack()
    {
        return this.beginTransactionStack;
    }


    private InvalidationSet invalidations(boolean forUpdate)
    {
        if(this.transactionInvalidationSet == null)
        {
            if(forUpdate)
            {
                this
                                .transactionInvalidationSet = (InvalidationSet)new TxInvalidationSet(this, (this.tenant == null) ? InvalidationManager.getInstance() : this.tenant.getInvalidationManager());
            }
            else
            {
                return InvalidationSet.EMPTY_SET;
            }
        }
        return this.transactionInvalidationSet;
    }


    public static Transaction current()
    {
        Transaction tx = currentTransaction.get();
        if(tx == null)
        {
            tx = createNew();
            currentTransaction.set(tx);
        }
        else if(tx.isRunning() && !tx.invalidationPhase && tx._txBoundConnection == null)
        {
            tx.clear();
            currentTransaction.set(null);
            System.err.println("Found corrupted running transaction " + tx.getObjectID() + " - discarding it");
            tx = createNew();
            currentTransaction.set(tx);
        }
        return tx;
    }


    protected static Transaction getCurrentIfExists()
    {
        return currentTransaction.get();
    }


    public boolean isCurrent()
    {
        return (this == currentTransaction.get());
    }


    protected static Transaction createNew()
    {
        TransactionFactory f = currentTransactionFactory.get();
        return (f == null) ? (Transaction)new DefaultTransaction() : f.newCurrent();
    }


    public Transaction()
    {
        this.objectID = OBJECT_ID_COUNTER.getAndIncrement();
        clear();
    }


    private boolean isAddTxStackTrace()
    {
        if(this.addTxStackTrace == null)
        {
            this.addTxStackTrace = Boolean.valueOf(readAddTxStackTraceConfig());
        }
        return this.addTxStackTrace.booleanValue();
    }


    void clearAddTxStackTrace()
    {
        this.addTxStackTrace = null;
    }


    private boolean readAddTxStackTraceConfig()
    {
        try
        {
            return Config.getBoolean("transaction.monitor.begin", false);
        }
        catch(Exception e)
        {
            LOG.error("error getting config setting 'transaction.monitor.begin' (error:" + e.getMessage() + ")", e);
            return false;
        }
    }


    public long getObjectID()
    {
        return this.objectID;
    }


    private void clearAndUnsetAsCurrent()
    {
        Preconditions.checkArgument(!this.invalidationPhase);
        currentTransaction.remove();
        clear();
    }


    private void clear()
    {
        this._openTransactionCount = 0;
        this.rollbackOnly = false;
        this._context = null;
        this.transactionInvalidationSet = null;
        this.onCommitExecutions = null;
        this.onRollbackExecutions = null;
        this.delayedConstraints = null;
        this.entityInstances = null;
        this.afterSaveEventCollector = null;
        this.transactionLocalCache = null;
        this.localCacheAdapter = null;
        this.attachedObjects.clear();
    }


    public final boolean isRunning()
    {
        return (this._openTransactionCount > 0);
    }


    public boolean isNested()
    {
        return (this._openTransactionCount > 1);
    }


    public final void setRollbackOnly()
    {
        this.rollbackOnly = true;
    }


    public boolean isRollbackOnly()
    {
        return this.rollbackOnly;
    }


    protected void clearRollbackOnly()
    {
        this.rollbackOnly = false;
    }


    public static boolean isInCommitOrRollback()
    {
        Transaction tx = currentTransaction.get();
        if(tx != null)
        {
            return (tx.currentlyCommittingOrRollbacking || tx.invalidationPhase);
        }
        return false;
    }


    public int getOpenTransactionCount()
    {
        return this._openTransactionCount;
    }


    protected void increaseOpenTransactionCount()
    {
        this._openTransactionCount++;
    }


    protected void decreaseOpenTransactionCount()
    {
        this._openTransactionCount--;
    }


    protected boolean calculatedUserTAEnabled(Tenant t)
    {
        return (t.getConfig().getBoolean("transaction.activate", true) && isUserTransactionEnabled());
    }


    public void begin() throws TransactionException
    {
        logBeforeBegin();
        checkForOtherCurrentTxRunning();
        if(isRunning())
        {
            try
            {
                flushDelayedStore();
                increaseOpenTransactionCount();
                setAsCurrent();
            }
            catch(Exception e)
            {
                onNestedBeginError(e);
                throw toTransactionException(e);
            }
        }
        else
        {
            try
            {
                beginOuter();
                increaseOpenTransactionCount();
                setAsCurrent();
            }
            catch(Exception e)
            {
                onOuterBeginError(e);
                throw toTransactionException(e);
            }
        }
    }


    protected void onNestedBeginError(Exception e)
    {
        setRollbackOnly();
    }


    protected void onOuterBeginError(Exception e)
    {
        if(isCurrent())
        {
            clearAndUnsetAsCurrent();
        }
        else
        {
            clear();
        }
    }


    protected void checkForOtherCurrentTxRunning()
    {
        Transaction current = getCurrentIfExists();
        if(current != null && current != this && current.isRunning())
        {
            throw new TransactionException("Cannot begin transaction " + this + " since current transaction " + current + " is still running");
        }
    }


    protected void beginOuter()
    {
        this.tenant = Registry.getCurrentTenantNoFallback();
        if(this.tenant == null)
        {
            throw new IllegalStateException("no tenant active");
        }
        ConfigIntf ctx = this.tenant.getConfig();
        this.delayedStore = ctx.getBoolean("transaction.delayedstore", true);
        this.localTxCacheEnabled = ctx.getBoolean("transaction.local.cache.enabled", true);
        this.localCacheAdapter = null;
        loadAfterSaveListenerRegistry();
        this._txBoundConnection = bindConnection(this.tenant, calculatedUserTAEnabled(this.tenant));
    }


    protected void loadAfterSaveListenerRegistry()
    {
        ApplicationContext coreCtx = Registry.getCoreApplicationContext();
        if(coreCtx != null)
        {
            this.afterSaveListenerRegistry = (AfterSaveListenerRegistry)coreCtx.getBean("afterSaveListenerRegistry", AfterSaveListenerRegistry.class);
        }
    }


    protected AfterSaveListenerRegistry getAfterSaveEventListenerRegistry()
    {
        if(this.afterSaveListenerRegistry == null)
        {
            loadAfterSaveListenerRegistry();
        }
        return this.afterSaveListenerRegistry;
    }


    protected ConnectionImpl bindConnection(Tenant tenant, boolean userTAEnabled)
    {
        ConnectionImpl con = null;
        try
        {
            tenant.forceMasterDataSource();
            con = getConnectionToBindWithRetry(tenant.getMasterDataSource());
            if(con == null)
            {
                throw new TransactionException("Data source of tenant " + tenant.getTenantID() + " returned NULL connection.");
            }
            if(userTAEnabled)
            {
                this.rollbackOnCommitError = tenant.getConfig().getBoolean(CFG_ROLLBACK_ON_COMMIT_ERROR, false);
                con.setTxBoundUserTA(this.txIsolationLevel);
            }
            else
            {
                con.setTxBoundNoUserTA();
            }
            return con;
        }
        catch(Exception e)
        {
            if(con != null)
            {
                try
                {
                    if(userTAEnabled)
                    {
                        rollbackConnection(con);
                        con.unsetTxBound();
                    }
                }
                catch(Exception exception)
                {
                }
                finally
                {
                    JdbcUtils.closeConnection((Connection)con);
                }
            }
            throw toTransactionException(e);
        }
    }


    protected void logBeforeBegin()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("entering tx.begin of objectID=" + getObjectID() + ",opentxcount=" + getOpenTransactionCount());
        }
        addTransactionStartTrace();
    }


    protected ConnectionImpl getConnectionToBindWithRetry(HybrisDataSource dataSource) throws SQLException
    {
        int retryCount = getRetryCountFromConfig();
        ConnectionImpl connection = null;
        for(int i = 0; i <= retryCount; i++)
        {
            try
            {
                connection = getConnectionToBind(dataSource);
                return connection;
            }
            catch(SQLException e)
            {
                LOG.warn(e);
            }
        }
        return connection;
    }


    private int getRetryCountFromConfig()
    {
        int retryCount = 1;
        try
        {
            retryCount = this.tenant.getConfig().getInt("transaction.connection.retryOnBind.count", 1);
            retryCount = Math.max(retryCount, 0);
        }
        catch(Exception e)
        {
            LOG.warn(e);
        }
        return retryCount;
    }


    protected ConnectionImpl getConnectionToBind(HybrisDataSource dataSource) throws SQLException
    {
        return (ConnectionImpl)dataSource.getConnection();
    }


    private void addTransactionStartTrace()
    {
        if(isAddTxStackTrace())
        {
            this.beginTransactionStack.offer(new RuntimeException("Stale transaction marker (" + this + ")"));
        }
    }


    private void removeTransactionStartTrail()
    {
        if(isAddTxStackTrace())
        {
            this.beginTransactionStack.poll();
        }
    }


    public void commit() throws TransactionException
    {
        logBeforeCommit();
        checkBeforeCommit();
        if(isRollbackOnly())
        {
            boolean wasOuter = !isNested();
            rollback();
            if(wasOuter)
            {
                throw new RollbackOnlyException("transaction rolled back because it has been marked as rollback-only");
            }
        }
        else if(isNested())
        {
            try
            {
                flushDelayedStore();
                decreaseOpenTransactionCount();
            }
            catch(Exception e)
            {
                rollback();
                throw toTransactionException(e);
            }
        }
        else
        {
            try
            {
                checkDelayedConstrains();
                flushDelayedStore();
                notifyAttachedObjectsBeforeCommit();
            }
            catch(Exception e)
            {
                rollback();
                throw toTransactionException(e);
            }
            commitConnectionAndClearAndUnsetAsCurrent();
        }
    }


    protected void commitConnectionAndClearAndUnsetAsCurrent()
    {
        Preconditions.checkArgument(!this.currentlyCommittingOrRollbacking, "commit called but there is currently a commit/rollback running");
        try
        {
            this.currentlyCommittingOrRollbacking = true;
            boolean commitSuccess = false;
            try
            {
                commitConnection();
                commitSuccess = true;
            }
            catch(Exception e)
            {
                throw toTransactionException(e);
            }
            finally
            {
                clearTxBoundConnectionAndNotify(commitSuccess);
            }
        }
        finally
        {
            this.currentlyCommittingOrRollbacking = false;
        }
    }


    protected void checkBeforeCommit()
    {
        if(!isCurrent())
        {
            throw new TransactionException("Cannot commit transaction " + this + " since it's not current transaction");
        }
        if(!isRunning())
        {
            throw new IllegalTransactionStateException("Commit called but transaction isn't running.");
        }
    }


    protected void logBeforeCommit()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("entering tx.commit of objectID=" + getObjectID() + ",opentxcount=" + getOpenTransactionCount());
        }
        removeTransactionStartTrail();
    }


    private final void commitConnection() throws SQLException
    {
        if(this._txBoundConnection != null && this._txBoundConnection.isTxBoundUserTA())
        {
            try
            {
                this._txBoundConnection.commit();
            }
            catch(Exception e)
            {
                if(isRollbackOnCommitError())
                {
                    rollbackAfterCommitError();
                }
                throw toSQLException(e);
            }
        }
    }


    private SQLException toSQLException(Throwable e)
    {
        if(e instanceof SQLException)
        {
            return (SQLException)e;
        }
        if(e instanceof RuntimeException)
        {
            throw (RuntimeException)e;
        }
        if(e instanceof Error)
        {
            throw (Error)e;
        }
        return new SQLException(e);
    }


    private TransactionException toTransactionException(Throwable e)
    {
        if(e instanceof TransactionException)
        {
            return (TransactionException)e;
        }
        if(e instanceof ConsistencyCheckException)
        {
            ConsistencyCheckException e1 = (ConsistencyCheckException)e;
            return new TransactionException(e, "Transaction error due to consistency error " + e1.getMessage(), e1
                            .getErrorCode());
        }
        if(e instanceof RuntimeException)
        {
            throw (RuntimeException)e;
        }
        if(e instanceof Error)
        {
            throw (Error)e;
        }
        return new TransactionException(e);
    }


    private Exception toException(Throwable e)
    {
        if(e instanceof RuntimeException)
        {
            throw (RuntimeException)e;
        }
        if(e instanceof Error)
        {
            throw (Error)e;
        }
        if(e instanceof Exception)
        {
            return (Exception)e;
        }
        return new RuntimeException(e);
    }


    public static <ET extends Exception> ET toException(Throwable e, Class<ET> businessExceptionClass)
    {
        if(businessExceptionClass.isAssignableFrom(e.getClass()))
        {
            return (ET)e;
        }
        if(e instanceof RuntimeException)
        {
            throw (RuntimeException)e;
        }
        if(e instanceof Error)
        {
            throw (Error)e;
        }
        throw new RuntimeException(e);
    }


    private void rollbackAfterCommitError()
    {
        try
        {
            this._txBoundConnection.rollback();
        }
        catch(Exception ex)
        {
            LOG.error("exception on rollback after commit exception", ex);
        }
    }


    protected void clearTxBoundConnectionAndNotifyCommit()
    {
        clearTxBoundConnectionAndNotify(true);
    }


    protected void clearTxBoundConnectionAndNotifyRollback()
    {
        clearTxBoundConnectionAndNotify(false);
    }


    protected void clearTxBoundConnectionAndNotify(boolean isCommit)
    {
        try
        {
            if(this._txBoundConnection != null)
            {
                unsetTxBoundConnection();
            }
        }
        finally
        {
            JdbcUtils.closeConnection((Connection)this._txBoundConnection);
            this._txBoundConnection = null;
            this.txIsolationLevel = null;
            if(isCommit)
            {
                notifyCommit();
            }
            else
            {
                notifyRollback();
            }
        }
    }


    protected void unsetTxBoundConnection()
    {
        this._txBoundConnection.unsetTxBound();
    }


    protected void checkDelayedConstrains() throws ConsistencyCheckException
    {
        Map<ItemKey, Set<Item.ItemConstraint>> _delayedConstraints = this.delayedConstraints;
        if(_delayedConstraints != null)
        {
            synchronized(this)
            {
                _delayedConstraints = this.delayedConstraints;
                if(_delayedConstraints != null)
                {
                    try
                    {
                        for(Map.Entry<ItemKey, Set<Item.ItemConstraint>> entry : _delayedConstraints.entrySet())
                        {
                            Item i = ((ItemKey)entry.getKey()).theItem;
                            for(Item.ItemConstraint constr : entry.getValue())
                            {
                                constr.assertConstraint(i);
                            }
                        }
                    }
                    finally
                    {
                        this.delayedConstraints = null;
                    }
                }
            }
        }
    }


    public void invalidateAndNotifyCommit(Object[] key, int invalidationDepth, int invalidationType)
    {
        invalidations(true).addInvalidation(key, invalidationDepth, convertInvalidationType(invalidationType));
        if(InvalidationKey.startsWithBaseEntityArrayKey(key))
        {
            collectAfterSaveEvent(key, invalidationType);
        }
        notifyCommit();
    }


    public void notifyCommit()
    {
        try
        {
            if(!this.invalidationPhase)
            {
                this.invalidationPhase = true;
                invalidations(false).executeDelayedInvalidationsGlobally();
            }
        }
        finally
        {
            this.invalidationPhase = false;
            Set<TransactionAwareExecution> delayedExecutions = getDelayedOnCommitExecutions();
            AfterSaveEventChangesCollector collector = getEntityChangesCollector();
            AfterSaveListenerRegistry registry = getAfterSaveEventListenerRegistry();
            clearAndUnsetAsCurrent();
            executeAllDelayedTransactionExecutions(delayedExecutions);
            notifyAfterSaveListeners(registry, collector);
        }
    }


    private Set<TransactionAwareExecution> getDelayedOnCommitExecutions()
    {
        return (this.onCommitExecutions == null) ? Collections.<TransactionAwareExecution>emptySet() : this.onCommitExecutions;
    }


    private void notifyAfterSaveListeners(AfterSaveListenerRegistry registry, AfterSaveEventChangesCollector collector)
    {
        byte[][] changes = collector.getEncodedChanges();
        if(registry != null && changes != null)
        {
            registry.publishChanges(changes);
        }
    }


    public void rollback() throws TransactionException
    {
        logBeforeRollback();
        checkBeforeRollback();
        if(isNested())
        {
            try
            {
                setRollbackOnly();
            }
            finally
            {
                decreaseOpenTransactionCount();
            }
        }
        else
        {
            try
            {
                notifyAttachedObjectsBeforeRollback();
                rollbackOuter();
            }
            finally
            {
                decreaseOpenTransactionCount();
                clearAndUnsetAsCurrent();
            }
        }
    }


    protected void checkBeforeRollback()
    {
        if(!isCurrent())
        {
            throw new TransactionException("Cannot rollback transaction " + this + " since it's not current transaction");
        }
        if(!isRunning())
        {
            throw new IllegalTransactionStateException("Rollback called but transaction isn't running.");
        }
    }


    protected void logBeforeRollback()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("entering tx.rollback of objectID=" + getObjectID() + ",opentxcount=" + getOpenTransactionCount());
        }
        removeTransactionStartTrail();
    }


    protected void rollbackOuter()
    {
        try
        {
            Preconditions.checkArgument(!this.currentlyCommittingOrRollbacking, "rollback called but there is currently a commit/rollback running");
            this.currentlyCommittingOrRollbacking = true;
            if(this._txBoundConnection != null && !this._txBoundConnection.isTxBoundUserTA())
            {
                LOG.warn("Transaction was running in fake mode (via transaction.activate=false or Transaction.enableUserTransactionForThread(false) and therefore rollback has no effect");
            }
            rollbackConnection();
        }
        catch(SQLException e)
        {
            throw toTransactionException(e);
        }
        finally
        {
            clearTxBoundConnectionAndNotifyRollback();
            this.currentlyCommittingOrRollbacking = false;
        }
    }


    protected void rollbackConnection() throws SQLException
    {
        rollbackConnection(this._txBoundConnection);
    }


    protected void rollbackConnection(ConnectionImpl con) throws SQLException
    {
        if(con != null && con.isTxBoundUserTA())
        {
            con.rollback();
        }
    }


    public void invalidateAndNotifyRollback(Object[] key, int invalidationDepth, int invalidationType)
    {
        invalidations(true).addInvalidation(key, invalidationDepth, convertInvalidationType(invalidationType));
        notifyRollback();
    }


    public void notifyRollback()
    {
        try
        {
            if(!this.invalidationPhase)
            {
                this.invalidationPhase = true;
                invalidations(false).executeDelayedRollbackInvalidationsLocally();
                invalidations(false).executeDelayedInvalidationsLocally();
            }
        }
        finally
        {
            this.invalidationPhase = false;
            Set<TransactionAwareExecution> delayedExecutions = getDelayedOnRollbackExecutions();
            clearAndUnsetAsCurrent();
            executeAllDelayedTransactionExecutions(delayedExecutions);
        }
    }


    private Set<TransactionAwareExecution> getDelayedOnRollbackExecutions()
    {
        return (this.onRollbackExecutions == null) ? Collections.<TransactionAwareExecution>emptySet() : this.onRollbackExecutions;
    }


    public Object execute(TransactionBody transactionBody) throws Exception
    {
        return execute(transactionBody, (Class<? extends Exception>[])null);
    }


    public <T> T execute(TransactionBody.GenericTransactionBody<T> transactionBody) throws Exception
    {
        return execute(transactionBody, (Class<? extends Exception>[])null);
    }


    public <T> T execute(TransactionBody.GenericTransactionBody<T> transactionBody, Class<? extends Exception>... permittedExceptions) throws Exception
    {
        return (T)execute(wrapGenericTransactionBody(transactionBody), permittedExceptions);
    }


    private TransactionBody wrapGenericTransactionBody(TransactionBody.GenericTransactionBody<?> transactionBody)
    {
        return (TransactionBody)new Object(this, transactionBody);
    }


    public Object execute(TransactionBody transactionBody, Class<? extends Exception>... permittedExceptions) throws Exception
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("entering tx.execute(body) of objectID#" + getObjectID());
        }
        begin();
        Throwable thrown = null;
        try
        {
            return transactionBody.execute();
        }
        catch(Throwable e)
        {
            thrown = e;
            throw toException(e);
        }
        finally
        {
            finishExecute(thrown, transactionBody, permittedExceptions);
        }
    }


    protected void finishExecute(Throwable thrown, TransactionBody body, Class<? extends Exception>... permittedExceptions) throws Exception
    {
        if(isRunning())
        {
            if(thrown == null || isExceptionIsPermitted(thrown, permittedExceptions))
            {
                try
                {
                    commit();
                }
                catch(Exception ex)
                {
                    if(thrown == null)
                    {
                        throw ex;
                    }
                    LOG.error("error committing transaction (after permitted exception): " + ex.getMessage(), ex);
                }
            }
            else
            {
                try
                {
                    rollback();
                }
                catch(Exception e)
                {
                    LOG.error("error rolling back transaction: " + e.getMessage(), e);
                }
            }
        }
        else
        {
            LOG.error("Transaction " + this + " is no longer running after performing " + body + " but before actual commit/rollback phase");
        }
    }


    protected boolean isExceptionIsPermitted(Throwable thrown, Class<? extends Exception>... permittedExceptions)
    {
        if(permittedExceptions != null)
        {
            for(Class<? extends Exception> clEx : permittedExceptions)
            {
                if(clEx != null && clEx.isInstance(thrown))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public Object getContextEntry(Object key)
    {
        return context(false).get(key);
    }


    public void setContextEntry(Object key, Object value)
    {
        context(true).put(key, value);
    }


    public void invalidate(AbstractCacheUnit unit, int invalidationType)
    {
        invalidate(unit, invalidationType, false);
    }


    public void invalidate(AbstractCacheUnit unit, int invalidationType, boolean sendImmediately)
    {
        Object[] key = unit.getKeyAsArray();
        invalidate(key, unit.getInvalidationTopicDepth(), invalidationType, sendImmediately);
    }


    public void invalidate(Object[] key, int invalidationTopicDepth, int invalidationType)
    {
        invalidate(key, invalidationTopicDepth, convertInvalidationType(invalidationType), false);
        if(InvalidationKey.startsWithBaseEntityArrayKey(key))
        {
            collectAfterSaveEvent(key, invalidationType);
        }
    }


    public void invalidateFromDirectPersistence(Object[] key, PK pk, int invalidationType)
    {
        invalidate(key, 3, convertInvalidationType(invalidationType), false);
        if(InvalidationKey.startsWithBaseEntityArrayKey(key))
        {
            collectAfterSaveEvent(key, invalidationType);
            removeFromTxContext(pk);
            removeFromEntityMap(pk);
        }
    }


    protected void removeFromEntityMap(PK pk)
    {
        EntityInstance instance = getEntityMap(false).remove(pk);
        if(instance != null && instance.needsStoring())
        {
            throw new IllegalStateException("Jalo item could not be modified because equivalent model was already modified in model service.");
        }
    }


    private void removeFromTxContext(PK pk)
    {
        context(false).remove(Key.get(pk, Boolean.FALSE));
        context(false).remove(Key.get(pk, Boolean.TRUE));
    }


    private int convertInvalidationType(int invalidationType)
    {
        switch(invalidationType)
        {
            case 4:
                return 1;
            case 1:
            case 2:
                return invalidationType;
        }
        throw new IllegalArgumentException("illegal invalidation type " + invalidationType);
    }


    private void collectAfterSaveEvent(Object[] key, int invalidationType)
    {
        int type;
        switch(invalidationType)
        {
            case 2:
                type = 2;
                break;
            case 1:
                type = 1;
                break;
            case 4:
                type = 4;
                break;
            default:
                throw new RuntimeException("wrong invalidation type");
        }
        getEntityChangesCollector().collect((PK)key[3], type);
    }


    public void invalidate(Object[] key, int invalidationTopicDepth, int invalidationType, boolean sendImmediately)
    {
        if(isRunning())
        {
            invalidateInTx(key, invalidationTopicDepth, invalidationType, sendImmediately);
        }
        else
        {
            invalidateOutsideTx(key, invalidationTopicDepth, invalidationType);
        }
    }


    private void invalidateOutsideTx(Object[] key, int invalidationTopicDepth, int invalidationType)
    {
        InvalidationSet.Invalidation inv = InvalidationSet.createInvalidation(key, invalidationTopicDepth, invalidationType);
        InvalidationSet.invalidateGlobally(inv);
    }


    private void invalidateInTx(Object[] key, int invalidationTopicDepth, int invalidationType, boolean sendImmediately)
    {
        performSpecialInvalidationActions(key, invalidationTopicDepth, invalidationType);
        if(this.invalidationPhase)
        {
            throw new RuntimeException("received external invalidation in invalidation processing phase");
        }
        InvalidationSet.Invalidation inv = invalidations(true).delayInvalidation(key, invalidationTopicDepth, invalidationType);
        if(sendImmediately)
        {
            InvalidationSet.invalidateGlobally(inv);
        }
    }


    private void performSpecialInvalidationActions(Object[] key, int invalidationTopicDepth, int invalidationType)
    {
        if(InvalidationKey.startsWithBaseEntityArrayKey(key))
        {
            PK itemPK = (PK)key[3];
            try
            {
                Key contextMapKeyOnModification = Key.get(itemPK, Boolean.FALSE);
                context(false).remove(contextMapKeyOnModification);
                if(invalidationType == 2)
                {
                    Key contextMapKeyOnRemoval = Key.get(itemPK, Boolean.TRUE);
                    context(false).remove(contextMapKeyOnRemoval);
                    clearDelayedConstrains(itemPK);
                }
            }
            finally
            {
                Key.clear();
            }
        }
    }


    public void addToDelayedRollbackLocalInvalidations(Object[] key, int type, int topicdepth)
    {
        invalidations(true).delayRollbackInvalidation(key, topicdepth, type);
    }


    public final boolean useCache(AbstractCacheUnit cache)
    {
        return (this.cacheActivated && useCacheInternal(cache));
    }


    public void activateCache(boolean activate)
    {
        this.cacheActivated = activate;
    }


    public boolean isInvalidated(Object[] key)
    {
        return invalidations(false).isInvalidated(key);
    }


    public void enableDelayedStore(boolean delay)
    {
        if(!delay)
        {
            flushDelayedStore();
        }
        this.delayedStore = delay;
    }


    public void setRollbackOnCommitError(boolean rollback)
    {
        this.rollbackOnCommitError = rollback;
    }


    public boolean isRollbackOnCommitError()
    {
        return this.rollbackOnCommitError;
    }


    public boolean isDelayedStoreEnabled()
    {
        return this.delayedStore;
    }


    public void flushDelayedStore()
    {
        if(!isRollbackOnly())
        {
            executeEJBModifications(null);
        }
    }


    protected void flushDelayedStore(EntityInstance entity)
    {
        executeEntityStore(entity);
    }


    public void executeOrDelayStore(EntityInstance entity)
    {
        if(!isDelayedStoreEnabled())
        {
            executeEntityStore(entity);
        }
    }


    public void addToDelayedRemoval(EntityInstance entity)
    {
        PK thePK = entity.getEntityContext().getPK();
        entity.ejbRemove();
        getEntityMap(false).remove(thePK);
    }


    private void executeEJBModifications(Set<Integer> permittedTypeCodes)
    {
        Map<PK, EntityInstance> entityMap = getEntityMap(false);
        if(MapUtils.isNotEmpty(entityMap))
        {
            List<EntityInstance> toStore = null;
            do
            {
                toStore = getModifiedInstances(entityMap, permittedTypeCodes);
                Collections.sort(toStore, MODIFIED_INSTANCES_COMP);
                for(EntityInstance entityInstance : toStore)
                {
                    executeEntityStore(entityInstance);
                }
            }
            while(!toStore.isEmpty());
        }
    }


    private static final Comparator<EntityInstance> MODIFIED_INSTANCES_COMP = (Comparator<EntityInstance>)new Object();


    private List<EntityInstance> getModifiedInstances(Map<PK, EntityInstance> entityMap, Set<Integer> permittedTypeCodes)
    {
        List<EntityInstance> toStore = new ArrayList<>(entityMap.size());
        boolean allTypes = CollectionUtils.isEmpty(permittedTypeCodes);
        for(Map.Entry<PK, EntityInstance> entry : entityMap.entrySet())
        {
            EntityInstance entityInstance = entry.getValue();
            PK pk = entityInstance.getEntityContext().getPK();
            if(entityInstance.needsStoring() && (allTypes || permittedTypeCodes
                            .contains(Integer.valueOf(pk.getTypeCode()))) &&
                            !Item.isCurrentlyRemoving(pk))
            {
                toStore.add(entityInstance);
            }
        }
        return toStore;
    }


    private static void executeEntityStore(EntityInstance entityInstance)
    {
        PK pk = entityInstance.getEntityContext().getPK();
        if(!Item.isCurrentlyRemoving(pk))
        {
            entityInstance.ejbStore();
        }
    }


    public void executeOnRollback(TransactionAwareExecution ex)
    {
        if(isRunning())
        {
            if(this.onRollbackExecutions == null)
            {
                this.onRollbackExecutions = new LinkedHashSet<>();
            }
            this.onRollbackExecutions.add(ex);
        }
    }


    public boolean executeOnCommit(TransactionAwareExecution ex)
    {
        if(isRunning())
        {
            if(this.onCommitExecutions == null)
            {
                this.onCommitExecutions = new LinkedHashSet<>();
            }
            this.onCommitExecutions.add(ex);
            return true;
        }
        executeTxAwareInternal(ex);
        return false;
    }


    private void executeTxAwareInternal(TransactionAwareExecution ex)
    {
        try
        {
            ex.execute(this);
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
    }


    private void executeAllDelayedTransactionExecutions(Set<TransactionAwareExecution> executions)
    {
        for(TransactionAwareExecution e : executions)
        {
            executeTxAwareInternal(e);
        }
    }


    public static void enableUserTransactionForThread(boolean enable)
    {
        utEnabled.set(Boolean.valueOf(enable));
    }


    public static boolean isUserTransactionEnabled()
    {
        return !Boolean.FALSE.equals(utEnabled.get());
    }


    public void activateAsCurrentTransaction()
    {
        assertNoCurrentTransactionRunning();
        setAsCurrent();
    }


    protected void assertNoCurrentTransactionRunning()
    {
        Transaction current = current();
        if(current != null && current.isRunning())
        {
            throw new IllegalStateException("there is still a current transaction running - cannot activate " + this + " as current");
        }
    }


    protected void setAsCurrent()
    {
        currentTransaction.set(this);
    }


    private Map<PK, EntityInstance> getEntityMap(boolean forWriting)
    {
        if(this.entityInstances == null)
        {
            if(forWriting)
            {
                this.entityInstances = new HashMap<>(50);
            }
            else
            {
                return Collections.EMPTY_MAP;
            }
        }
        return this.entityInstances;
    }


    public void registerEntityInstance(EntityInstance instance)
    {
        Preconditions.checkArgument(isRunning(), "cannot register entity instance for non-running tx");
        EntityInstance old = getEntityMap(true).put(instance.getEntityContext().getPK(), instance);
        Preconditions.checkArgument((old == null), "there was already an entity instance registered for the given PK");
    }


    public EntityInstance getOrLoadTxBoundEntityInstance(PersistencePool pool, String jndi, PK pk)
    {
        Preconditions.checkArgument(isRunning());
        Map<PK, EntityInstance> entityMap = getEntityMap(true);
        EntityInstance entity = entityMap.get(pk);
        if(entity == null)
        {
            entity = pool.createEntityInstance(jndi, pk);
            entity.ejbLoad();
            entityMap.put(pk, entity);
        }
        return entity;
    }


    public EntityInstance getAttachedEntityInstance(PK pk)
    {
        return getEntityMap(false).get(pk);
    }


    public void reloadEntityInstance(PK pk)
    {
        EntityInstance entity = getAttachedEntityInstance(pk);
        if(entity != null)
        {
            flushDelayedStore(entity);
            entity.ejbLoad();
        }
    }


    public ConnectionImpl getTXBoundConnection()
    {
        if(!isRunning())
        {
            throw new IllegalStateException("transaction is not running");
        }
        if(this.invalidationPhase)
        {
            throw new IllegalStateException("The transaction is performing invalidatins after commit / rollback. You cannot access the database in this phase.");
        }
        return this._txBoundConnection;
    }


    public void setTransactionIsolationLevel(int level)
    {
        if(isRunning())
        {
            try
            {
                getTXBoundConnection().setTransactionIsolation(level);
            }
            catch(SQLException e)
            {
                throw new JaloSystemException(e);
            }
        }
        this.txIsolationLevel = Integer.valueOf(level);
    }


    public static Transaction performCommitBeginEvery(Transaction existingTransaction, int ms)
    {
        Long lastStartL = lastStartTL.get();
        long lastStart = (lastStartL == null) ? 0L : lastStartL.longValue();
        if(System.currentTimeMillis() > lastStart + ms)
        {
            existingTransaction.commit();
            Transaction newTransaction = current();
            newTransaction.begin();
            return newTransaction;
        }
        return existingTransaction;
    }


    public void clearDelayedConstrains(PK itemPK)
    {
        if(this.delayedConstraints != null)
        {
            this.delayedConstraints.remove(new ItemKey(itemPK));
        }
    }


    public void addDelayedConstraint(Item item, Item.ItemConstraint constr)
    {
        Set<Item.ItemConstraint> constraints;
        ItemKey key = new ItemKey(item);
        Map<ItemKey, Set<Item.ItemConstraint>> localDdelayedConstraints = this.delayedConstraints;
        if(localDdelayedConstraints == null)
        {
            synchronized(this)
            {
                localDdelayedConstraints = this.delayedConstraints;
                if(localDdelayedConstraints == null)
                {
                    localDdelayedConstraints = new LinkedHashMap<>();
                    localDdelayedConstraints.put(key, constraints = new LinkedHashSet<>());
                    this.delayedConstraints = localDdelayedConstraints;
                }
                else
                {
                    constraints = this.delayedConstraints.get(key);
                    if(constraints == null)
                    {
                        this.delayedConstraints.put(key, constraints = new LinkedHashSet<>());
                    }
                }
            }
        }
        else
        {
            constraints = this.delayedConstraints.get(key);
            if(constraints == null)
            {
                this.delayedConstraints.put(key, constraints = new LinkedHashSet<>());
            }
        }
        constraints.add(constr);
    }


    public void lock(Item item) throws IllegalStateException
    {
        if(!isRunning())
        {
            throw new IllegalStateException("Attempted to acquire a lock on an entity outside of a transaction. Make certain to call transaction.begin() before any lock acquisition attempts are made.");
        }
        if(Config.isHSQLDBUsed())
        {
            throw new UnsupportedOperationException("HSQLDB doesn't support explicit row locking");
        }
        if(item == null)
        {
            throw new NullPointerException("item was null");
        }
        PK primaryKey = item.getPK();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            String table = this.tenant.getPersistenceManager().getItemDeployment(primaryKey.getTypeCode()).getDatabaseTableName();
            statement = getTXBoundConnection().prepareStatement(Config.isSQLServerUsed() ? ("SELECT PK FROM " +
                            table + " WITH (UPDLOCK,ROWLOCK) WHERE PK = ?") : ("SELECT PK FROM " +
                            table + " WHERE PK = ? FOR UPDATE"));
            statement.setLong(1, primaryKey.getLongValue());
            resultSet = statement.executeQuery();
            if(!resultSet.next())
            {
                throw new IllegalStateException("No entity exists with the given primary key [" + primaryKey + "]");
            }
            long fetched = resultSet.getLong(1);
            Preconditions.checkArgument((fetched == primaryKey.getLongValue()));
            reloadEntityInstance(primaryKey);
            Utilities.invalidateCache(this, this.tenant.getCache(), primaryKey, true);
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(null, statement, resultSet, true);
        }
    }


    public TransactionAwareCache getTransactionAwareCache(Cache globalCache)
    {
        if(!isRunning())
        {
            return getOrCreateGlobalCacheAdapter(globalCache);
        }
        if(isTxCacheEnabled())
        {
            return getOrCreateTxLocalCacheAdapter(globalCache);
        }
        return getOrCreateLegacyCacheAdapter(globalCache);
    }


    protected TransactionAwareCache getOrCreateGlobalCacheAdapter(Cache globalCache)
    {
        if(!(this.localCacheAdapter instanceof GlobalCacheAdapter))
        {
            this.localCacheAdapter = (TransactionAwareCache)new GlobalCacheAdapter(this, globalCache);
        }
        return this.localCacheAdapter;
    }


    protected TransactionAwareCache getOrCreateTxLocalCacheAdapter(Cache globalCache)
    {
        if(!(this.localCacheAdapter instanceof DefaultTransactionAwareCache))
        {
            this.localCacheAdapter = (TransactionAwareCache)new DefaultTransactionAwareCache(this, globalCache);
        }
        return this.localCacheAdapter;
    }


    protected TransactionAwareCache getOrCreateLegacyCacheAdapter(Cache globalCache)
    {
        if(!(this.localCacheAdapter instanceof LegacyTxCacheAdapter))
        {
            this.localCacheAdapter = (TransactionAwareCache)new LegacyTxCacheAdapter(this, globalCache);
        }
        return this.localCacheAdapter;
    }


    public boolean isTxCacheEnabled()
    {
        return this.localTxCacheEnabled;
    }


    public void enableTxCache(boolean enable)
    {
        if(this.localTxCacheEnabled != enable)
        {
            this.localTxCacheEnabled = enable;
            this.localCacheAdapter = null;
            if(!enable)
            {
                this.transactionLocalCache = null;
            }
        }
    }


    private LocalCache localCache(boolean forUpdate)
    {
        if(this.transactionLocalCache != null)
        {
            return this.transactionLocalCache;
        }
        if(!forUpdate)
        {
            this.transactionLocalCache = (LocalCache)new DefaultLocalCache();
            return this.transactionLocalCache;
        }
        return LocalCache.empty();
    }


    public void attach(Object objectToAttach)
    {
        Objects.requireNonNull(objectToAttach, "objectToAttach mustn't be null.");
        this.attachedObjects.forEach(o -> {
            if(o == objectToAttach)
            {
                throw new IllegalArgumentException("object is already attached");
            }
            if(objectToAttach.getClass().isInstance(o))
            {
                throw new IllegalArgumentException("Instance of '" + objectToAttach.getClass().getSimpleName() + "' has been already registered.");
            }
        });
        this.attachedObjects.add(objectToAttach);
    }


    public boolean dettach(Object objectToAttach)
    {
        Objects.requireNonNull(objectToAttach, "objectToAttach mustn't be null.");
        return this.attachedObjects.removeIf(o -> (o == objectToAttach || objectToAttach.getClass().isInstance(o)));
    }


    public <T> T getAttached(Class<T> classOfAttachedObject)
    {
        Objects.requireNonNull(classOfAttachedObject);
        Objects.requireNonNull(classOfAttachedObject);
        return this.attachedObjects.stream().filter(classOfAttachedObject::isInstance).map(classOfAttachedObject::cast)
                        .findFirst()
                        .orElse(null);
    }


    private void notifyAttachedObjectsBeforeCommit()
    {
        Objects.requireNonNull(BeforeCommitNotification.class);
        Objects.requireNonNull(BeforeCommitNotification.class);
        this.attachedObjects.stream().filter(BeforeCommitNotification.class::isInstance).map(BeforeCommitNotification.class::cast)
                        .forEach(BeforeCommitNotification::beforeCommit);
    }


    private void notifyAttachedObjectsBeforeRollback()
    {
        Objects.requireNonNull(BeforeRollbackNotification.class);
        Objects.requireNonNull(BeforeRollbackNotification.class);
        this.attachedObjects.stream().filter(BeforeRollbackNotification.class::isInstance).map(BeforeRollbackNotification.class::cast)
                        .forEach(BeforeRollbackNotification::beforeRollback);
    }


    protected abstract boolean useCacheInternal(AbstractCacheUnit paramAbstractCacheUnit);
}
