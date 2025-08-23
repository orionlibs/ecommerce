package de.hybris.platform.ruleengine.init.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.init.ConcurrentMapFactory;
import de.hybris.platform.ruleengine.init.RuleEngineContainerRegistry;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.PostConstruct;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineContainerRegistry implements RuleEngineContainerRegistry<ReleaseId, KieContainer>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngineContainerRegistry.class);
    private Map<ReleaseId, KieContainer> kieContainerMap;
    private ConcurrentMapFactory concurrentMapFactory;
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = this.readWriteLock.readLock();
    private final Lock writeLock = this.readWriteLock.writeLock();
    private boolean readLockEnabled = false;
    private boolean keepOnlyOneContainerVersion = true;


    public void setActiveContainer(ReleaseId releaseId, KieContainer rulesContainer)
    {
        this.kieContainerMap.put(releaseId, rulesContainer);
        if(isKeepOnlyOneContainerVersion())
        {
            removeAllPreviousVersions(releaseId);
        }
    }


    public KieContainer getActiveContainer(ReleaseId releaseId)
    {
        return this.kieContainerMap.get(releaseId);
    }


    public Optional<ReleaseId> lookupForDeployedRelease(String... releaseTokens)
    {
        Preconditions.checkArgument(Objects.nonNull(releaseTokens), "Lookup release tokens should be provided");
        if(releaseTokens.length == 2)
        {
            return this.kieContainerMap.keySet().stream()
                            .filter(rid -> (rid.getGroupId().equals(releaseTokens[0]) && rid.getArtifactId().equals(releaseTokens[1])))
                            .findFirst();
        }
        return Optional.empty();
    }


    public KieContainer removeActiveContainer(ReleaseId releaseHolder)
    {
        return this.kieContainerMap.remove(releaseHolder);
    }


    public void lockReadingRegistry()
    {
        if(isReadLockEnabled())
        {
            this.readLock.lock();
        }
    }


    public void unlockReadingRegistry()
    {
        if(isReadLockEnabled())
        {
            this.readLock.unlock();
        }
    }


    public void lockWritingRegistry()
    {
        this.writeLock.lock();
    }


    public void unlockWritingRegistry()
    {
        this.writeLock.unlock();
    }


    public boolean isLockedForReading()
    {
        return (this.readWriteLock.getReadLockCount() > 0);
    }


    public boolean isLockedForWriting()
    {
        return this.readWriteLock.isWriteLocked();
    }


    @PostConstruct
    public void setup()
    {
        this.kieContainerMap = getConcurrentMapFactory().createNew();
    }


    protected ConcurrentMapFactory getConcurrentMapFactory()
    {
        return this.concurrentMapFactory;
    }


    @Required
    public void setConcurrentMapFactory(ConcurrentMapFactory concurrentMapFactory)
    {
        this.concurrentMapFactory = concurrentMapFactory;
    }


    protected ReadWriteLock getReadWriteLock()
    {
        return this.readWriteLock;
    }


    protected Lock getReadLock()
    {
        return this.readLock;
    }


    protected Lock getWriteLock()
    {
        return this.writeLock;
    }


    protected boolean isKeepOnlyOneContainerVersion()
    {
        return this.keepOnlyOneContainerVersion;
    }


    public void setKeepOnlyOneContainerVersion(boolean keepOnlyOneContainerVersion)
    {
        this.keepOnlyOneContainerVersion = keepOnlyOneContainerVersion;
    }


    protected void removeAllPreviousVersions(ReleaseId newReleaseId)
    {
        Iterator<ReleaseId> it = this.kieContainerMap.keySet().iterator();
        while(it.hasNext())
        {
            ReleaseId oldReleaseId = it.next();
            if(isPreviousReleaseId(newReleaseId, oldReleaseId))
            {
                LOGGER.info("Removing old Kie module [{}] from container registry", oldReleaseId);
                it.remove();
            }
        }
    }


    protected boolean isPreviousReleaseId(ReleaseId newReleaseId, ReleaseId oldReleaseId)
    {
        return (newReleaseId.getArtifactId().equals(oldReleaseId.getArtifactId()) && newReleaseId
                        .getGroupId().equals(oldReleaseId.getGroupId()) &&
                        !newReleaseId.getVersion().equals(oldReleaseId.getVersion()));
    }


    protected boolean isReadLockEnabled()
    {
        return this.readLockEnabled;
    }


    public void setReadLockEnabled(boolean readLockEnabled)
    {
        this.readLockEnabled = readLockEnabled;
    }
}
