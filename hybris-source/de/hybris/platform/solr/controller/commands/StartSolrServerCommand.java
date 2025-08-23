package de.hybris.platform.solr.controller.commands;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.CommandBuilder;
import de.hybris.platform.solr.controller.core.CommandOption;
import de.hybris.platform.solr.controller.core.CommandResult;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.core.SolrServerMode;
import de.hybris.platform.solr.controller.core.impl.SolrCommandBuilder;
import de.hybris.platform.solr.controller.core.impl.SolrCommonParamsCommandBuilder;
import de.hybris.platform.solr.controller.util.ConfigurationUtils;
import de.hybris.platform.solr.controller.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartSolrServerCommand extends AbstractCommand
{
    private static final Logger LOG = Logger.getLogger(StartSolrServerCommand.class.getName());
    protected static final String SOLR_START_COMMAND = "start";
    protected static final String SOLR_STOP_COMMAND = "stop";
    protected static final String SOLR_STATUS_COMMAND = "status";
    protected static final String RUNNING_CHECK_REGEX = "Solr process \\d+ running on port (\\d+)\\s*(\\{.*\\})?";


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
        boolean restart = false;
        ServerStatus serverStatus = getSolrServerStatus(configuration, solrInstance);
        if(serverStatus.getStatus().equals(ServerStatus.Status.STARTED))
        {
            LOG.log(Level.INFO, "Detected Solr server running on the same port for instance {0}", solrInstance);
            if(ConfigurationUtils.isForceRestart(configuration))
            {
                restart = true;
            }
            else if(!isCorrespondingServerForInstance(solrInstance, serverStatus))
            {
                throw new SolrControllerException(
                                MessageFormat.format("Detected different Solr server running on the same port for instance {0}", new Object[] {solrInstance}));
            }
        }
        else if(serverStatus.getStatus().equals(ServerStatus.Status.UNKNOWN))
        {
            LOG.log(Level.INFO, "Detected Solr server running on the same port for instance {0}", solrInstance);
            if(ConfigurationUtils.isForceRestart(configuration))
            {
                restart = true;
            }
            else
            {
                throw new SolrControllerException(
                                MessageFormat.format("Unknown status for Solr server running on the same port for instance {0}", new Object[] {solrInstance}));
            }
        }
        if(restart)
        {
            stopSolrServer(configuration, solrInstance);
        }
        startSolrServer(configuration, solrInstance);
        if(SolrServerMode.CLOUD.equals(solrInstance.getMode()) && solrInstance.isZkUpdateConfig())
        {
            UploadSolrConfigCommand uploadSolrConfigCommand = new UploadSolrConfigCommand();
            uploadSolrConfigCommand.executeCommand(configuration, solrInstance);
            LOG.log(Level.INFO, "Some cluster-wide properties need to be set before any Solr node starts up (e.g.: urlScheme), due to this we have to restart the Solr server");
            stopSolrServer(configuration, solrInstance);
            startSolrServer(configuration, solrInstance);
        }
    }


    protected void startSolrServer(Map<String, String> configuration, SolrInstance solrInstance) throws SolrControllerException
    {
        LOG.log(Level.INFO, "Starting Solr server for instance {0}", solrInstance);
        Collection<CommandBuilder> commandBuilders = Arrays.asList(new CommandBuilder[] {(CommandBuilder)new SolrCommandBuilder(configuration, solrInstance, "start"), (CommandBuilder)new SolrCommonParamsCommandBuilder(configuration, solrInstance),
                        processBuilder -> processBuilder.environment().put("SOLR_ULIMIT_CHECKS", Boolean.FALSE.toString())});
        CommandResult commandResult = getCommandExecutor().execute(commandBuilders, new CommandOption[0]);
        if(commandResult.getExitValue() != 0)
        {
            throw new SolrControllerException(MessageFormat.format("Failed to start Solr server for instance {0}", new Object[] {solrInstance}));
        }
    }


    protected void stopSolrServer(Map<String, String> configuration, SolrInstance solrInstance) throws SolrControllerException
    {
        LOG.log(Level.INFO, "Stopping Solr server for instance {0}", solrInstance);
        Collection<CommandBuilder> commandBuilders = Arrays.asList(new CommandBuilder[] {(CommandBuilder)new SolrCommandBuilder(configuration, solrInstance, "stop"), (CommandBuilder)new SolrCommonParamsCommandBuilder(configuration, solrInstance)});
        CommandResult commandResult = getCommandExecutor().execute(commandBuilders, new CommandOption[0]);
        if(commandResult.getExitValue() != 0)
        {
            throw new SolrControllerException(MessageFormat.format("Failed to stop Solr server for instance {0}", new Object[] {solrInstance}));
        }
    }


    protected ServerStatus getSolrServerStatus(Map<String, String> configuration, SolrInstance solrInstance)
    {
        LOG.log(Level.INFO, "Checking Solr server status for instance {0}", solrInstance);
        Collection<CommandBuilder> commandBuilders = Arrays.asList(new CommandBuilder[] {(CommandBuilder)new SolrCommandBuilder(configuration, solrInstance, "status"), (CommandBuilder)new SolrCommonParamsCommandBuilder(configuration, solrInstance)});
        CommandResult commandResult = getCommandExecutor().execute(commandBuilders, new CommandOption[] {CommandOption.COLLECT_OUTPUT});
        ServerStatus serverStatus = new ServerStatus();
        serverStatus.setPort(solrInstance.getPort());
        serverStatus.setStatus(ServerStatus.Status.STOPPED);
        Pattern pattern = Pattern.compile("Solr process \\d+ running on port (\\d+)\\s*(\\{.*\\})?", 32);
        Matcher matcher = pattern.matcher(commandResult.getOutput());
        while(matcher.find())
        {
            String statusPort = matcher.group(1);
            String statusJson = matcher.group(2);
            if(Objects.equals(Integer.toString(solrInstance.getPort()), statusPort))
            {
                serverStatus.setStatus(ServerStatus.Status.UNKNOWN);
                String solrHome = extractSolrHome(statusJson);
                String version = extractVersion(statusJson);
                if(StringUtils.isNotBlank(solrHome) && StringUtils.isNotBlank(version))
                {
                    serverStatus.setStatus(ServerStatus.Status.STARTED);
                    serverStatus.setSolrHome(solrHome);
                    serverStatus.setVersion(version);
                }
            }
        }
        return serverStatus;
    }


    protected String extractSolrHome(String statusJson)
    {
        if(StringUtils.isBlank(statusJson))
        {
            return null;
        }
        String solrHomePrefix = "\"solr_home\":\"";
        int solrHomeStart = statusJson.indexOf("\"solr_home\":\"");
        int solrHomeEnd = statusJson.indexOf("\",", solrHomeStart);
        return (solrHomeStart == -1 || solrHomeEnd == -1) ? null :
                        statusJson.substring(solrHomeStart + "\"solr_home\":\"".length() + 1, solrHomeEnd);
    }


    protected String extractVersion(String statusJson)
    {
        if(StringUtils.isBlank(statusJson))
        {
            return null;
        }
        String versionPrefix = "\"version\":\"";
        int versionStart = statusJson.indexOf("\"version\":\"");
        int versionEnd = statusJson.indexOf("\",", versionStart);
        return (versionStart == -1 || versionEnd == -1) ? null :
                        statusJson.substring(versionStart + "\"version\":\"".length() + 1, versionEnd);
    }


    protected boolean isCorrespondingServerForInstance(SolrInstance solrInstance, ServerStatus serverStatus) throws SolrControllerException
    {
        try
        {
            String expectedSolrHome = solrInstance.getConfigDir();
            Path expectedSolrHomePath = Paths.get(expectedSolrHome, new String[0]);
            String solrHome = serverStatus.getSolrHome();
            Path solrHomePath = Paths.get(solrHome, new String[0]);
            return Files.isSameFile(expectedSolrHomePath, solrHomePath);
        }
        catch(IOException e)
        {
            throw new SolrControllerException("Failed to check running Solr server for instance " + solrInstance, e);
        }
    }
}
