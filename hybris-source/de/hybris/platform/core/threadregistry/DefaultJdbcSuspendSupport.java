package de.hybris.platform.core.threadregistry;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.suspend.SystemStatus;
import de.hybris.platform.jdbcwrapper.JdbcSuspendSupport;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DefaultJdbcSuspendSupport implements JdbcSuspendSupport
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultJdbcSuspendSupport.class);
    private final ThreadRegistry threadRegistry;
    private final LoadingCache<Long, Boolean> logMemo = CacheBuilder.newBuilder().expireAfterAccess(2L, TimeUnit.SECONDS)
                    .build(CacheLoader.from(() -> Boolean.valueOf(true)));


    DefaultJdbcSuspendSupport(ThreadRegistry threadRegistry)
    {
        this.threadRegistry = Objects.<ThreadRegistry>requireNonNull(threadRegistry);
    }


    public void aboutToBorrowTheConnection()
    {
        SystemStatus systemStatus = this.threadRegistry.getStatus();
        if(systemStatus.isRunningOrWaitingForUpdate())
        {
            return;
        }
        if(!this.threadRegistry.isThreadSuspendable(Thread.currentThread()))
        {
            logInfoIfNeeded(String.format("System is %s, but connection will be returned to allow not suspendable operation to be finished.", new Object[] {systemStatus}));
            return;
        }
        if(this.threadRegistry.areDbFailuresHandledByCaller(Thread.currentThread()))
        {
            logInfoIfNeeded(String.format("System is %s, but connection will be returned to allow operation which can handle db failures to be finished.", new Object[] {systemStatus}));
            return;
        }
        String msg = String.format("System is %s. Connection cannot be borrowed.", new Object[] {systemStatus});
        logInfoIfNeeded(msg);
        throw new SystemIsSuspendedException(msg, this.threadRegistry.getStatus().toString());
    }


    private void logInfoIfNeeded(String message)
    {
        if(this.logMemo.getIfPresent(Long.valueOf(Thread.currentThread().getId())) == null)
        {
            this.logMemo.refresh(Long.valueOf(Thread.currentThread().getId()));
            LOG.info(message);
        }
    }
}
