package de.hybris.platform.cmscockpit.components.editor;

import de.hybris.platform.cockpit.model.general.UIViewComponent;

public interface UIMediaReferencePreview extends UIViewComponent
{
    void setModel(MediaReferencePreviewModel paramMediaReferencePreviewModel);


    MediaReferencePreviewModel getModel();


    void removeMediaReferencePreviewListener(MediaReferencePreviewListener paramMediaReferencePreviewListener);


    void addMediaReferencePreviewListener(MediaReferencePreviewListener paramMediaReferencePreviewListener);
}
