package de.hybris.platform.solr.controller.commands;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.CommandBuilder;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.core.impl.SolrCommandBuilder;
import de.hybris.platform.solr.controller.core.impl.SolrCommonParamsCommandBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StopSolrServerCommand extends AbstractCommand
{
    private static final Logger LOG = Logger.getLogger(StopSolrServerCommand.class.getName());
    protected static final String SOLR_STOP_COMMAND = "stop";


    public Integer apply(Map<String, String> configuration)
    {
        try
        {
            SolrInstance solrInstance = getSolrInstanceForName(configuration);
            executeCommand(configuration, solrInstance);
            return Integer.valueOf(0);
        }
        catch(SolrControllerException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), (Throwable)e);
            return Integer.valueOf(1);
        }
    }


    protected void executeCommand(Map<String, String> configuration, SolrInstance solrInstance)
    {
        LOG.log(Level.INFO, "Stopping Solr server for instance {0}", solrInstance);
        Collection<CommandBuilder> commandBuilders = Arrays.asList(new CommandBuilder[] {(CommandBuilder)new SolrCommandBuilder(configuration, solrInstance, "stop"), (CommandBuilder)new SolrCommonParamsCommandBuilder(configuration, solrInstance)});
        getCommandExecutor().execute(commandBuilders, new de.hybris.platform.solr.controller.core.CommandOption[0]);
    }
}
