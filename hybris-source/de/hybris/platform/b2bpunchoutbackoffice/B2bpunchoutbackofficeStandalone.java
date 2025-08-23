package de.hybris.platform.b2bpunchoutbackoffice;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class B2bpunchoutbackofficeStandalone
{
    private static final Logger LOG = LoggerFactory.getLogger(B2bpunchoutbackofficeStandalone.class);


    public static void main(String[] args)
    {
        (new B2bpunchoutbackofficeStandalone()).run();
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
