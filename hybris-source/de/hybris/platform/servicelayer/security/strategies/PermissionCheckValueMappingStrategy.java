package de.hybris.platform.servicelayer.security.strategies;

import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;

public interface PermissionCheckValueMappingStrategy
{
    PermissionCheckResult getPermissionCheckResult(PermissionCheckValue paramPermissionCheckValue);
}
