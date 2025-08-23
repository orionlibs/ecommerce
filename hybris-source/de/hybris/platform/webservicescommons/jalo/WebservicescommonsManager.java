package de.hybris.platform.webservicescommons.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class WebservicescommonsManager extends GeneratedWebservicescommonsManager
{
    public static final WebservicescommonsManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (WebservicescommonsManager)em.getExtension("webservicescommons");
    }
}
