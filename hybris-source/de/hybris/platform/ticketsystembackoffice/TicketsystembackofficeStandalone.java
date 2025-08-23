package de.hybris.platform.ticketsystembackoffice;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketsystembackofficeStandalone
{
    private static final Logger LOG = LoggerFactory.getLogger(TicketsystembackofficeStandalone.class);


    public static void main(String[] args)
    {
        (new TicketsystembackofficeStandalone()).run();
    }


    public void run()
    {
        Registry.activateStandaloneMode();
        Registry.activateMasterTenant();
        JaloSession jaloSession = JaloSession.getCurrentSession();
        LOG.info("Session ID: {}", jaloSession.getSessionID());
        LOG.info("User: {}", jaloSession.getUser());
        Utilities.printAppInfo();
        RedeployUtilities.shutdown();
    }
}
