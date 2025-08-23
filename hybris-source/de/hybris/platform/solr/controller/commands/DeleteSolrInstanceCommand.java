package de.hybris.platform.solr.controller.commands;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.util.FileUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteSolrInstanceCommand extends AbstractCommand
{
    private static final Logger LOG = Logger.getLogger(DeleteSolrInstanceCommand.class.getName());


    public Integer apply(Map<String, String> configuration)
    {
        try
        {
            SolrInstance solrInstance = getSolrInstanceForName(configuration);
            executeCommand(solrInstance);
            return Integer.valueOf(0);
        }
        catch(SolrControllerException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), (Throwable)e);
            return Integer.valueOf(1);
        }
    }


    protected void executeCommand(SolrInstance solrInstance) throws SolrControllerException
    {
        try
        {
            Path instanceConfigDirectory = Paths.get(solrInstance.getConfigDir(), new String[0]);
            if(instanceConfigDirectory.toFile().exists())
            {
                LOG.log(Level.INFO, "Deleting config directory ''{0}'' for Solr instance {1}", new Object[] {instanceConfigDirectory, solrInstance});
                FileUtils.deleteDirectory(instanceConfigDirectory);
            }
            Path instanceDataDirectory = Paths.get(solrInstance.getDataDir(), new String[0]);
            if(instanceDataDirectory.toFile().exists())
            {
                LOG.log(Level.INFO, "Deleting data directory ''{0}'' for Solr instance {1}", new Object[] {instanceDataDirectory, solrInstance});
                FileUtils.deleteDirectory(instanceDataDirectory);
            }
            Path instanceLogDirectory = Paths.get(solrInstance.getLogDir(), new String[0]);
            if(instanceLogDirectory.toFile().exists())
            {
                LOG.log(Level.INFO, "Deleting log directory ''{0}'' for Solr instance {1}", new Object[] {instanceLogDirectory, solrInstance});
                FileUtils.deleteDirectory(instanceLogDirectory);
            }
        }
        catch(IOException e)
        {
            throw new SolrControllerException("Failed to delete Solr instance", e);
        }
    }
}
