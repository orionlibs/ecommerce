package de.hybris.platform.task.logging;

import com.google.common.collect.Iterables;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLoggingEvent;
import de.hybris.platform.util.logging.log4j2.HybrisLog4j2Logger;
import de.hybris.platform.util.logging.log4j2.HybrisLoggerContext;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Priority;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.slf4j.MDC;

public class MediaFileLogListener implements HybrisLogListener
{
    private final Level effectiveLevel;
    private final String loggerName;
    private final TempFileWriter tempFileWriter;
    private final boolean logOnlyForCurrentThread;
    private final long currentThreadId = Thread.currentThread().getId();


    public MediaFileLogListener(Level effectiveLevel, String loggerName, TempFileWriter tempFileWriter)
    {
        this(effectiveLevel, loggerName, tempFileWriter, true);
    }


    public MediaFileLogListener(Level effectiveLevel, String loggerName, TempFileWriter tempFileWriter, boolean logOnlyForCurrentThread)
    {
        this.effectiveLevel = Objects.<Level>requireNonNull(effectiveLevel, "effectiveLevel is required");
        this.tempFileWriter = Objects.<TempFileWriter>requireNonNull(tempFileWriter, "tempFileWriter is required");
        this.loggerName = loggerName;
        this.logOnlyForCurrentThread = logOnlyForCurrentThread;
    }


    public boolean isEnabledFor(Level level)
    {
        return (level.isGreaterOrEqual((Priority)this.effectiveLevel) && (!this.logOnlyForCurrentThread || this.currentThreadId ==
                        Thread.currentThread().getId()));
    }


    public void log(HybrisLoggingEvent event)
    {
        this.tempFileWriter.write(getFormattedMessage(event));
    }


    private String getFormattedMessage(HybrisLoggingEvent event)
    {
        Layout<? extends Serializable> layout = getLogLayout();
        if(layout == null)
        {
            return event.getRenderedMessage();
        }
        Log4jLogEvent.Builder eventBuilder = new Log4jLogEvent.Builder();
        eventBuilder.setMessage((Message)new SimpleMessage(event.getRenderedMessage()));
        eventBuilder.setLoggerFqcn(event.getFQNOfLoggerClass());
        if(event.getLevel() != null)
        {
            eventBuilder.setLevel(HybrisLog4j2Logger.mapV1ToV2Level(event.getLevel()));
        }
        else
        {
            eventBuilder.setLevel(Level.INFO);
        }
        if(event.getLogger() != null)
        {
            eventBuilder.setLoggerName(event.getLogger().getName());
        }
        else
        {
            eventBuilder.setLoggerName(event.getFQNOfLoggerClass());
        }
        eventBuilder.setTimeMillis((new Date()).getTime());
        eventBuilder.setContextMap(MDC.getCopyOfContextMap());
        Log4jLogEvent event2 = eventBuilder.build();
        byte[] bytes = layout.toByteArray((LogEvent)event2);
        return new String(bytes);
    }


    private Layout<? extends Serializable> getLogLayout()
    {
        Configuration configuration = ((HybrisLoggerContext)LogManager.getContext()).getConfiguration();
        Map<String, Appender> appenders = configuration.getAppenders();
        Appender appender = null;
        if(appenders.get(this.loggerName) == null)
        {
            Set<String> appendersNames = appenders.keySet();
            if(CollectionUtils.isNotEmpty(appendersNames))
            {
                appender = appenders.get(Iterables.get(appendersNames, 0));
            }
        }
        else
        {
            appender = (Appender)configuration.getAppenders().get(this.loggerName);
        }
        return (appender == null) ? null : appender.getLayout();
    }
}
