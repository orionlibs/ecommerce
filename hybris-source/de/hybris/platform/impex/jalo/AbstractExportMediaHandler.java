package de.hybris.platform.impex.jalo;

import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.jalo.media.Media;
import java.security.InvalidParameterException;
import org.apache.log4j.Logger;

public abstract class AbstractExportMediaHandler implements MediaDataHandler
{
    private static final Logger LOG = Logger.getLogger(AbstractExportMediaHandler.class.getName());
    private ImpExZip mediaFile;


    protected AbstractExportMediaHandler()
    {
    }


    public AbstractExportMediaHandler(ImpExZip mediaFile)
    {
        if(mediaFile == null)
        {
            throw new InvalidParameterException("missing 'media file' !");
        }
        this.mediaFile = mediaFile;
    }


    public void setMediaFile(ImpExZip mediaFile)
    {
        if(mediaFile == null)
        {
            throw new InvalidParameterException("'media file' couldn't be nulll!");
        }
        this.mediaFile = mediaFile;
    }


    public ImpExZip getMediaFile()
    {
        return this.mediaFile;
    }


    public void importData(Media media, String path)
    {
        throw new UnsupportedOperationException("import operation isn't supported by this MediaDataHandler implementation!");
    }


    public void cleanUp()
    {
    }
}
