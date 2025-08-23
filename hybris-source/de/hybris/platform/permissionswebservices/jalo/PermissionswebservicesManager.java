package de.hybris.platform.permissionswebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PermissionswebservicesManager extends GeneratedPermissionswebservicesManager
{
    public static final PermissionswebservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PermissionswebservicesManager)em.getExtension("permissionswebservices");
    }
}
