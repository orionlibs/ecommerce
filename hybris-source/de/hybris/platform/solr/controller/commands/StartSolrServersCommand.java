package de.hybris.platform.solr.controller.commands;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.util.ConfigurationUtils;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StartSolrServersCommand extends AbstractCommand
{
    private static final Logger LOG = Logger.getLogger(StartSolrServersCommand.class.getName());
    protected static final String SOLR_START_COMMAND = "start";
    protected static final String SOLR_STOP_COMMAND = "stop";
    protected static final String SOLR_STATUS_COMMAND = "status";
    protected static final String RUNNING_CHECK_REGEX = "Solr process \\d+ running on port (\\d+)\\s*(\\{.*\\})?";


    public Integer apply(Map<String, String> configuration)
    {
        try
        {
            executeCommand(configuration);
            return Integer.valueOf(0);
        }
        catch(SolrControllerException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), (Throwable)e);
            return Integer.valueOf(1);
        }
    }


    protected void executeCommand(Map<String, String> configuration) throws SolrControllerException
    {
        LOG.info("Starting Solr servers ...");
        boolean failOnError = ConfigurationUtils.isFailOnError(configuration);
        List<SolrInstance> solrInstances = (List<SolrInstance>)getSolrInstances(configuration).stream().filter(SolrInstance::isAutostart).sorted(this::compareSolrInstances).collect(Collectors.toList());
        for(SolrInstance solrInstance : solrInstances)
        {
            try
            {
                StartSolrServerCommand startSolrServerCommand = new StartSolrServerCommand();
                startSolrServerCommand.executeCommand(configuration, solrInstance);
            }
            catch(SolrControllerException e)
            {
                if(failOnError)
                {
                    throw e;
                }
            }
        }
    }


    protected int compareSolrInstances(SolrInstance solrInstance1, SolrInstance solrInstance2)
    {
        return Integer.compare(solrInstance2.getPriority(), solrInstance1.getPriority());
    }
}
