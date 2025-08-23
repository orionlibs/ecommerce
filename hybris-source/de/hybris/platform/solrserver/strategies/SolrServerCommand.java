package de.hybris.platform.solrserver.strategies;

import de.hybris.platform.solrserver.SolrServerException;
import java.util.Map;

@FunctionalInterface
public interface SolrServerCommand
{
    void execute(Map<String, String> paramMap) throws SolrServerException;
}
