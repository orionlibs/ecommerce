package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cmscockpit.components.editor.AbstractMediaReferencePreviewModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.MediaUpdateService;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;
import org.zkoss.spring.SpringUtil;

public class DefaultMediaReferencePreviewModel extends AbstractMediaReferencePreviewModel
{
    private TypedObject source;
    private MediaUpdateService mediaUpdateService;


    public MediaUpdateService getMediaUpdateService()
    {
        if(this.mediaUpdateService == null)
        {
            this.mediaUpdateService = (MediaUpdateService)SpringUtil.getBean("mediaUpdateService");
        }
        return this.mediaUpdateService;
    }


    public TypedObject getSource()
    {
        return this.source;
    }


    public void setSource(TypedObject source)
    {
        this.source = source;
        fireValueChanged(source);
    }


    public MediaEditorSectionConfiguration.MediaContent getBaseMediaProperties()
    {
        return getMediaUpdateService().retrieveMediaBinaryStream(this.source);
    }


    public TypedObject getUploadMediaStream(TypedObject typedObject, byte[] data, String mimeType, String name)
    {
        return getMediaUpdateService().updateMediaBinaryStream(typedObject, data, mimeType, name);
    }


    public void removeCurrentImageStream(TypedObject typedObject)
    {
        getMediaUpdateService().removeCurrentImageStream(typedObject);
    }
}
