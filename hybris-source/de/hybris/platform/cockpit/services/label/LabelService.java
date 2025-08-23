package de.hybris.platform.cockpit.services.label;

import de.hybris.platform.cockpit.model.meta.TypedObject;

public interface LabelService
{
    @Deprecated
    String getObjectTextLabel(TypedObject paramTypedObject);


    String getObjectTextLabelForTypedObject(TypedObject paramTypedObject);


    @Deprecated
    String getObjectDescription(TypedObject paramTypedObject);


    String getObjectDescriptionForTypedObject(TypedObject paramTypedObject);


    @Deprecated
    String getObjectIconPath(TypedObject paramTypedObject);


    String getObjectIconPathForTypedObject(TypedObject paramTypedObject);
}
