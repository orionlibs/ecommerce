package de.hybris.platform.embeddedserver.api;

public interface EmbeddedServerBuilder
{
    EmbeddedServerBuilderContext needEmbeddedServer();


    boolean isAvailable();
}
