package de.hybris.platform.solrserver.ant;

import de.hybris.platform.solrserver.SolrServerException;
import de.hybris.platform.solrserver.strategies.impl.DefaultSolrServerCommandExecutor;
import de.hybris.platform.solrserver.util.VersionUtils;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class SolrServerCommandTask extends Task
{
    private String command;


    public String getCommand()
    {
        return this.command;
    }


    public void setCommand(String command)
    {
        this.command = command;
    }


    public void execute()
    {
        if(this.command == null || this.command.isEmpty())
        {
            throw new BuildException("Unknown command");
        }
        Map<String, String> configuration = new HashMap<>();
        for(Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)getProject().getProperties().entrySet())
        {
            if((((String)entry.getKey()).startsWith("solrserver.") || ((String)entry.getKey()).startsWith("instance.")) && entry
                            .getValue() instanceof String)
            {
                configuration.put(entry.getKey(), (String)entry.getValue());
            }
        }
        configuration.put("HYBRIS_CONFIG_PATH", getProject().getProperty("HYBRIS_CONFIG_DIR"));
        configuration.put("HYBRIS_DATA_PATH", getProject().getProperty("HYBRIS_DATA_DIR"));
        configuration.put("HYBRIS_LOG_PATH", getProject().getProperty("HYBRIS_LOG_DIR"));
        String extDir = getProject().getProperty("ext.solrserver.path");
        String solrServerVersion = configuration.get("solrserver.solr.server.version");
        String versionPath = VersionUtils.getVersionPath(solrServerVersion);
        Path solrServerPath = Paths.get(extDir, new String[] {"resources", "solr", versionPath, "server"});
        configuration.put("SOLR_SERVER_PATH", solrServerPath.toString());
        Object object = new Object(this, configuration);
        DefaultSolrServerCommandExecutor solrServerCommandExecutor = new DefaultSolrServerCommandExecutor();
        try
        {
            solrServerCommandExecutor.executeCommand(this.command, object.getConfiguration());
        }
        catch(SolrServerException e)
        {
            throw new BuildException(e);
        }
    }
}
