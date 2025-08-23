package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

public interface PermissionCheckingService
{
    PermissionCheckResult checkItemPermission(ItemModel paramItemModel, PrincipalModel paramPrincipalModel, String paramString);


    PermissionCheckResult checkItemPermission(ItemModel paramItemModel, String paramString);


    PermissionCheckResult checkTypePermission(ComposedTypeModel paramComposedTypeModel, PrincipalModel paramPrincipalModel, String paramString);


    PermissionCheckResult checkTypePermission(String paramString1, PrincipalModel paramPrincipalModel, String paramString2);


    PermissionCheckResult checkTypePermission(ComposedTypeModel paramComposedTypeModel, String paramString);


    PermissionCheckResult checkTypePermission(String paramString1, String paramString2);


    PermissionCheckResult checkAttributeDescriptorPermission(AttributeDescriptorModel paramAttributeDescriptorModel, PrincipalModel paramPrincipalModel, String paramString);


    PermissionCheckResult checkAttributeDescriptorPermission(AttributeDescriptorModel paramAttributeDescriptorModel, String paramString);


    PermissionCheckResult checkAttributeDescriptorPermission(String paramString1, String paramString2, PrincipalModel paramPrincipalModel, String paramString3);


    PermissionCheckResult checkAttributeDescriptorPermission(String paramString1, String paramString2, String paramString3);


    PermissionCheckResult checkGlobalPermission(PrincipalModel paramPrincipalModel, String paramString);


    PermissionCheckResult checkGlobalPermission(String paramString);
}
