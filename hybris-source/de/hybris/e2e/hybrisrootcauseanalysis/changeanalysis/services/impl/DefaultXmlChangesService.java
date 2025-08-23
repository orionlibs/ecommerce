package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesXmlService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import org.apache.log4j.Logger;

public class DefaultXmlChangesService implements E2EChangesXmlService
{
    private static final Logger LOG = Logger.getLogger(DefaultXmlChangesService.class.getName());
    private MediaService mediaService;
    private String nameFile;
    private String code;


    public MediaModel getMedia(String code)
    {
        MediaModel returnMedia = null;
        try
        {
            returnMedia = this.mediaService.getMedia(code);
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug(e);
        }
        return returnMedia;
    }


    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public void setNameFile(String nameFile)
    {
        this.nameFile = nameFile;
    }


    public String getNameFile()
    {
        return this.nameFile;
    }


    public String getCode()
    {
        return this.code;
    }
}
