package de.hybris.platform.solrserver.ant;

import de.hybris.platform.solrserver.util.VersionUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class SolrVersionPathPropertyTask extends Task
{
    private String name;
    private String version;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public void execute()
    {
        if(this.name == null || this.name.isEmpty())
        {
            throw new BuildException("Unknown name");
        }
        if(this.version == null || this.version.isEmpty())
        {
            throw new BuildException("Unknown version");
        }
        String versionPath = VersionUtils.getVersionPath(this.version);
        getProject().setProperty(this.name, versionPath);
    }
}
