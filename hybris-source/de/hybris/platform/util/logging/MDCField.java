package de.hybris.platform.util.logging;

import de.hybris.platform.util.logging.context.LoggingContextFactory;

public enum MDCField
{
    JDBC_DATA_SOURCE_ID("jdbc-dataSourceId"),
    JDBC_CATEGORY("jdbc-category"),
    JDBC_START_TIME("jdbc-startTime"),
    JDBC_TIME_ELAPSED("jdbc-timeElapsed");
    private final String mdcFieldName;


    MDCField(String name)
    {
        this.mdcFieldName = name;
    }


    public void put(Object value)
    {
        LoggingContextFactory.getLoggingContextHandler().put(this.mdcFieldName, String.valueOf(value));
    }


    public void remove()
    {
        LoggingContextFactory.getLoggingContextHandler().remove(this.mdcFieldName);
    }
}
