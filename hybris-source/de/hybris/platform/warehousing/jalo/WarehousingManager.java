package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class WarehousingManager extends GeneratedWarehousingManager
{
    public static final WarehousingManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (WarehousingManager)em.getExtension("warehousing");
    }
}
