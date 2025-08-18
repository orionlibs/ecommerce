package io.github.orionlibs.ecommerce.core;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class LoggerTest
{
    private final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(io.github.orionlibs.ecommerce.core.Logger.class);
    private final InMemoryAppender appender = new InMemoryAppender();


    public LoggerTest()
    {
        appender.setContext(logger.getLoggerContext());
        appender.start();
        logger.addAppender(appender);
    }


    @AfterEach
    void tearDown()
    {
        logger.detachAppender(appender);
        appender.stop();
        appender.clear();
    }


    @Test
    void testLogger_info()
    {
        Logger.info("Hello world");
        List<ILoggingEvent> events = appender.getEvents();
        assertThat(events.isEmpty()).isFalse();
        ILoggingEvent e = events.get(0);
        assertThat(e.getFormattedMessage()).contains("Hello world");
    }


    @Test
    void testLogger_info_withParameters()
    {
        Logger.info("Hello {} world", "beautiful");
        List<ILoggingEvent> events = appender.getEvents();
        assertThat(events.isEmpty()).isFalse();
        ILoggingEvent e = events.get(0);
        assertThat(e.getFormattedMessage()).contains("Hello beautiful world");
    }


    @Test
    void testLogger_error()
    {
        Logger.error("Hello world");
        List<ILoggingEvent> events = appender.getEvents();
        assertThat(events.isEmpty()).isFalse();
        ILoggingEvent e = events.get(0);
        assertThat(e.getFormattedMessage()).contains("Hello world");
    }


    @Test
    void testLogger_error_withParameters()
    {
        Logger.error("Hello {} world", "beautiful");
        List<ILoggingEvent> events = appender.getEvents();
        assertThat(events.isEmpty()).isFalse();
        ILoggingEvent e = events.get(0);
        assertThat(e.getFormattedMessage()).contains("Hello beautiful world");
    }


    static class InMemoryAppender extends AppenderBase<ILoggingEvent>
    {
        private final List<ILoggingEvent> events = new ArrayList<>();


        @Override
        protected void append(ILoggingEvent eventObject)
        {
            events.add(eventObject);
        }


        public List<ILoggingEvent> getEvents()
        {
            return events;
        }


        public void clear()
        {
            events.clear();
        }
    }
}
