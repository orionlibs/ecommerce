package de.hybris.platform.util.threadpool;

public interface SelfLoggingProcess
{
    boolean isErrorEnabled();


    void error(String paramString);
}
