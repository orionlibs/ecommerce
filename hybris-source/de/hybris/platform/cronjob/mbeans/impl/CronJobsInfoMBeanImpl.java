package de.hybris.platform.cronjob.mbeans.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.mbeans.CronJobsInfoMBean;
import de.hybris.platform.cronjob.mbeans.MetricsCronJobHolder;
import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Gives an overview of currently running CronJobs and the possibility to abort them.")
public class CronJobsInfoMBeanImpl extends AbstractJMXMBean implements CronJobsInfoMBean
{
    private MetricsCronJobHolder holder;


    @Required
    public void setHolder(MetricsCronJobHolder holder)
    {
        this.holder = holder;
    }


    @Deprecated(since = "2205", forRemoval = false)
    public void setCronJobService(CronJobService cronJobService)
    {
    }


    protected boolean isJNDIContextAware()
    {
        return StringUtils.isNotBlank(Registry.getCurrentTenant().getDataSource().getJNDIName());
    }


    @ManagedOperation(description = "Abort running CronJobs.")
    public Boolean abortRunningCronJobs()
    {
        return (Boolean)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Overview of currently running CronJobs.", persistPeriod = 1)
    public Collection<String> getRunningCronJobs()
    {
        return (Collection<String>)(new Object(this))
                        .getResult();
    }
}
