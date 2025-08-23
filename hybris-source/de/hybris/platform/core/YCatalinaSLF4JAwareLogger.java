package de.hybris.platform.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YCatalinaSLF4JAwareLogger implements Consumer<LogRecord>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(YCatalinaSLF4JAwareLogger.class);
    private static final Map<Level, Consumer<LogRecord>> logFunctions = new HashMap<>();

    static
    {
        logFunctions.put(Level.FINEST, lr -> LOGGER.trace(lr.getMessage()));
        logFunctions.put(Level.FINER, lr -> LOGGER.debug(lr.getMessage()));
        logFunctions.put(Level.FINE, lr -> LOGGER.debug(lr.getMessage()));
        logFunctions.put(Level.CONFIG, lr -> LOGGER.info(lr.getMessage()));
        logFunctions.put(Level.INFO, lr -> LOGGER.info(lr.getMessage()));
        logFunctions.put(Level.WARNING, lr -> LOGGER.warn(lr.getMessage()));
        logFunctions.put(Level.SEVERE, lr -> {
            if(lr.getThrown() != null)
            {
                LOGGER.error(lr.getMessage(), lr.getThrown());
            }
            else
            {
                LOGGER.error(lr.getMessage());
            }
        });
    }

    public void accept(LogRecord record)
    {
        ((Consumer<LogRecord>)logFunctions.get(record.getLevel())).accept(record);
    }
}
