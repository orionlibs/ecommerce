package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.core.model.security.PrincipalModel;

public interface PermissionChecker
{
    PermissionCheckValue checkPermission(PrincipalModel paramPrincipalModel, String paramString);
}
