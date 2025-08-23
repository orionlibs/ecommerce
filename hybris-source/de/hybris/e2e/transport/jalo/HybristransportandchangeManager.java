package de.hybris.e2e.transport.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class HybristransportandchangeManager extends GeneratedHybristransportandchangeManager
{
    private static Logger log = Logger.getLogger(HybristransportandchangeManager.class.getName());


    public static final HybristransportandchangeManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (HybristransportandchangeManager)em.getExtension("hybristransportandchange");
    }
}
