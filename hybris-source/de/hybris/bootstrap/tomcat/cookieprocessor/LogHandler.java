package de.hybris.bootstrap.tomcat.cookieprocessor;

public interface LogHandler
{
    public static final String LOGGER_NAME = "cookies.samesite";


    void debug(String paramString);


    void info(String paramString);


    void warn(String paramString);


    void error(String paramString, Exception paramException);
}
