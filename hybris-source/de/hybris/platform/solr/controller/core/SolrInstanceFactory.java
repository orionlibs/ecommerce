package de.hybris.platform.solr.controller.core;

import de.hybris.platform.solr.controller.SolrControllerException;
import java.util.Collection;
import java.util.Map;

public interface SolrInstanceFactory
{
    SolrInstance getInstanceForName(Map<String, String> paramMap, String paramString) throws SolrControllerException;


    Collection<SolrInstance> getInstances(Map<String, String> paramMap) throws SolrControllerException;
}
