package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

public interface PermissionCRUDService
{
    boolean canReadType(ComposedTypeModel paramComposedTypeModel);


    boolean canReadType(String paramString);


    boolean canReadAttribute(AttributeDescriptorModel paramAttributeDescriptorModel);


    boolean canReadAttribute(String paramString1, String paramString2);


    boolean canChangeType(ComposedTypeModel paramComposedTypeModel);


    boolean canChangeType(String paramString);


    boolean canChangeAttribute(AttributeDescriptorModel paramAttributeDescriptorModel);


    boolean canChangeAttribute(String paramString1, String paramString2);


    boolean canCreateTypeInstance(ComposedTypeModel paramComposedTypeModel);


    boolean canCreateTypeInstance(String paramString);


    boolean canRemoveTypeInstance(ComposedTypeModel paramComposedTypeModel);


    boolean canRemoveTypeInstance(String paramString);


    boolean canChangeTypePermission(ComposedTypeModel paramComposedTypeModel);


    boolean canChangeTypePermission(String paramString);


    boolean canChangeAttributePermission(AttributeDescriptorModel paramAttributeDescriptorModel);


    boolean canChangeAttributePermission(String paramString1, String paramString2);
}
