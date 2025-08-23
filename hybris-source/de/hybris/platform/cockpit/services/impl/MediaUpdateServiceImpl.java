package de.hybris.platform.cockpit.services.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.MediaUpdateService;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class MediaUpdateServiceImpl implements MediaUpdateService
{
    private static final Logger LOG = LoggerFactory.getLogger(MediaUpdateServiceImpl.class);
    private ModelService modelService;


    public TypedObject updateMediaBinaryStream(TypedObject typedObject, Media zkMedia)
    {
        return updateMediaBinaryStream(typedObject, zkMedia.getByteData(), zkMedia.getContentType(), zkMedia.getName());
    }


    public TypedObject updateMediaBinaryStream(TypedObject typedObject, byte[] byteStream, String contentType, String name)
    {
        TypedObject ret = null;
        try
        {
            Media mediaObject = null;
            if(typedObject.getObject() instanceof MediaModel)
            {
                MediaModel mediaModel = (MediaModel)typedObject.getObject();
                getModelService().refresh(mediaModel);
                mediaObject = (Media)getModelService().getSource(mediaModel);
                mediaObject.setData(byteStream);
                mediaModel.setMime(contentType);
                mediaModel.setRealfilename(name);
                getModelService().save(mediaModel);
                ret = UISessionUtils.getCurrentSession().getTypeService().wrapItem(mediaObject);
            }
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Some errors have occured while updating a media object [image updating].", (Throwable)e);
        }
        return ret;
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    public MediaEditorSectionConfiguration.MediaContent retrieveMediaBinaryStream(TypedObject typedObject)
    {
        MediaEditorSectionConfiguration.MediaContent ret = null;
        try
        {
            if(typedObject != null)
            {
                Media realMedia = null;
                ret = new MediaEditorSectionConfiguration.MediaContent();
                if(typedObject.getObject() instanceof MediaModel)
                {
                    realMedia = (Media)UISessionUtils.getCurrentSession().getModelService().getSource(typedObject.getObject());
                }
                if(realMedia != null)
                {
                    ret.setData(realMedia.getData());
                    ret.setMimeType(realMedia.getMime());
                    ret.setName(realMedia.getFileName());
                    ret.setUrl(realMedia.getURL());
                }
            }
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Error occures while retrieving an Media!");
        }
        return ret;
    }


    public TypedObject removeCurrentImageStream(TypedObject typedObject)
    {
        Media realMedia = null;
        TypedObject ret = null;
        try
        {
            if(typedObject.getObject() instanceof MediaModel)
            {
                realMedia = (Media)UISessionUtils.getCurrentSession().getModelService().getSource(typedObject.getObject());
            }
            if(realMedia != null)
            {
                realMedia.setData(new byte[0]);
                ret = UISessionUtils.getCurrentSession().getTypeService().wrapItem(realMedia);
            }
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Error occures while clearing an image stream!");
        }
        return ret;
    }
}
