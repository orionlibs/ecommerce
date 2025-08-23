/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.datahubbackoffice.datahub.rest;

import com.hybris.datahub.client.ClientConfiguration;
import com.hybris.datahub.client.DataHubClientProvider;
import com.hybris.datahub.client.DefaultDataHubClientProvider;
import com.hybris.datahub.client.StatusCountClient;
import de.hybris.platform.datahubbackoffice.service.datahub.UserContext;
import de.hybris.platform.datahubbackoffice.service.datahub.impl.DataHubServerContextServiceImpl;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * A status count client which connects to multiple DataHub servers
 */
public class DynamicStatusCountClient extends StatusCountClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicStatusCountClient.class);
    private DataHubServerContextServiceImpl service;
    private ClientConfiguration adminClientConfiguration;
    private ClientConfiguration developerClientConfiguration;
    private UserContext userContext;


    public DynamicStatusCountClient(final ClientConfiguration cfg)
    {
        super(cfg, null);
    }


    @Override
    public String getBaseApiUrl()
    {
        return service.getContextDataHubServer().getLocation();
    }


    @Override
    protected ClientConfiguration getConfiguration()
    {
        if(userContext == null)
        {
            return new ClientConfiguration()
                            .setContentType(getDefaultMediaType().toString());
        }
        return userContext.isUserDataHubAdmin() ? adminClientConfiguration : developerClientConfiguration;
    }


    @Override
    public Invocation.Builder resource(final String uri)
    {
        final String resourceUrl = this.getBaseApiUrl() + uri;
        LOGGER.debug("Created resource for {}", resourceUrl);
        final DataHubClientProvider dataHubClientProvider = new DefaultDataHubClientProvider();
        final Client client = dataHubClientProvider.createClient(getConfiguration());
        return client.target(resourceUrl).request();
    }


    /**
     * Injects service to use
     *
     * @param s a service, which will provide DataHub server information for the connections.
     */
    @Required
    public void setServerContextService(final DataHubServerContextServiceImpl s)
    {
        service = s;
    }


    @Required
    public void setAdminClientConfiguration(final ClientConfiguration adminClientConfiguration)
    {
        this.adminClientConfiguration = adminClientConfiguration;
    }


    @Required
    public void setDeveloperClientConfiguration(final ClientConfiguration developerClientConfiguration)
    {
        this.developerClientConfiguration = developerClientConfiguration;
    }


    @Required
    public void setUserContext(final UserContext userContext)
    {
        this.userContext = userContext;
    }
}
