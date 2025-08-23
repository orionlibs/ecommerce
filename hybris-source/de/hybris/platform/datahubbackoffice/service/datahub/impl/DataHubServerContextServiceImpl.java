/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.datahubbackoffice.service.datahub.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.datahubbackoffice.service.datahub.DataHubNameService;
import de.hybris.platform.datahubbackoffice.service.datahub.DataHubServer;
import de.hybris.platform.datahubbackoffice.service.datahub.DataHubServerAware;
import de.hybris.platform.datahubbackoffice.service.datahub.DataHubServerContextService;
import de.hybris.platform.datahubbackoffice.service.datahub.InaccessibleDataHubServer;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * A factory for creating REST clients based on the DataHub instance selected by the user.
 */
public class DataHubServerContextServiceImpl implements DataHubServerAware, DataHubServerContextService
{
    private DataHubServer dataHubServer;
    private Collection<DataHubServer> allServers;
    private DataHubNameService nameService;


    @Override
    public DataHubServer getContextDataHubServer()
    {
        return dataHubServer != null ? dataHubServer : getDefaultServer();
    }


    @Override
    public Collection<DataHubServer> getAllServers()
    {
        if(allServers == null)
        {
            allServers = initializeServers();
        }
        return allServers;
    }


    private Collection<DataHubServer> initializeServers()
    {
        return nameService.getAllServers().stream()
                        .map(DataHubServer::new)
                        .collect(Collectors.toList());
    }


    private DataHubServer getDefaultServer()
    {
        final DataHubServer server = getAllServers().stream()
                        .filter(DataHubServer::isAccessibleWithTimeout)
                        .findFirst()
                        .orElseGet(InaccessibleDataHubServer::new);
        setDataHubServer(server);
        return server;
    }


    @Override
    public void reset()
    {
        dataHubServer = null;
        allServers = null;
    }


    /**
     * Sets DataHub server the user will operate with.
     *
     * @param server a DataHub server to be used as the context server.
     * @see #getContextDataHubServer()
     * @throws IllegalArgumentException if provided server is {@code null}
     */
    @Override
    public void setDataHubServer(final DataHubServer server)
    {
        Preconditions.checkArgument(server != null);
        dataHubServer = server;
    }


    /**
     * Injects DataHub server name service to be used.
     *
     * @param service a service implementation to use.
     */
    @Required
    public void setNameService(final DataHubNameService service)
    {
        nameService = service;
    }
}
