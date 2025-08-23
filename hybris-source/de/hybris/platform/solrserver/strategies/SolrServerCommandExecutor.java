package de.hybris.platform.solrserver.strategies;

import de.hybris.platform.solrserver.SolrServerException;
import java.util.Map;

public interface SolrServerCommandExecutor
{
    void executeCommand(String paramString, Map<String, String> paramMap) throws SolrServerException;
}
