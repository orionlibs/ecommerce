package com.hybris.datahub.client;

import javax.ws.rs.client.Client;

public interface DataHubClientProvider
{
    Client createClient(ClientConfiguration paramClientConfiguration);
}
