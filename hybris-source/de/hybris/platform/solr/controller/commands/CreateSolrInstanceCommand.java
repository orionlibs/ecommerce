package de.hybris.platform.solr.controller.commands;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.util.FileUtils;
import de.hybris.platform.solr.controller.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateSolrInstanceCommand extends AbstractCommand
{
    private static final Logger LOG = Logger.getLogger(CreateSolrInstanceCommand.class.getName());


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


    protected void executeCommand(Map<String, String> configuration, SolrInstance solrInstance) throws SolrControllerException
    {
        try
        {
            createInstanceConfigDirectory(solrInstance, configuration);
            createInstanceDataDirectory(solrInstance);
            createInstanceLogDirectory(solrInstance);
        }
        catch(IOException e)
        {
            throw new SolrControllerException("Failed to create Solr instance", e);
        }
    }


    protected void createInstanceConfigDirectory(SolrInstance solrInstance, Map<String, String> configuration) throws IOException
    {
        Path instanceConfigDirectory = Paths.get(solrInstance.getConfigDir(), new String[0]);
        if(!instanceConfigDirectory.toFile().exists())
        {
            LOG.log(Level.INFO, "Creating config directory ''{0}'' for Solr instance {1}", new Object[] {instanceConfigDirectory, solrInstance});
            Files.createDirectories(instanceConfigDirectory, (FileAttribute<?>[])new FileAttribute[0]);
            String solrServerPath = configuration.get("SOLR_SERVER_PATH");
            List<String> configFiles = Arrays.asList(new String[] {"/server/resources/log4j2.xml", "/server/solr/security.json.example", "/server/solr/solr.p12", "/server/solr/solr_client.p12", "/server/solr/solr.xml", "/server/solr/zoo.cfg"});
            for(String configFile : configFiles)
            {
                Path sourceConfigFilePath = Paths.get(solrServerPath, new String[] {configFile});
                Path configFilePath = instanceConfigDirectory.resolve(Paths.get(StringUtils.removeEnd(configFile, ".example"), new String[0]).getFileName());
                Files.copy(sourceConfigFilePath, configFilePath, new java.nio.file.CopyOption[0]);
            }
            Path sourceConfigSetsDirectory = Paths.get(solrServerPath, new String[] {"/server/solr/configsets"});
            Path configSetsDirectory = instanceConfigDirectory.resolve("configsets");
            FileUtils.copyDirectory(sourceConfigSetsDirectory, configSetsDirectory);
        }
    }


    protected void createInstanceDataDirectory(SolrInstance solrInstance) throws IOException
    {
        Path instanceDataDirectory = Paths.get(solrInstance.getDataDir(), new String[0]);
        if(!instanceDataDirectory.toFile().exists())
        {
            LOG.log(Level.INFO, "Creating data directory ''{0}'' for Solr instance {1}", new Object[] {instanceDataDirectory, solrInstance});
            Files.createDirectories(instanceDataDirectory, (FileAttribute<?>[])new FileAttribute[0]);
        }
    }


    protected void createInstanceLogDirectory(SolrInstance solrInstance) throws IOException
    {
        Path instanceLogDirectory = Paths.get(solrInstance.getLogDir(), new String[0]);
        if(!instanceLogDirectory.toFile().exists())
        {
            LOG.log(Level.INFO, "Creating log directory ''{0}'' for Solr instance {1}", new Object[] {instanceLogDirectory, solrInstance});
            Files.createDirectories(instanceLogDirectory, (FileAttribute<?>[])new FileAttribute[0]);
        }
    }
}
