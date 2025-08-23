package de.hybris.platform.core.threadregistry;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.suspend.SystemStatus;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ThreadRegistry
{
    private static final Logger LOG = LoggerFactory.getLogger(ThreadRegistry.class);
    private final Map<Long, OperationInfo> THREADS = new ConcurrentHashMap<>();
    private final BooleanSupplier shouldLogInDebug;
    private volatile boolean BLOCK_NOT_SUSPENDABLE_OPERATIONS = false;
    private volatile boolean update = false;


    ThreadRegistry(BooleanSupplier shouldLogInDebug)
    {
        this.shouldLogInDebug = Objects.<BooleanSupplier>requireNonNull(shouldLogInDebug);
    }


    void blockNotSuspendableOperations()
    {
        this.BLOCK_NOT_SUSPENDABLE_OPERATIONS = true;
    }


    void unblockNotSuspendableOperations()
    {
        this.BLOCK_NOT_SUSPENDABLE_OPERATIONS = false;
    }


    boolean isBlockingNotSuspendableOperations()
    {
        return this.BLOCK_NOT_SUSPENDABLE_OPERATIONS;
    }


    void register(@Nonnull OperationInfo operationInfo)
    {
        Objects.requireNonNull(operationInfo, "Thread operation info is required");
        Long threadId = getCurrentThreadId();
        String threadName = getCurrentThreadName();
        OperationInfo registeredInfo = this.THREADS.get(threadId);
        OperationInfo operationToRegister = OperationInfo.builder().withThreadId(threadId).withThreadName(threadName).withAspectNameIfPresent(System.getenv("ASPECT_NAME")).build().merge(operationInfo);
        if(registeredInfo != null)
        {
            if(registeredInfo.isJunitOperation() || operationInfo.isJunitOperation())
            {
                update(operationInfo);
                return;
            }
            throw new IllegalStateException(
                            String.format("Unable to register thread with %s. Is already registered", new Object[] {operationToRegister}));
        }
        putOperationInfo(threadId, operationToRegister);
        LOG.debug("Thread has been registered for the operation: {}", operationToRegister);
    }


    OperationInfo getCurrentOperationInfo()
    {
        Long threadId = getCurrentThreadId();
        return this.THREADS.getOrDefault(threadId, OperationInfo.empty());
    }


    private boolean checkUpdate(OperationInfo operationInfo)
    {
        return operationInfo.isInitOrUpdate();
    }


    void unregister()
    {
        Long threadId = getCurrentThreadId();
        String threadName = getCurrentThreadName();
        OperationInfo operationInfo = this.THREADS.get(threadId);
        Preconditions.checkState((operationInfo != null), "Unable to unregister thread with [id=%s name=%s]. Is not present in thread registry", threadId, threadName);
        if(operationInfo.isJunitOperation())
        {
            return;
        }
        this.THREADS.remove(threadId);
        LOG.debug("Thread has been unregistered. Operation: {}", operationInfo);
    }


    OperationInfo update(@Nonnull OperationInfo operationInfo)
    {
        Objects.requireNonNull(operationInfo, "Thread operation info is required");
        Long threadId = getCurrentThreadId();
        OperationInfo previousOperationInfo = this.THREADS.get(threadId);
        if(previousOperationInfo == null)
        {
            logAttemptToModifyInfoOnUnregisteredThread(operationInfo, threadId);
            return null;
        }
        OperationInfo operationInfoToSet = previousOperationInfo.merge(OperationInfo.builder().withThreadName(getCurrentThreadName()).build()).merge(operationInfo);
        putOperationInfo(threadId, operationInfoToSet);
        LOG.debug("Operation info changed from {} to {}.", previousOperationInfo, operationInfoToSet);
        return previousOperationInfo;
    }


    void set(@Nonnull OperationInfo operationInfo)
    {
        Objects.requireNonNull(operationInfo, "Thread operation info is required");
        Long threadId = getCurrentThreadId();
        if(!this.THREADS.containsKey(threadId))
        {
            logAttemptToModifyInfoOnUnregisteredThread(operationInfo, threadId);
            return;
        }
        putOperationInfo(threadId, operationInfo);
        LOG.debug("Operation info changed to {}.", operationInfo);
    }


    Map<Long, OperationInfo> getAllOperations()
    {
        return (Map<Long, OperationInfo>)ImmutableMap.copyOf(this.THREADS);
    }


    Set<Long> getAllNotSuspendableThreadIds()
    {
        Set<Long> result = new HashSet<>();
        this.THREADS.forEach((id, operation) -> {
            if(!operation.canBeSuspended())
            {
                result.add(id);
            }
        });
        return result;
    }


    SystemStatus getStatus()
    {
        if(isInitOrUpdate())
        {
            return SystemStatus.WAITING_FOR_UPDATE;
        }
        if(isBlockingNotSuspendableOperations())
        {
            if(getAllNotSuspendableThreadIds().isEmpty())
            {
                return SystemStatus.SUSPENDED;
            }
            return SystemStatus.WAITING_FOR_SUSPEND;
        }
        return SystemStatus.RUNNING;
    }


    boolean isThreadSuspendable(Thread thread)
    {
        OperationInfo info = this.THREADS.get(Long.valueOf(thread.getId()));
        return (info == null || info.canBeSuspended());
    }


    private void logAttemptToModifyInfoOnUnregisteredThread(OperationInfo operationInfo, Long threadId)
    {
        if(this.shouldLogInDebug.getAsBoolean())
        {
            LOG.debug("Updating operation {} on unregistered thread {}. Update won't take effect.", operationInfo, threadId);
        }
        else
        {
            LOG.warn("Updating operation {} on unregistered thread {}. Update won't take effect.", operationInfo, threadId);
        }
    }


    private void putOperationInfo(Long threadId, OperationInfo operationInfo)
    {
        Preconditions.checkArgument(operationInfo.wasCreatedForThread(threadId), "threadId doesn't match operationInfo");
        if(checkUpdate(operationInfo))
        {
            this.update = true;
        }
        else if(this.update)
        {
            OperationInfo registeredOperationInfo = this.THREADS.get(getThreadId(operationInfo));
            if(updateThread(registeredOperationInfo))
            {
                this.update = false;
            }
        }
        if(this.BLOCK_NOT_SUSPENDABLE_OPERATIONS)
        {
            OperationInfo previousInfo = this.THREADS.getOrDefault(threadId, OperationInfo.empty());
            if(previousInfo.canBeSuspended() && !operationInfo.canBeSuspended())
            {
                throw new SystemIsSuspendedException("Cannot begin not suspendable operation when system is suspended.",
                                getStatus().toString());
            }
        }
        this.THREADS.put(threadId, operationInfo);
    }


    private Object getThreadId(OperationInfo operationInfo)
    {
        return operationInfo.getAttribute(OperationInfo.StandardAttributes.THREAD_ID);
    }


    private boolean updateThread(OperationInfo registeredOperationInfo)
    {
        return (registeredOperationInfo != null && registeredOperationInfo.isInitOrUpdate());
    }


    private Long getCurrentThreadId()
    {
        return Long.valueOf(Thread.currentThread().getId());
    }


    private String getCurrentThreadName()
    {
        return Thread.currentThread().getName();
    }


    public boolean isInitOrUpdate()
    {
        return this.update;
    }


    boolean areDbFailuresHandledByCaller(Thread thread)
    {
        OperationInfo info = this.THREADS.get(Long.valueOf(thread.getId()));
        return (info != null && info.canHandleDbFailures());
    }
}
