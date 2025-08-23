package de.hybris.platform.task;

public interface TaskEngine
{
    void triggerRepoll(Integer paramInteger, String paramString);


    void repollIfNecessary(Integer paramInteger, String paramString);


    boolean isAllowedToStart();


    void start();


    void stop();


    boolean isRunning();
}
