package de.hybris.platform.embeddedserver.api;

public interface EmbeddedServerBuilderContext
{
    EmbeddedServer build();


    EmbeddedServerBuilderContext runningOnPort(int paramInt);


    EmbeddedServerBuilderContext runningOnPort(int paramInt, boolean paramBoolean);


    EmbeddedServerBuilderContext withApplication(EmbeddedApplication paramEmbeddedApplication);
}
