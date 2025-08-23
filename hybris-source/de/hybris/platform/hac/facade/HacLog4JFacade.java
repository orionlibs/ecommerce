package de.hybris.platform.hac.facade;

import com.google.common.collect.Lists;
import de.hybris.platform.hac.data.LoggerConfigData;
import de.hybris.platform.util.logging.log4j2.HybrisLoggerContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class HacLog4JFacade
{
    private static final Logger LOG = Logger.getLogger(HacLog4JFacade.class);
    private static final String PRESENTATION_ROOT_NAME = "root";


    public List<LoggerConfigData> getLoggers()
    {
        Collection<LoggerConfig> loggerConfigs = getAllLoggerConfig();
        List<LoggerConfigData> loggerConfigurations = new ArrayList<>();
        for(LoggerConfig loggerConfig : loggerConfigs)
        {
            loggerConfigurations.add(mapToConfigData(loggerConfig));
        }
        return new ArrayList<>(loggerConfigurations);
    }


    private LoggerConfigData mapToConfigData(LoggerConfig loggerConfig)
    {
        LoggerConfigData configData = new LoggerConfigData();
        configData.setName(toPresentationFormat(loggerConfig.getName()));
        configData.setEffectiveLevel(loggerConfig.getLevel());
        if(loggerConfig.getParent() != null)
        {
            configData.setParentName(toPresentationFormat(loggerConfig.getParent().getName()));
        }
        return configData;
    }


    private String toPresentationFormat(String name)
    {
        return name.equals("") ? "root" : name;
    }


    private String fromPresentationFormat(String name)
    {
        return name.equals("root") ? "" : name;
    }


    private Collection<LoggerConfig> getAllLoggerConfig()
    {
        HybrisLoggerContext ctx = getLoggerContext();
        Configuration config = ctx.getConfiguration();
        return config.getLoggers().values();
    }


    public List<Level> getAllLevels()
    {
        return Lists.newArrayList((Object[])new Level[] {Level.ALL, Level.OFF, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL});
    }


    public boolean changeLogLevel(String targetLogger, String levelName)
    {
        LoggerConfig config = getOrCreateLoggerConfigFor(fromPresentationFormat(targetLogger));
        Level level = Level.getLevel(levelName);
        LOG.info("Changing level of " + config + " from " + config.getLevel() + " to " + level);
        config.setLevel(level);
        getLoggerContext().updateLoggers();
        return true;
    }


    private LoggerConfig getOrCreateLoggerConfigFor(String loggerName)
    {
        Configuration configuration = getLoggerContext().getConfiguration();
        LoggerConfig existingConfig = configuration.getLoggerConfig(loggerName);
        if(existingConfig.getName().equals(loggerName))
        {
            return existingConfig;
        }
        LOG.info("Creating logger " + loggerName);
        LoggerConfig rootLoggerConfig = configuration.getRootLogger();
        LoggerConfig newLoggerConfig = LoggerConfig.createLogger(true, rootLoggerConfig
                                        .getLevel(), loggerName,
                        String.valueOf(rootLoggerConfig.isIncludeLocation()), (AppenderRef[])rootLoggerConfig
                                        .getAppenderRefs()
                                        .toArray((Object[])new AppenderRef[0]), null, configuration, rootLoggerConfig
                                        .getFilter());
        rootLoggerConfig.getAppenders().forEach((k, v) -> rootLoggerConfig.addAppender(v, null, null));
        configuration.addLogger(loggerName, newLoggerConfig);
        return newLoggerConfig;
    }


    private HybrisLoggerContext getLoggerContext()
    {
        return (HybrisLoggerContext)LogManager.getContext(false);
    }
}
