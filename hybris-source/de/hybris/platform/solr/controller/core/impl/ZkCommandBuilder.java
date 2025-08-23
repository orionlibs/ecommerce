package de.hybris.platform.solr.controller.core.impl;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.CommandBuilder;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.util.OsUtils;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ZkCommandBuilder implements CommandBuilder
{
    protected static final String UNIX_SHELL_EXECUTABLE = "bash";
    protected static final String WINDOWS_SHELL_EXECUTABLE = "cmd.exe";
    private final Map<String, String> configuration;
    private final SolrInstance solrInstance;
    private final String command;


    public ZkCommandBuilder(Map<String, String> configuration, SolrInstance solrInstance, String command)
    {
        this.configuration = configuration;
        this.solrInstance = solrInstance;
        this.command = command;
    }


    public Map<String, String> getConfiguration()
    {
        return this.configuration;
    }


    public SolrInstance getSolrInstance()
    {
        return this.solrInstance;
    }


    public String getCommand()
    {
        return this.command;
    }


    public void build(ProcessBuilder processBuilder) throws SolrControllerException
    {
        String solrServerPath = this.configuration.get("SOLR_SERVER_PATH");
        if(OsUtils.isUnix())
        {
            buildForUnix(processBuilder, solrServerPath);
        }
        else if(OsUtils.isWindows())
        {
            buildForWindows(processBuilder, solrServerPath);
        }
        else
        {
            throw new SolrControllerException("Only Windows and Unix systems are supported");
        }
    }


    public void buildForUnix(ProcessBuilder processBuilder, String solrServerPath)
    {
        processBuilder.directory(Paths.get(solrServerPath, new String[0]).toFile());
        List<String> commandParams = new ArrayList<>();
        commandParams.add("bash");
        commandParams.add(Paths.get("server", new String[] {"scripts", "cloud-scripts", "zkcli.sh"}).toString());
        commandParams.add("-cmd");
        addCommand(commandParams, this.command);
        processBuilder.command().addAll(commandParams);
    }


    public void buildForWindows(ProcessBuilder processBuilder, String solrServerPath)
    {
        processBuilder.directory(Paths.get(solrServerPath, new String[0]).toFile());
        List<String> commandParams = new ArrayList<>();
        commandParams.add("cmd.exe");
        commandParams.add("/C");
        commandParams.add(Paths.get("server", new String[] {"scripts", "cloud-scripts", "zkcli.bat"}).toString());
        commandParams.add("-cmd");
        addCommand(commandParams, this.command);
        processBuilder.command().addAll(commandParams);
    }


    protected void addCommand(List<String> commandParams, String command)
    {
        String[] commandParts = command.split("\\s+");
        commandParams.addAll(Arrays.asList(commandParts));
    }
}
