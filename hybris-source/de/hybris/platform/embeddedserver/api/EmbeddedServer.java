package de.hybris.platform.embeddedserver.api;

public interface EmbeddedServer
{
    void start();


    void stop();


    boolean isRunning();
}
