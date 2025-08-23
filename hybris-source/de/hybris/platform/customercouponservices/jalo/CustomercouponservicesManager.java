package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomercouponservicesManager extends GeneratedCustomercouponservicesManager
{
    private static final Logger log = Logger.getLogger(CustomercouponservicesManager.class.getName());


    public static final CustomercouponservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (CustomercouponservicesManager)em.getExtension("customercouponservices");
    }
}
