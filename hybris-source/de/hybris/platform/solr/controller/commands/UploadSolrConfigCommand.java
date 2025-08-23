package de.hybris.platform.solr.controller.commands;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.CommandBuilder;
import de.hybris.platform.solr.controller.core.CommandResult;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.core.SolrServerMode;
import de.hybris.platform.solr.controller.core.impl.SolrCommandBuilder;
import de.hybris.platform.solr.controller.core.impl.ZkCommandBuilder;
import de.hybris.platform.solr.controller.util.StringUtils;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadSolrConfigCommand extends AbstractCommand
{
    private static final Logger LOG = Logger.getLogger(UploadSolrConfigCommand.class.getName());
    protected static final String SOLR_ZK_CP_COMMAND = "zk cp";
    protected static final String SOLR_ZK_UPCONFIG_COMMAND = "zk upconfig";
    protected static final String ZK_CLUSTERPROP_COMMAND = "clusterprop";
    protected static final String CONFIGSETS_PATH = "configsets";
    protected static final String CORES_PATH = "cores";


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
        CreateSolrInstanceCommand createSolrInstanceCommand = new CreateSolrInstanceCommand();
        createSolrInstanceCommand.executeCommand(configuration, solrInstance);
        if(SolrServerMode.CLOUD.equals(solrInstance.getMode()))
        {
            String zkHost = resolveZkHost(solrInstance);
            LOG.log(Level.INFO, "Uploading configuration for instance {0} to ZK host {1}", new Object[] {solrInstance, zkHost});
            uploadConfigFiles(configuration, solrInstance, zkHost);
            uploadConfigSets(configuration, solrInstance, zkHost);
            setClusterProperties(configuration, solrInstance, zkHost);
        }
        else
        {
            LOG.log(Level.INFO, "Configuration upload skipped, you are running Solr server in standalone mode.");
        }
    }


    protected String resolveZkHost(SolrInstance solrInstance)
    {
        if(StringUtils.isNotBlank(solrInstance.getZkHost()))
        {
            return solrInstance.getZkHost();
        }
        int port = solrInstance.getPort() + 1000;
        return "localhost:" + port;
    }


    protected void uploadConfigFiles(Map<String, String> configuration, SolrInstance solrInstance, String zkHost) throws SolrControllerException
    {
        List<String> configFiles = Arrays.asList(new String[] {"security.json"});
        for(String configFile : configFiles)
        {
            Path configFilePath = Paths.get(solrInstance.getConfigDir(), new String[] {configFile});
            Collection<CommandBuilder> commandBuilders = Arrays.asList(new CommandBuilder[] {(CommandBuilder)new SolrCommandBuilder(configuration, solrInstance, "zk cp"), processBuilder -> {
                List<String> commandParams = new ArrayList<>();
                commandParams.add("file:" + configFilePath.toString());
                commandParams.add("zk:" + configFile);
                commandParams.add("-z");
                commandParams.add(zkHost);
                processBuilder.command().addAll(commandParams);
            }});
            CommandResult commandResult = getCommandExecutor().execute(commandBuilders, new de.hybris.platform.solr.controller.core.CommandOption[0]);
            if(commandResult.getExitValue() != 0)
            {
                throw new SolrControllerException(
                                MessageFormat.format("Failed to upload config file to ZK: file={0}, zk={1}", new Object[] {configFile, zkHost}));
            }
        }
    }


    protected void uploadConfigSets(Map<String, String> configuration, SolrInstance solrInstance, String zkHost) throws SolrControllerException
    {
        File configSets = new File(Paths.get(solrInstance.getConfigDir(), new String[] {"configsets"}).toString());
        for(File configSet : configSets.listFiles())
        {
            Path configSetPath = Paths.get(solrInstance.getConfigDir(), new String[] {"configsets", configSet.getName(), "conf"});
            Collection<CommandBuilder> commandBuilders = Arrays.asList(new CommandBuilder[] {(CommandBuilder)new SolrCommandBuilder(configuration, solrInstance, "zk upconfig"), processBuilder -> {
                List<String> commandParams = new ArrayList<>();
                commandParams.add("-d");
                commandParams.add(configSetPath.toString());
                commandParams.add("-n");
                commandParams.add(configSet.getName());
                commandParams.add("-z");
                commandParams.add(zkHost);
                processBuilder.command().addAll(commandParams);
            }});
            CommandResult commandResult = getCommandExecutor().execute(commandBuilders, new de.hybris.platform.solr.controller.core.CommandOption[0]);
            if(commandResult.getExitValue() != 0)
            {
                throw new SolrControllerException(
                                MessageFormat.format("Failed to upload config set to ZK: name={0}, zk={1}", new Object[] {configSet.getName(), zkHost}));
            }
        }
    }


    protected void setClusterProperties(Map<String, String> configuration, SolrInstance solrInstance, String zkHost) throws SolrControllerException
    {
        Map<String, String> zkProperties = solrInstance.getZkProperties();
        if(zkProperties == null || zkProperties.isEmpty())
        {
            return;
        }
        for(Map.Entry<String, String> entry : zkProperties.entrySet())
        {
            Collection<CommandBuilder> commandBuilders = Arrays.asList(new CommandBuilder[] {(CommandBuilder)new ZkCommandBuilder(configuration, solrInstance, "clusterprop"), processBuilder -> {
                List<String> commandParams = new ArrayList<>();
                commandParams.add("-name");
                commandParams.add((String)entry.getKey());
                commandParams.add("-val");
                commandParams.add((String)entry.getValue());
                commandParams.add("-z");
                commandParams.add(zkHost);
                processBuilder.command().addAll(commandParams);
            }});
            CommandResult commandResult = getCommandExecutor().execute(commandBuilders, new de.hybris.platform.solr.controller.core.CommandOption[0]);
            if(commandResult.getExitValue() != 0)
            {
                throw new SolrControllerException(MessageFormat.format("Failed to set ZK property: name={0}, value={1}, zk={2}", new Object[] {entry
                                .getKey(), entry.getValue(), zkHost}));
            }
        }
    }
}
