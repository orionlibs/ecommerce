package de.hybris.platform.jdbcwrapper.logger;

public class LogData
{
    private final int logLevel;
    private final long threadId;
    private final String dataSourceID;
    private final int connectionId;
    private final String now;
    private final long elapsed;
    private final String category;
    private final String prepared;
    private final String sql;
    private final Exception exception;
    private final String text;


    private LogData(LogDataBuilder builder)
    {
        this.logLevel = builder.logLevel;
        this.threadId = builder.threadId;
        this.dataSourceID = builder.dataSourceID;
        this.connectionId = builder.connectionId;
        this.now = builder.now;
        this.elapsed = builder.elapsed;
        this.category = builder.category;
        this.prepared = builder.prepared;
        this.sql = builder.sql;
        this.exception = builder.exception;
        this.text = builder.text;
    }


    public static LogDataBuilder builder()
    {
        return new LogDataBuilder();
    }


    public int getLogLevel()
    {
        return this.logLevel;
    }


    public long getThreadId()
    {
        return this.threadId;
    }


    public String getDataSourceID()
    {
        return this.dataSourceID;
    }


    public int getConnectionId()
    {
        return this.connectionId;
    }


    public String getNow()
    {
        return this.now;
    }


    public long getElapsed()
    {
        return this.elapsed;
    }


    public String getCategory()
    {
        return this.category;
    }


    public String getPrepared()
    {
        return this.prepared;
    }


    public String getSql()
    {
        return this.sql;
    }


    public String getText()
    {
        return this.text;
    }


    public Exception getException()
    {
        return this.exception;
    }
}
