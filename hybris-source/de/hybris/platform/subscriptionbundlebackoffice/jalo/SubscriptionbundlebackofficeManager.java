package de.hybris.platform.subscriptionbundlebackoffice.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class SubscriptionbundlebackofficeManager extends GeneratedSubscriptionbundlebackofficeManager
{
    private static final Logger LOG = Logger.getLogger(SubscriptionbundlebackofficeManager.class.getName());


    public static final SubscriptionbundlebackofficeManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (SubscriptionbundlebackofficeManager)em.getExtension("subscriptionbundlebackoffice");
    }
}
