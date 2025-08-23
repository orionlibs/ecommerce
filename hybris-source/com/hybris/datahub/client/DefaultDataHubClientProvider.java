package com.hybris.datahub.client;

import com.hybris.datahub.rest.filter.FollowRedirectFilter;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Configuration;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

public class DefaultDataHubClientProvider implements DataHubClientProvider
{
    public Client createClient(ClientConfiguration cfg)
    {
        JerseyClient jerseyClient = (cfg != null) ? JerseyClientBuilder.createClient((Configuration)cfg.toJerseyClientConfig()) : JerseyClientBuilder.createClient();
        jerseyClient.register(FollowRedirectFilter.class);
        return (Client)jerseyClient;
    }
}
