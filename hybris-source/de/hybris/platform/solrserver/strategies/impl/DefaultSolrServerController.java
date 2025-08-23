package de.hybris.platform.solrserver.strategies.impl;

import de.hybris.platform.solrserver.SolrServerException;
import de.hybris.platform.solrserver.strategies.SolrServerCommandExecutor;
import de.hybris.platform.solrserver.strategies.SolrServerConfigurationProvider;
import de.hybris.platform.solrserver.strategies.SolrServerController;
import java.util.Map;

public class DefaultSolrServerController implements SolrServerController
{
    protected static final String START_SERVERS_COMMAND = "startSolrServers";
    protected static final String STOP_SERVERS_COMMAND = "stopSolrServers";
    private SolrServerConfigurationProvider solrServerConfigurationProvider;
    private SolrServerCommandExecutor solrServerCommandExecutor;


    public void startServers() throws SolrServerException
    {
        Map<String, String> configuration = this.solrServerConfigurationProvider.getConfiguration();
        this.solrServerCommandExecutor.executeCommand("startSolrServers", configuration);
    }


    public void stopServers() throws SolrServerException
    {
        Map<String, String> configuration = this.solrServerConfigurationProvider.getConfiguration();
        this.solrServerCommandExecutor.executeCommand("stopSolrServers", configuration);
    }


    public SolrServerConfigurationProvider getSolrServerConfigurationProvider()
    {
        return this.solrServerConfigurationProvider;
    }


    public void setSolrServerConfigurationProvider(SolrServerConfigurationProvider solrServerConfigurationProvider)
    {
        this.solrServerConfigurationProvider = solrServerConfigurationProvider;
    }


    public SolrServerCommandExecutor getSolrServerCommandExecutor()
    {
        return this.solrServerCommandExecutor;
    }


    public void setSolrServerCommandExecutor(SolrServerCommandExecutor solrServerCommandExecutor)
    {
        this.solrServerCommandExecutor = solrServerCommandExecutor;
    }
}
