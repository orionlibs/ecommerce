package de.hybris.platform.warehousing;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarehousingStandalone
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehousingStandalone.class);


    public static void main(String[] args)
    {
        (new WarehousingStandalone()).run();
    }


    public void run()
    {
        Registry.activateStandaloneMode();
        Registry.activateMasterTenant();
        JaloSession jaloSession = JaloSession.getCurrentSession();
        LOGGER.info("Session ID: {}", jaloSession.getSessionID());
        LOGGER.info("User: {}", jaloSession.getUser());
        Utilities.printAppInfo();
        RedeployUtilities.shutdown();
    }
}
