package de.hybris.platform.solrserver.ant;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class CleanupPreviousVersionsTask extends Task
{
    private static final Logger LOG = Logger.getLogger(CleanupPreviousVersionsTask.class.getName());
    private String currentVersion;
    private String searchDirectory;
    private String versionType;


    public String getCurrentVersion()
    {
        return this.currentVersion;
    }


    public void setCurrentVersion(String currentVersion)
    {
        this.currentVersion = currentVersion;
    }


    public String getSearchDirectory()
    {
        return this.searchDirectory;
    }


    public void setSearchDirectory(String searchDirectory)
    {
        this.searchDirectory = searchDirectory;
    }


    public String getVersionType()
    {
        return this.versionType;
    }


    public void setVersionType(String versionType)
    {
        this.versionType = versionType;
    }


    public void execute()
    {
        validateAttributes();
        ArrayList<String> currentVersions = getPrunedCurrentVerions(this.currentVersion);
        for(File versionDirectory : getExistingVersionDirectories())
        {
            deleteEmptyVersionDirectory(versionDirectory);
            if(isUnusedVersionDirectory(currentVersions, versionDirectory))
            {
                File[] subDirectories = versionDirectory.listFiles(File::isDirectory);
                for(File subDir : subDirectories)
                {
                    deleteObsoleteDirectory(versionDirectory, subDirectories, subDir);
                }
            }
        }
    }


    protected boolean isUnusedVersionDirectory(ArrayList<String> currentVersions, File versionDirectory)
    {
        return !currentVersions.contains(versionDirectory.getName());
    }


    protected void deleteObsoleteDirectory(File versionDirectory, File[] subDirectories, File subDir)
    {
        if(this.versionType.equals(subDir.getName()))
        {
            if(subDirectories.length == 1)
            {
                LOG.log(Level.INFO, "Deleting obsolete solr directory {0}", versionDirectory.getAbsoluteFile());
                deleteDirectory(versionDirectory);
            }
            else
            {
                LOG.log(Level.INFO, "Deleting obsolete solr directory {0}", subDir.getAbsoluteFile());
                deleteDirectory(subDir);
            }
        }
    }


    protected void deleteDirectory(File directory)
    {
        try
        {
            FileUtils.deleteDirectory(directory);
        }
        catch(IOException e)
        {
            String msg = MessageFormat.format("Failed to delete obsolete solr version directory {0}", new Object[] {directory.getAbsoluteFile()});
            LOG.log(Level.WARNING, msg, e);
        }
    }


    protected void deleteEmptyVersionDirectory(File versionDirectory)
    {
        if(isDirectoryEmpty(versionDirectory))
        {
            deleteDirectory(versionDirectory);
        }
    }


    protected void validateAttributes()
    {
        if(this.currentVersion == null || this.currentVersion.isEmpty())
        {
            throw new BuildException("Unknown current version");
        }
        if(this.searchDirectory == null || this.searchDirectory.isEmpty())
        {
            throw new BuildException("Unknown search directory");
        }
        if(this.versionType == null || this.versionType.isEmpty() || !EnumUtils.isValidEnum(VersionTypes.class, this.versionType))
        {
            throw new BuildException("Unknown version type, allowed version types: " + Arrays.toString(VersionTypes.values()));
        }
    }


    protected File[] getExistingVersionDirectories()
    {
        File dir = FileUtils.getFile(new String[] {this.searchDirectory});
        if(dir.exists() && dir.isDirectory())
        {
            return dir.listFiles(File::isDirectory);
        }
        throw new BuildException("Search directory [" + dir.getAbsolutePath() + "] not found");
    }


    protected boolean isDirectoryEmpty(File file)
    {
        boolean result = false;
        if(file.isDirectory())
        {
            String[] files = file.list();
            if(files.length == 0)
            {
                result = true;
            }
        }
        return result;
    }


    protected ArrayList<String> getPrunedCurrentVerions(String versionEntry)
    {
        ArrayList<String> versions = new ArrayList<>();
        for(String version : StringUtils.split(versionEntry, ","))
        {
            versions.add(version.replaceFirst("(.*\\..*)\\..*", "$1"));
        }
        return versions;
    }
}
