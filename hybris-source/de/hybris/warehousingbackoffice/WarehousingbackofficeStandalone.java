package de.hybris.warehousingbackoffice;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarehousingbackofficeStandalone
{
    private static Logger LOGGER = LoggerFactory.getLogger(WarehousingbackofficeStandalone.class);


    public static void main(String[] args)
    {
        (new WarehousingbackofficeStandalone()).run();
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
