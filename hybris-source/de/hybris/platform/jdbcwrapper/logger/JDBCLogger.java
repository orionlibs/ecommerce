package de.hybris.platform.jdbcwrapper.logger;

public interface JDBCLogger
{
    void logSQL(long paramLong1, String paramString1, int paramInt, String paramString2, long paramLong2, String paramString3, String paramString4, String paramString5);


    void logException(Exception paramException);


    void logText(String paramString);


    default void logText(LogData logData)
    {
        logText(logData.getText());
    }


    default void logException(LogData logData)
    {
        logException(logData.getException());
    }


    default void logSQL(LogData logData)
    {
        logSQL(logData.getThreadId(), logData.getDataSourceID(), logData.getConnectionId(), logData.getNow(), logData
                        .getElapsed(), logData.getCategory(), logData.getPrepared(), logData.getSql());
    }


    String getLastEntry();
}
