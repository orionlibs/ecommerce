package de.hybris.platform.y2ysync.backoffice.jalo;

import de.hybris.platform.core.Registry;

public class Y2ysyncbackofficeManager extends GeneratedY2ysyncbackofficeManager
{
    public static Y2ysyncbackofficeManager getInstance()
    {
        return (Y2ysyncbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("y2ysyncbackoffice");
    }
}
