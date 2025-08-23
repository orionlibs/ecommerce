package de.hybris.platform.solr.controller.core;

import de.hybris.platform.solr.controller.SolrControllerException;

@FunctionalInterface
public interface CommandBuilder
{
    void build(ProcessBuilder paramProcessBuilder) throws SolrControllerException;
}
