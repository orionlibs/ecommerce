package de.hybris.platform.ruleengine.concurrency;

import de.hybris.platform.core.Tenant;
import java.util.concurrent.ThreadFactory;

public interface TaskContext
{
    Tenant getCurrentTenant();


    ThreadFactory getThreadFactory();


    int getNumberOfThreads();


    Long getThreadTimeout();
}
