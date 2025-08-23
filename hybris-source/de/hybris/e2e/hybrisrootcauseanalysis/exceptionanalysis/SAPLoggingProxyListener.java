package de.hybris.e2e.hybrisrootcauseanalysis.exceptionanalysis;

import com.sap.tc.logging.Category;
import com.sap.tc.logging.FileLog;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Log;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;
import de.hybris.datasupplier.services.DataSupplierRepositoryService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLogger;
import de.hybris.platform.util.logging.HybrisLoggingEvent;
import javax.annotation.Resource;
import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SAPLoggingProxyListener implements HybrisLogListener
{
    @Resource
    private DataSupplierRepositoryService dataSupplierRepositoryService;
    @Resource
    private ConfigurationService configurationService;
    public static final String FIELD_MSG_ID = "";
    public static final String FIELD_CATEGORY = "hybris";
    private String logSeverity;
    private String traceSeverity;
    private boolean enableEvents;
    private boolean enableTracing;
    private int rotationSize;
    private int rotationCount;
    private String logFilePath;
    private String traceFilePath;
    private String csnComponentName;
    private String dcComponentName;
    private FileLog traceFileLog = null;
    private FileLog logFileLog = null;
    private static final Logger LOG = LogManager.getLogger();


    public void init()
    {
        if(this.configurationService != null)
        {
            Configuration config = this.configurationService.getConfiguration();
            this.logSeverity = config.getString("e2e.rootcauseanalysis.exceptionanalysis.listlog.logseverity");
            this.traceSeverity = config.getString("e2e.rootcauseanalysis.exceptionanalysis.listlog.traceseverity");
            this.logFilePath = config.getString("e2e.rootcauseanalysis.exceptionanalysis.listlog.logfilepath");
            this.traceFilePath = config.getString("e2e.rootcauseanalysis.exceptionanalysis.listlog.tracefilepath");
            this.rotationCount = config.getInt("e2e.rootcauseanalysis.exceptionanalysis.listlog.rotationcount");
            this.rotationSize = config.getInt("e2e.rootcauseanalysis.exceptionanalysis.listlog.rotationsize");
            this.enableEvents = config.getBoolean("e2e.rootcauseanalysis.exceptionanalysis.listlog.enableevents");
            this.enableTracing = config.getBoolean("e2e.rootcauseanalysis.exceptionanalysis.listlog.enabletracing");
        }
        if(this.dataSupplierRepositoryService != null)
        {
            this.dcComponentName = this.dataSupplierRepositoryService.getHybrisProductTechnicalName();
            this.csnComponentName = this.dataSupplierRepositoryService.getHybrisSoftwareComponentTechnicalName();
        }
        this.traceFileLog = new FileLog(this.traceFilePath, this.rotationSize, this.rotationCount);
        this.logFileLog = new FileLog(this.logFilePath, this.rotationSize, this.rotationCount);
        register();
    }


    public void register()
    {
        for(HybrisLogListener listener : HybrisLogger.getAllAListeners())
        {
            if(listener instanceof SAPLoggingProxyListener)
            {
                return;
            }
        }
        HybrisLogger.addListener(this);
        LOG.info("SAP Logging listener registered!");
    }


    public void deregister()
    {
        HybrisLogger.removeListener(this);
    }


    public boolean isEnabledFor(Level level)
    {
        return isEnableEvents();
    }


    protected int log4jLevelToSAPSeverity(Level level)
    {
        if(level == Level.ALL || level == Level.DEBUG || level == Level.TRACE)
        {
            return 100;
        }
        if(level == Level.INFO)
        {
            return 300;
        }
        if(level == Level.WARN)
        {
            return 400;
        }
        if(level == Level.FATAL)
        {
            return 600;
        }
        return 500;
    }


    protected Level log4jConvertLevel1to2(Level level)
    {
        switch(level.toString().toLowerCase())
        {
            case "info":
                return Level.INFO;
            case "warn":
                return Level.WARN;
            case "error":
                return Level.ERROR;
            case "fatal":
                return Level.FATAL;
        }
        return Level.DEBUG;
    }


    public void log(HybrisLoggingEvent e)
    {
        String msg = e.getMessage().toString();
        Category category = Category.getCategory(Category.APPLICATIONS, "hybris");
        Location location = Location.getLocation(e.getFQNOfLoggerClass());
        int msgSeverity = log4jLevelToSAPSeverity(log4jConvertLevel1to2(e.getLevel()));
        Throwable throwable = (e.getThrowable() == null) ? null : e.getThrowable();
        category.addLog((Log)this.logFileLog);
        category.setEffectiveSeverity(Severity.parse(this.logSeverity));
        location.addLog((Log)this.traceFileLog);
        location.setEffectiveSeverity(this.enableTracing ? Severity.parse(this.traceSeverity) : 701);
        SimpleLogger.log(msgSeverity, category, location, this.dcComponentName, this.csnComponentName, "", msg, new Object[0]);
        if(throwable != null && this.enableTracing)
        {
            SimpleLogger.traceThrowable(msgSeverity, location, msg, throwable);
        }
    }


    public void setLogFilePath(String logFilePath)
    {
        this.logFilePath = logFilePath;
    }


    public void setTraceFilePath(String traceFilePath)
    {
        this.traceFilePath = traceFilePath;
    }


    public void setLogSeverity(String logSeverity)
    {
        this.logSeverity = logSeverity;
    }


    public void setTraceSeverity(String traceSeverity)
    {
        this.traceSeverity = traceSeverity;
    }


    public void setRotationSize(int rotationSize)
    {
        this.rotationSize = rotationSize;
    }


    public void setRotationCount(int rotationCount)
    {
        this.rotationCount = rotationCount;
    }


    public void setCsnComponentName(String csnComponentName)
    {
        this.csnComponentName = csnComponentName;
    }


    public void setDcComponentName(String dcComponentName)
    {
        this.dcComponentName = dcComponentName;
    }


    public void setEnableEvents(boolean enableEvents)
    {
        this.enableEvents = enableEvents;
    }


    public void setEnableTracing(boolean enableTracing)
    {
        this.enableTracing = enableTracing;
    }


    public void setDataSupplierRepositoryService(DataSupplierRepositoryService dataSupplierRepositoryService)
    {
        this.dataSupplierRepositoryService = dataSupplierRepositoryService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public String getLogSeverity()
    {
        return this.logSeverity;
    }


    public String getTraceSeverity()
    {
        return this.traceSeverity;
    }


    public boolean isEnableTracing()
    {
        return this.enableTracing;
    }


    public boolean isEnableEvents()
    {
        return this.enableEvents;
    }


    public String getLogFilePath()
    {
        return this.logFilePath;
    }


    public String getTraceFilePath()
    {
        return this.traceFilePath;
    }


    public int getRotationCount()
    {
        return this.rotationCount;
    }


    public int getRotationSize()
    {
        return this.rotationSize;
    }
}
