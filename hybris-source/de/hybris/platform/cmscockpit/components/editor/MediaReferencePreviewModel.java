package de.hybris.platform.cmscockpit.components.editor;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;

public interface MediaReferencePreviewModel
{
    TypedObject getSource();


    void setSource(TypedObject paramTypedObject);


    MediaEditorSectionConfiguration.MediaContent getBaseMediaProperties();


    TypedObject getUploadMediaStream(TypedObject paramTypedObject, byte[] paramArrayOfbyte, String paramString1, String paramString2);


    void addMediaReferencePreviewModelListener(MediaReferencePreviewModelListener paramMediaReferencePreviewModelListener);


    void removeMediaReferencePreviewModelListener(MediaReferencePreviewModelListener paramMediaReferencePreviewModelListener);


    void removeCurrentImageStream(TypedObject paramTypedObject);
}
