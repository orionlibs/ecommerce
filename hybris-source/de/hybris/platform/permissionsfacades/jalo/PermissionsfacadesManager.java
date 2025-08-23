package de.hybris.platform.permissionsfacades.jalo;

import de.hybris.platform.core.Registry;

public class PermissionsfacadesManager extends GeneratedPermissionsfacadesManager
{
    public static PermissionsfacadesManager getInstance()
    {
        return (PermissionsfacadesManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("permissionsfacades");
    }
}
