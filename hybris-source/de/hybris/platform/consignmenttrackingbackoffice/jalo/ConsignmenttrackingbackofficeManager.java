package de.hybris.platform.consignmenttrackingbackoffice.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ConsignmenttrackingbackofficeManager extends GeneratedConsignmenttrackingbackofficeManager
{
    private static final Logger log = Logger.getLogger(ConsignmenttrackingbackofficeManager.class.getName());


    public static final ConsignmenttrackingbackofficeManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (ConsignmenttrackingbackofficeManager)em.getExtension("consignmenttrackingbackoffice");
    }
}
