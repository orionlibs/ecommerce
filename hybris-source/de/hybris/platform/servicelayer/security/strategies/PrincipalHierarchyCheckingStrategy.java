package de.hybris.platform.servicelayer.security.strategies;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionChecker;

public interface PrincipalHierarchyCheckingStrategy
{
    PermissionCheckValue checkPermissionsForPrincipalHierarchy(PermissionChecker paramPermissionChecker, PrincipalModel paramPrincipalModel, String paramString);
}
