package de.hybris.platform.previewwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PreviewwebservicesManager extends GeneratedPreviewwebservicesManager
{
    public static final PreviewwebservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PreviewwebservicesManager)em.getExtension("previewwebservices");
    }
}
