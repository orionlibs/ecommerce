package com.hybris.backoffice.solrsearch.jalo;

import de.hybris.platform.core.Registry;

public class BackofficesolrsearchManager extends GeneratedBackofficesolrsearchManager
{
    public static BackofficesolrsearchManager getInstance()
    {
        return (BackofficesolrsearchManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("backofficesolrsearch");
    }
}
