package de.hybris.platform.cmscockpit.components.editor;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;

public interface MediaReferencePreviewListener
{
    void valueChanged(TypedObject paramTypedObject);


    void uploadImageStream(TypedObject paramTypedObject, MediaEditorSectionConfiguration.MediaContent paramMediaContent);


    void clearImageStream(TypedObject paramTypedObject);


    void editImage(TypedObject paramTypedObject);


    void createImage(TypedObject paramTypedObject);
}
