package de.hybris.platform.marketplacebackoffice.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class MarketplacebackofficeManager extends GeneratedMarketplacebackofficeManager
{
    private static final Logger log = Logger.getLogger(MarketplacebackofficeManager.class.getName());


    public static final MarketplacebackofficeManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (MarketplacebackofficeManager)em.getExtension("marketplacebackoffice");
    }
}
