package de.hybris.platform.media.storage.impl;

import de.hybris.platform.media.services.MediaStorageInitializer;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LocalFileMediaStorageCleaner implements MediaStorageInitializer
{
    private static final Logger LOG = Logger.getLogger(LocalFileMediaStorageCleaner.class);
    private boolean cleanOnInit = true;
    private List<File> replicationDirs;


    public void onInitialize()
    {
        if(this.cleanOnInit)
        {
            for(File dir : this.replicationDirs)
            {
                File[] files;
                if(!dir.canRead())
                {
                    LOG.warn("No read access to media directory (" + dir + " ). Cannot delete media files");
                    return;
                }
                try
                {
                    files = dir.listFiles((FilenameFilter)new DummyFileFilter("dummy.txt"));
                }
                catch(SecurityException e)
                {
                    LOG.warn("No read access to media directory (" + dir + " ). Cannot delete media files. SecurityException is occured: " + e
                                    .getMessage());
                    return;
                }
                if(files == null)
                {
                    LOG.warn("Path for media directory defined in local/project.properties isn't a directory. Cannot delete media files");
                    return;
                }
                for(int i = 0; i < files.length; i++)
                {
                    File fileDel = files[i];
                    if(!dir.canWrite())
                    {
                        LOG.warn("No write access to media file(" + fileDel.getAbsolutePath() + " ). Cannot delete this file");
                    }
                    try
                    {
                        if(fileDel.isDirectory())
                        {
                            try
                            {
                                FileUtils.deleteDirectory(fileDel);
                            }
                            catch(IOException e)
                            {
                                LOG.warn("Couldn't delete media directory (" + fileDel.getAbsolutePath());
                            }
                        }
                        else if(!fileDel.delete())
                        {
                            LOG.warn("Couldn't delete media (" + fileDel.getAbsolutePath());
                        }
                    }
                    catch(SecurityException e)
                    {
                        LOG.warn("Cannot delete media (" + fileDel.getAbsolutePath() + " ). SecurityException is occured: " + e
                                        .getMessage());
                    }
                }
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Clean on init feature for " + getClass().getSimpleName() + " is disabled in properties file");
        }
    }


    @Required
    public void setReplicationDirs(List<File> replicationDirs)
    {
        this.replicationDirs = replicationDirs;
    }


    public void setCleanOnInit(boolean cleanOnInit)
    {
        this.cleanOnInit = cleanOnInit;
    }


    public void onUpdate()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(">>> Skipping media storage initialization for update system operation");
        }
    }
}
