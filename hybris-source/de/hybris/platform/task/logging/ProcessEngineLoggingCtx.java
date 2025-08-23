package de.hybris.platform.task.logging;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskLogModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLogger;
import java.util.Date;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

public class ProcessEngineLoggingCtx implements TaskLoggingCtx
{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessEngineLoggingCtx.class);
    private final TempFileWriter tempFileWriter;
    private final MediaFileLogListener listener;
    private final ProcessTaskModel task;
    private static final String PROCESS_ENGINE_LOGGER_LEVEL_PROP = "loggingctx.process.engine.logger.level";
    private static final String PROCESS_ENGINE_LOGGER_NAME_PROP = "loggingctx.process.engine.logger.name";
    private static final String PROCESS_ENGINE_LOGGER_DB_STORE_ENABLED = "processengine.process.log.dbstore.enabled";
    private final ModelService modelService;
    private Date startDate;
    private String statusCode;
    private String startAction;


    public ProcessEngineLoggingCtx(ProcessTaskModel task, ModelService modelService)
    {
        this.task = task;
        this.tempFileWriter = new TempFileWriter();
        this.listener = createFileLogListener();
        if(this.listener != null)
        {
            HybrisLogger.addListener((HybrisLogListener)this.listener);
        }
        this.modelService = modelService;
    }


    @Deprecated(since = "6.7", forRemoval = true)
    public ProcessEngineLoggingCtx(ProcessTaskModel task, ModelService modelService, TransactionTemplate transactionTemplate)
    {
        this(task, modelService);
    }


    protected MediaFileLogListener createFileLogListener()
    {
        try
        {
            ConfigIntf config = Registry.getCurrentTenant().getConfig();
            String enabledFoLoggerLevel = config.getString("loggingctx.process.engine.logger.level", "");
            String loggerNameForLayoutConfig = config.getString("loggingctx.process.engine.logger.name", "");
            return new MediaFileLogListener(Level.toLevel(enabledFoLoggerLevel), loggerNameForLayoutConfig, this.tempFileWriter);
        }
        catch(Exception ex)
        {
            LOG.error("Failed to create file log listener", ex);
            return null;
        }
    }


    public void registerExecutionInfo(Date startDate, String statusCode, String action)
    {
        this.startDate = startDate;
        this.statusCode = statusCode;
        this.startAction = action;
    }


    public void finishAndClose()
    {
        if(this.listener != null)
        {
            HybrisLogger.removeListener((HybrisLogListener)this.listener);
        }
        if(this.tempFileWriter != null)
        {
            try
            {
                this.tempFileWriter.close();
                boolean dbStoreEnabled = Config.getBoolean("processengine.process.log.dbstore.enabled", true);
                if(dbStoreEnabled)
                {
                    BusinessProcessModel process = this.task.getProcess();
                    String loggedMessages = this.tempFileWriter.getContextAsText();
                    saveAsTaskLogModel(process, loggedMessages);
                }
            }
            finally
            {
                this.tempFileWriter.deleteTempFile();
            }
        }
    }


    protected void saveAsTaskLogModel(BusinessProcessModel process, String loggedMessages)
    {
        ProcessTaskLogModel processTaskLog = new ProcessTaskLogModel();
        processTaskLog.setReturnCode(this.statusCode);
        processTaskLog.setStartDate(this.startDate);
        processTaskLog.setEndDate(new Date());
        processTaskLog.setActionId(this.startAction);
        processTaskLog.setClusterId(Integer.valueOf(Registry.getClusterID()));
        processTaskLog.setLogMessages(loggedMessages);
        processTaskLog.setProcess(process);
        this.modelService.save(processTaskLog);
    }
}
