package de.hybris.platform.task.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.task.BusinessProcessRestartStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBusinessProcessRestartStrategy implements BusinessProcessRestartStrategy
{
    public static final String PROCESSENGINE_PROCESS_RESTART_RETRIES = "processengine.process.restart.retries";
    public static final String PROCESSENGINE_PROCESS_RESTART_MILLIS = "processengine.process.restart.millis";
    public static final String PROCESSENGINE_PROCESS_RESTART_EXCEPTION_IF_FAILED = "processengine.process.restart.exception.if.failed";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBusinessProcessRestartStrategy.class);
    private TaskDAO taskDAO;
    private ModelService modelService;


    public DefaultBusinessProcessRestartStrategy(TaskDAO taskDAO, ModelService modelService)
    {
        this.taskDAO = taskDAO;
        this.modelService = modelService;
    }


    private static void threadSleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOG.error("Thread interrupted", e);
        }
    }


    public boolean makeARequestToRestartProcess(BusinessProcessModel process)
    {
        int numberOfRetries = Config.getInt("processengine.process.restart.retries", 3);
        long secondsBetweenRetries = Config.getInt("processengine.process.restart.millis", 500);
        for(int i = 0; i < numberOfRetries; i++)
        {
            if(restartOfCurrentProcessPossible(process))
            {
                LOG.info("Restart possible");
                return true;
            }
            threadSleep(secondsBetweenRetries);
        }
        LOG.info("Failed to restart");
        throwExceptionOrLogMessageIfFailedToRestart(process);
        return false;
    }


    private void throwExceptionOrLogMessageIfFailedToRestart(BusinessProcessModel process)
    {
        String msg = "Couldn't restart business process " + process.getCode();
        if(Config.getBoolean("processengine.process.restart.exception.if.failed", true))
        {
            throw new IllegalStateException(msg);
        }
        LOG.info(msg);
    }


    private boolean restartOfCurrentProcessPossible(BusinessProcessModel process)
    {
        return (isNotActive(process) || removeProcessTasksIfAllUnlocked(process));
    }


    private boolean isNotActive(BusinessProcessModel process)
    {
        return (process.getProcessState() != ProcessState.WAITING && process.getProcessState() != ProcessState.RUNNING);
    }


    private boolean removeProcessTasksIfAllUnlocked(BusinessProcessModel process)
    {
        List<PK> lockedTaskPKs = new ArrayList<>();
        List<TasksProvider.VersionPK> unlockedTasksPKs = this.taskDAO.getProcessTasksIfAllUnlocked(process.getPk().getLongValue());
        LOG.debug("Unlocked tasks PKs " + unlockedTasksPKs);
        if(unlockedTasksPKs.stream().allMatch(ver -> this.taskDAO.lock(Long.valueOf(ver.pk.getLongValue()), ver.version)))
        {
            lockedTaskPKs.addAll((Collection<? extends PK>)unlockedTasksPKs.stream().map(ver -> ver.pk).collect(Collectors.toList()));
        }
        Objects.requireNonNull(this.modelService);
        lockedTaskPKs.forEach(this.modelService::remove);
        return !lockedTaskPKs.isEmpty();
    }
}
