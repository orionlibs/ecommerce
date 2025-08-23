package de.hybris.platform.cmscockpit.components.editor;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMediaReferencePreviewModel implements MediaReferencePreviewModel
{
    private final List<MediaReferencePreviewModelListener> listeners = new ArrayList<>();


    public void addMediaReferencePreviewModelListener(MediaReferencePreviewModelListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeMediaReferencePreviewModelListener(MediaReferencePreviewModelListener listener)
    {
        if(this.listeners.contains(listener))
        {
            this.listeners.remove(listener);
        }
    }


    public void fireValueChanged(TypedObject typedObject)
    {
        for(MediaReferencePreviewModelListener listener : this.listeners)
        {
            listener.valueChanged(typedObject);
        }
    }
}
