package de.hybris.platform.pcmbackoffice;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PcmbackofficeStandalone
{
    private static final Logger LOG = LoggerFactory.getLogger(PcmbackofficeStandalone.class);


    public static void main(String[] args)
    {
        (new PcmbackofficeStandalone()).run();
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
