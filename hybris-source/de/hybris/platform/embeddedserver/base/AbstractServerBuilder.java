package de.hybris.platform.embeddedserver.base;

import de.hybris.platform.embeddedserver.api.EmbeddedServer;
import de.hybris.platform.embeddedserver.api.EmbeddedServerBuilder;
import de.hybris.platform.embeddedserver.api.EmbeddedServerBuilderContext;

public abstract class AbstractServerBuilder implements EmbeddedServerBuilder
{
    private static final String EMBEDDEDSERVER_HTTP_PORT_CONFIG_KEY = "embeddedserver.http.port";
    private static final String EMBEDDEDSERVER_HTTPS_PORT_CONFIG_KEY = "embeddedserver.ssl.port";


    public EmbeddedServerBuilderContext needEmbeddedServer()
    {
        return (EmbeddedServerBuilderContext)new EmbeddedServerCreationContext(this);
    }


    protected abstract EmbeddedServer createEmbeddedServerFor(EmbeddedServerCreationData paramEmbeddedServerCreationData);
}
