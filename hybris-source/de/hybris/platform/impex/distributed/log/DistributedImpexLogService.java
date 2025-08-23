package de.hybris.platform.impex.distributed.log;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLogger;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "6.2.0", forRemoval = true)
public class DistributedImpexLogService
{
    public static final String BATCH_ID = "batchId";
    public static final String THREAD_ID = "threadId";
    public static final String PROCESS_CODE = "processCode";
    public static final String PROCESS_EXECUTION_ID = "processExecutionId";
    public static final String CLUSTER_ID = "clusterId";
    public static final String TENANT_ID = "tenantId";
    private ClusterService clusterService;


    public void registerLogListener(HybrisLogListener logListener)
    {
        HybrisLogger.addListener(logListener);
    }


    public void unregisterLogListener(HybrisLogListener logListener)
    {
        if(logListener == null)
        {
            return;
        }
        HybrisLogger.removeListener(logListener);
    }


    public void setLogContext(ImportBatchModel inputBatch)
    {
        LoggingContextFactory.getLoggingContextHandler().put("batchId", inputBatch.getId());
        LoggingContextFactory.getLoggingContextHandler().put("threadId", Thread.currentThread().getName());
        LoggingContextFactory.getLoggingContextHandler().put("processCode", inputBatch.getProcess().getCode());
        LoggingContextFactory.getLoggingContextHandler().put("processExecutionId", inputBatch.getProcess().getCurrentExecutionId());
        LoggingContextFactory.getLoggingContextHandler().put("clusterId", getClusterId());
        LoggingContextFactory.getLoggingContextHandler().put("tenantId", getTenantId());
    }


    private String getClusterId()
    {
        if(this.clusterService.isClusteringEnabled())
        {
            return String.valueOf(this.clusterService.getClusterId());
        }
        return String.valueOf(-1);
    }


    public Level getLogLevel(String logLevelCode)
    {
        if(StringUtils.isBlank(logLevelCode) || GeneratedCronJobConstants.Enumerations.JobLogLevel.WARNING.equalsIgnoreCase(logLevelCode))
        {
            return Level.WARN;
        }
        return Level.toLevel(logLevelCode);
    }


    private String getTenantId()
    {
        return Registry.getCurrentTenantNoFallback().getTenantID();
    }


    public void clearLogContext()
    {
        LoggingContextFactory.getLoggingContextHandler().remove("batchId");
        LoggingContextFactory.getLoggingContextHandler().remove("threadId");
        LoggingContextFactory.getLoggingContextHandler().remove("processCode");
        LoggingContextFactory.getLoggingContextHandler().remove("processExecutionId");
        LoggingContextFactory.getLoggingContextHandler().remove("clusterId");
        LoggingContextFactory.getLoggingContextHandler().remove("tenantId");
    }


    public String getLogHeader()
    {
        return "[" + getFromMDC("threadId") + "] [clusterId: " + getFromMDC("clusterId") + "; batchId: " +
                        getFromMDC("batchId") + "; processCode: " + getFromMDC("processCode") + "; processExecutionId: " +
                        getFromMDC("processExecutionId") + "; tenantId: " + getFromMDC("tenantId") + "] ";
    }


    private String getFromMDC(String key)
    {
        String value = LoggingContextFactory.getLoggingContextHandler().get(key);
        return StringUtils.isEmpty(value) ? "<none>" : value;
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }
}
