package de.hybris.platform.cmscockpit.components.editor;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Div;

public abstract class AbstractMediaReferencePreview extends Div implements UIMediaReferencePreview
{
    protected List<MediaReferencePreviewListener> listeners = new ArrayList<>();


    public void removeMediaReferencePreviewListener(MediaReferencePreviewListener listener)
    {
        if(this.listeners.contains(listener))
        {
            this.listeners.remove(listener);
        }
    }


    public void addMediaReferencePreviewListener(MediaReferencePreviewListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    protected void fireValueChanged(TypedObject value)
    {
        for(MediaReferencePreviewListener listener : this.listeners)
        {
            listener.clearImageStream(value);
        }
    }


    protected void fireUploadImageStream(TypedObject value, MediaEditorSectionConfiguration.MediaContent mediaData)
    {
        for(MediaReferencePreviewListener listener : this.listeners)
        {
            listener.uploadImageStream(value, mediaData);
        }
    }


    protected void fireClearImageStream(TypedObject value)
    {
        for(MediaReferencePreviewListener listener : this.listeners)
        {
            listener.clearImageStream(value);
        }
    }


    protected void fireEditImage(TypedObject value)
    {
        for(MediaReferencePreviewListener listener : this.listeners)
        {
            listener.editImage(value);
        }
    }


    protected void fireCreateImage(TypedObject value)
    {
        for(MediaReferencePreviewListener listener : this.listeners)
        {
            listener.createImage(value);
        }
    }
}
