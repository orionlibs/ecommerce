package de.hybris.platform.cms2.permissions;

import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;

public interface PermissionCachedCRUDService extends PermissionCRUDService
{
    default void initCache()
    {
    }
}
