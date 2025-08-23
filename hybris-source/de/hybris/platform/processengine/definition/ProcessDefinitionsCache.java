package de.hybris.platform.processengine.definition;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProcessDefinitionsCache
{
    private ProcessDefinitionsProvider processDefinitionsProvider;
    private volatile Map<ProcessDefinitionId, ProcessDefinition> definitions = null;
    private final ReadWriteLock definitionsLock = new ReentrantReadWriteLock();


    public void setProcessDefinitionsProvider(ProcessDefinitionsProvider processDefinitionsProvider)
    {
        this.processDefinitionsProvider = processDefinitionsProvider;
    }


    public ProcessDefinition find(ProcessDefinitionId id)
    {
        Preconditions.checkNotNull(id);
        assureCaheIsInitialized();
        if(id.isActive())
        {
            ProcessDefinitionId latestId = this.processDefinitionsProvider.getLatestDefinitionIdFor(id);
            if(latestId == null)
            {
                return executeRead(searchBy(id));
            }
            Preconditions.checkState(latestId.isHistorical());
            return findHistoricalDefinition(latestId);
        }
        return findHistoricalDefinition(id);
    }


    public void addActiveProcessDefinitions(ProcessDefinition... definitionsToAdd)
    {
        assureCaheIsInitialized();
        if(definitionsToAdd.length == 0)
        {
            return;
        }
        executeWrite((Operation<?>)new Object(this, definitionsToAdd));
    }


    public ProcessDefinition remove(ProcessDefinitionId id)
    {
        assureCaheIsInitialized();
        Preconditions.checkNotNull(id);
        return executeWrite((Operation<ProcessDefinition>)new Object(this, id));
    }


    public void clear()
    {
        assureCaheIsInitialized();
        executeWrite((Operation<?>)new Object(this));
    }


    public Set<ProcessDefinitionId> getAllIds()
    {
        assureCaheIsInitialized();
        return executeRead((Operation<Set<ProcessDefinitionId>>)new Object(this));
    }


    private ProcessDefinition findHistoricalDefinition(ProcessDefinitionId id)
    {
        ProcessDefinition fromCache = executeRead(searchBy(id));
        if(fromCache != null)
        {
            return fromCache;
        }
        return executeWrite(tryToRefreshCacheFor(id));
    }


    private Operation<ProcessDefinition> searchBy(ProcessDefinitionId id)
    {
        Preconditions.checkNotNull(id);
        return (Operation<ProcessDefinition>)new Object(this, id);
    }


    private void assureCaheIsInitialized()
    {
        if(this.definitions != null)
        {
            return;
        }
        executeWrite((Operation<?>)new Object(this));
    }


    private void loadInitialDefinitions()
    {
        for(ProcessDefinition definition : this.processDefinitionsProvider.getInitialActiveDefinitions())
        {
            addActiveProcessDefinition(definition);
        }
    }


    private void addActiveProcessDefinition(ProcessDefinition definition)
    {
        Preconditions.checkNotNull(definition);
        Preconditions.checkNotNull(definition.getName());
        ProcessDefinitionId activeId = new ProcessDefinitionId(definition.getName());
        this.definitions.put(activeId, definition);
        if(definition.getVersion() != null)
        {
            addHistoricalDefinition(definition);
        }
    }


    private void addHistoricalDefinition(ProcessDefinition definition)
    {
        Preconditions.checkNotNull(definition);
        Preconditions.checkNotNull(definition.getName());
        Preconditions.checkNotNull(definition.getVersion());
        ProcessDefinitionId id = new ProcessDefinitionId(definition.getName(), definition.getVersion());
        this.definitions.put(id, definition);
    }


    private Operation<ProcessDefinition> tryToRefreshCacheFor(ProcessDefinitionId id)
    {
        Preconditions.checkNotNull(id);
        Preconditions.checkArgument(id.isHistorical());
        return (Operation<ProcessDefinition>)new Object(this, id);
    }


    private <T> T executeWrite(Operation<T> operation)
    {
        return executeWithLock(operation, this.definitionsLock.writeLock());
    }


    private <T> T executeRead(Operation<T> operation)
    {
        return executeWithLock(operation, this.definitionsLock.readLock());
    }


    private <T> T executeWithLock(Operation<T> operation, Lock lock)
    {
        ((Lock)Objects.<Lock>requireNonNull(lock)).lock();
        try
        {
            return (T)((Operation)Objects.<Operation>requireNonNull(operation)).execute();
        }
        finally
        {
            lock.unlock();
        }
    }
}
