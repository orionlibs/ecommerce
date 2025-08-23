package de.hybris.platform.solrserver.strategies;

import de.hybris.platform.solrserver.SolrServerException;

public interface SolrServerController
{
    void startServers() throws SolrServerException;


    void stopServers() throws SolrServerException;
}
