package de.hybris.platform.cms2.common.exceptions;

import de.hybris.platform.cms2.exceptions.AttributePermissionException;
import de.hybris.platform.cms2.exceptions.TypePermissionException;
import de.hybris.platform.util.localization.Localization;

public class PermissionExceptionUtils
{
    public static TypePermissionException createTypePermissionException(String permissionName, String typeCode)
    {
        String errorMsg = Localization.getLocalizedString("unauthorized.type.insufficient.permission", new Object[] {permissionName, typeCode});
        return new TypePermissionException(errorMsg);
    }


    public static AttributePermissionException createAttributePermissionException(String permissionName, String typeCode, String qualifier)
    {
        String errorMsg = Localization.getLocalizedString("unauthorized.attribute.insufficient.permission", new Object[] {permissionName, typeCode, qualifier});
        return new AttributePermissionException(errorMsg);
    }
}
