package de.hybris.platform.cockpit.services.label;

import de.hybris.platform.cockpit.model.meta.TypedObject;

public interface ObjectLabelProvider
{
    String getLabel(TypedObject paramTypedObject);


    String getLabel(TypedObject paramTypedObject, String paramString);


    String getDescription(TypedObject paramTypedObject);


    String getDescription(TypedObject paramTypedObject, String paramString);


    String getIconPath(TypedObject paramTypedObject);


    String getIconPath(TypedObject paramTypedObject, String paramString);
}
