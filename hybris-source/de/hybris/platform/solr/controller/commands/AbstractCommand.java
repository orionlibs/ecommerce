package de.hybris.platform.solr.controller.commands;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.CommandExecutor;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.core.SolrInstanceFactory;
import de.hybris.platform.solr.controller.core.impl.DefaultCommandExecutor;
import de.hybris.platform.solr.controller.core.impl.DefaultSolrInstanceFactory;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractCommand implements Function<Map<String, String>, Integer>
{
    private final SolrInstanceFactory solrInstanceFactory = (SolrInstanceFactory)new DefaultSolrInstanceFactory();
    private final CommandExecutor commandExecutor = (CommandExecutor)new DefaultCommandExecutor();


    protected SolrInstanceFactory getSolrInstanceFactory()
    {
        return this.solrInstanceFactory;
    }


    protected CommandExecutor getCommandExecutor()
    {
        return this.commandExecutor;
    }


    protected SolrInstance getSolrInstanceForName(Map<String, String> configuration) throws SolrControllerException
    {
        String instanceName = configuration.get("instance.name");
        if(instanceName == null)
        {
            instanceName = "default";
        }
        return getSolrInstanceFactory().getInstanceForName(configuration, instanceName);
    }


    protected Collection<SolrInstance> getSolrInstances(Map<String, String> configuration) throws SolrControllerException
    {
        return getSolrInstanceFactory().getInstances(configuration);
    }
}
