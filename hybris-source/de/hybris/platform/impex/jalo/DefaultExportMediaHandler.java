package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

public class DefaultExportMediaHandler extends AbstractExportMediaHandler
{
    private static final Logger LOG = Logger.getLogger(DefaultExportMediaHandler.class.getName());


    public String exportData(Media media)
    {
        if(media.getURL() == null || media.getURL().trim().length() == 0)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("skipping media " + media + " (has no data)");
            }
            return "";
        }
        try
        {
            if(getMediaFile() == null)
            {
                throw new ImpExException("missing 'media file' !");
            }
            if(!getMediaFile().containsEntry(media.getFileName()))
            {
                getMediaFile().startNewEntry(media.getFileName());
                copyMedia(media);
                getMediaFile().closeEntry();
            }
        }
        catch(Exception e)
        {
            LOG.error("Error while exporting media file: " + media.getFileName() + "\n" + Utilities.getStackTraceAsString(e));
        }
        return media.getFileName();
    }


    private void copyMedia(Media media) throws IOException, JaloBusinessException
    {
        InputStream mediaInputStream = media.getDataFromStreamSure();
        try
        {
            MediaUtil.copy(mediaInputStream, getMediaFile().getOutputStream(), false);
            if(mediaInputStream != null)
            {
                mediaInputStream.close();
            }
        }
        catch(Throwable throwable)
        {
            if(mediaInputStream != null)
            {
                try
                {
                    mediaInputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }
}
