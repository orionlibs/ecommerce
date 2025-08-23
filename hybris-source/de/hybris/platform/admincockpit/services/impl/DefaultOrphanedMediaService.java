package de.hybris.platform.admincockpit.services.impl;

import de.hybris.platform.admincockpit.jalo.cronjob.OrphanedFilesHandler;
import de.hybris.platform.admincockpit.services.OrphanedMediaResult;
import de.hybris.platform.admincockpit.services.OrphanedMediaService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.media.impl.OrphanedMediaDao;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrphanedMediaService extends AbstractBusinessService implements OrphanedMediaService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOrphanedMediaService.class);
    private OrphanedFilesHandler orphanedFilesHandler;
    private OrphanedMediaDao orphanedMediaDao;


    public boolean processFiles(Collection<File> files)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("processing orphaned files using handler: " + this);
        }
        return this.orphanedFilesHandler.process(files);
    }


    public OrphanedMediaResult searchForOrphanedMediaFiles(File folder)
    {
        return searchForOrphanedMediaFiles(folder, 200);
    }


    public OrphanedMediaResult searchForOrphanedMediaFiles(File folder, int range)
    {
        Collection<File> orphanedMedias = new ArrayList<>();
        Collection<File> notHybrisMedias = new ArrayList<>();
        Map<Object, Object> fsvalues = new HashMap<>();
        int filesOverall = 0;
        int mediasCount = this.orphanedMediaDao.getMediasCount();
        for(Iterator<File> fileIter = FileUtils.iterateFiles(folder, null, true); fileIter.hasNext(); )
        {
            filesOverall++;
            File file = fileIter.next();
            String filebasename = FilenameUtils.getBaseName(file.getName());
            int start = 0;
            int total = 0;
            try
            {
                PK.parse(filebasename);
                fsvalues.put("myPK", filebasename);
                boolean relatedMedaExists = false;
                do
                {
                    Collection<MediaModel> res = this.orphanedMediaDao.findOrphanedMedias(start, range, fsvalues);
                    total = res.size();
                    start += range;
                    if(total <= 0)
                    {
                        continue;
                    }
                    relatedMedaExists = true;
                }
                while(start < mediasCount && !relatedMedaExists);
                if(!relatedMedaExists)
                {
                    orphanedMedias.add(file);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Added " + file + " to orphaned media.");
                    }
                }
            }
            catch(de.hybris.platform.core.PK.PKException pke)
            {
                notHybrisMedias.add(file);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Added " + file + " to \"not hybris\" media.");
                }
            }
        }
        return new OrphanedMediaResult(filesOverall, orphanedMedias, notHybrisMedias);
    }


    @Required
    public void setOrphanedFilesHandler(OrphanedFilesHandler orphanedFilesHandler)
    {
        this.orphanedFilesHandler = orphanedFilesHandler;
    }


    @Required
    public void setOrphanedMediaDao(OrphanedMediaDao orphanedMediaDao)
    {
        this.orphanedMediaDao = orphanedMediaDao;
    }
}
