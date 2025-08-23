package de.hybris.platform.patches.internal.logger;

public interface PatchLogger
{
    String getName();


    boolean isDebugEnabled();


    void debug(String paramString);


    void debug(String paramString, Object paramObject);


    void debug(String paramString, Object paramObject1, Object paramObject2);


    void debug(String paramString, Object... paramVarArgs);


    void debug(String paramString, Throwable paramThrowable);


    boolean isInfoEnabled();


    void info(String paramString);


    void info(LoggingMode paramLoggingMode, String paramString);


    void info(LoggingMode paramLoggingMode, String paramString, Object paramObject);


    void info(LoggingMode paramLoggingMode, String paramString, Object paramObject1, Object paramObject2);


    void info(LoggingMode paramLoggingMode, String paramString, Object... paramVarArgs);


    void info(LoggingMode paramLoggingMode, String paramString, Throwable paramThrowable);


    boolean isWarnEnabled();


    void warn(LoggingMode paramLoggingMode, String paramString);


    void warn(LoggingMode paramLoggingMode, String paramString, Object paramObject);


    void warn(LoggingMode paramLoggingMode, String paramString, Object... paramVarArgs);


    void warn(LoggingMode paramLoggingMode, String paramString, Object paramObject1, Object paramObject2);


    void warn(LoggingMode paramLoggingMode, String paramString, Throwable paramThrowable);


    boolean isErrorEnabled();


    void error(LoggingMode paramLoggingMode, String paramString);


    void error(LoggingMode paramLoggingMode, String paramString, Object paramObject);


    void error(LoggingMode paramLoggingMode, String paramString, Object paramObject1, Object paramObject2);


    void error(LoggingMode paramLoggingMode, String paramString, Object... paramVarArgs);


    void error(LoggingMode paramLoggingMode, String paramString, Throwable paramThrowable);


    void error(String paramString, Throwable paramThrowable);
}
