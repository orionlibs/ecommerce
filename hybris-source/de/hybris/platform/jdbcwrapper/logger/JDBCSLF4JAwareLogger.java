package de.hybris.platform.jdbcwrapper.logger;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.logging.MDCField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

public class JDBCSLF4JAwareLogger extends FormattedLogger
{
    private static final String FQCN = JDBCSLF4JAwareLogger.class.getName();
    private static final int DEFAULT_LOG_LEVEL = 10;
    private final Logger logger = LoggerFactory.getLogger(JDBCSLF4JAwareLogger.class);
    private final LocationAwareLogger locationAwareLogger;


    public JDBCSLF4JAwareLogger(Tenant system)
    {
        if(this.logger instanceof LocationAwareLogger)
        {
            this.locationAwareLogger = (LocationAwareLogger)this.logger;
        }
        else
        {
            this.locationAwareLogger = null;
        }
    }


    public void logException(LogData logData)
    {
        bridgeLogMsg(logData.getLogLevel(), logData.getException().getMessage(), logData.getException());
    }


    public void logSQL(LogData logData)
    {
        MDCField.JDBC_DATA_SOURCE_ID.put(logData.getDataSourceID());
        MDCField.JDBC_CATEGORY.put(logData.getCategory());
        MDCField.JDBC_START_TIME.put(logData.getNow());
        MDCField.JDBC_TIME_ELAPSED.put(Long.valueOf(logData.getElapsed()));
        try
        {
            logText(logData.getPrepared() + logData.getPrepared());
        }
        finally
        {
            MDCField.JDBC_DATA_SOURCE_ID.remove();
            MDCField.JDBC_CATEGORY.remove();
            MDCField.JDBC_START_TIME.remove();
            MDCField.JDBC_TIME_ELAPSED.remove();
        }
    }


    public void logText(LogData logData)
    {
        bridgeLogMsg(logData.getLogLevel(), logData.getText(), null);
        setLastEntry(logData.getText());
    }


    public void logSQL(long threadId, String dataSourceID, int connectionId, String now, long elapsed, String category, String prepared, String sql)
    {
        LogData logData = LogData.builder().withLogLevel(10).withThreadId(threadId).withDataSourceID(dataSourceID).withConnectionId(connectionId).withNow(now).withElapsed(elapsed).withCategory(category).withPrepared(prepared).withSql(sql).build();
        logSQL(logData);
    }


    public void logException(Exception exception)
    {
        LogData logData = LogData.builder().withLogLevel(10).withException(exception).build();
        logException(logData);
    }


    public void logText(String text)
    {
        LogData logData = LogData.builder().withLogLevel(10).withText(text).build();
        logText(logData);
    }


    private void bridgeLogMsg(int level, Object message, Throwable th)
    {
        String msg = String.valueOf(message);
        if(this.locationAwareLogger == null)
        {
            logWithLogger(level, th, msg);
        }
        else
        {
            this.locationAwareLogger.log(null, FQCN, level, msg, null, th);
        }
    }


    private void logWithLogger(int level, Throwable th, String msg)
    {
        switch(level)
        {
            case 0:
                this.logger.trace(msg, th);
                return;
            case 10:
                this.logger.debug(msg, th);
                return;
            case 20:
                this.logger.info(msg, th);
                return;
            case 30:
                this.logger.warn(msg, th);
                return;
            case 40:
                this.logger.error(msg, th);
                return;
        }
        this.logger.debug(msg, th);
    }
}
