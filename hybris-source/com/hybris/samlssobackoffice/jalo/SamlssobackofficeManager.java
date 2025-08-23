package com.hybris.samlssobackoffice.jalo;

import de.hybris.platform.core.Registry;

public class SamlssobackofficeManager extends GeneratedSamlssobackofficeManager
{
    public static SamlssobackofficeManager getInstance()
    {
        return (SamlssobackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("samlssobackoffice");
    }
}
