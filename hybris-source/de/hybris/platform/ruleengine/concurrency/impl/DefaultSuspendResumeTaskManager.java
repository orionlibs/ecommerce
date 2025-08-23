package de.hybris.platform.ruleengine.concurrency.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.suspend.SuspendResumeService;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.ruleengine.concurrency.SuspendResumeTaskManager;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSuspendResumeTaskManager implements SuspendResumeTaskManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSuspendResumeTaskManager.class);
    private Tenant currentTenant;
    private SuspendResumeService suspendResumeService;


    public void registerAsNonSuspendableTask(Thread task, String statusInfo)
    {
        Preconditions.checkArgument(Objects.nonNull(task), "Task instance should be provided");
        if(task instanceof RegistrableThread)
        {
            ((RegistrableThread)task).withInitialInfo(OperationInfo.builder()
                            .withStatusInfo("Rule engine module deployment is in progress")
                            .asNotSuspendableOperation()
                            .withTenant(getCurrentTenant().getTenantID())
                            .withCategory(OperationInfo.Category.TASK).build());
        }
        else
        {
            LOGGER.warn("The worker thread {} is not an instance of RegisterableThread: suspension/resume functionality is not working correctly.", task
                            .getName());
        }
    }


    public void registerAsSuspendableTask(Thread task, String statusInfo)
    {
        Preconditions.checkArgument(Objects.nonNull(task), "Task instance should be provided");
        if(task instanceof RegistrableThread)
        {
            ((RegistrableThread)task).withInitialInfo(OperationInfo.builder()
                            .withStatusInfo("Rule engine module deployment is in progress")
                            .withTenant(getCurrentTenant().getTenantID())
                            .withCategory(OperationInfo.Category.TASK).build());
        }
        else
        {
            LOGGER.warn("The worker thread {} is not an instance of RegisterableThread: suspension/resume functionality is not working correctly.", task
                            .getName());
        }
    }


    public boolean isSystemRunning()
    {
        return getSuspendResumeService().getSystemStatus().isRunningOrWaitingForUpdate();
    }


    protected Tenant getCurrentTenant()
    {
        return this.currentTenant;
    }


    @Required
    public void setCurrentTenant(Tenant currentTenant)
    {
        this.currentTenant = currentTenant;
    }


    protected SuspendResumeService getSuspendResumeService()
    {
        return this.suspendResumeService;
    }


    @Required
    public void setSuspendResumeService(SuspendResumeService suspendResumeService)
    {
        this.suspendResumeService = suspendResumeService;
    }
}
