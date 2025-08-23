package de.hybris.platform.admincockpit.jalo.cronjob;

import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import java.io.File;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultOrphanedFilesHandler extends AbstractBusinessService implements OrphanedFilesHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOrphanedFilesHandler.class.getName());


    public boolean process(Collection<File> files)
    {
        boolean success = true;
        for(File file : files)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("processing file: " + file + " ...");
            }
            if(file.exists() && file.isFile())
            {
                try
                {
                    if(!file.delete())
                    {
                        LOG.warn("Failing to delete " + file);
                        success = false;
                    }
                }
                catch(Exception e)
                {
                    LOG.warn("Failing to delete " + file);
                    LOG.warn(e.getMessage());
                    success = false;
                }
            }
        }
        return success;
    }
}
