package de.hybris.platform.processing;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;

public class ProcessingStandalone
{
    public static void main(String[] args)
    {
        (new ProcessingStandalone()).run();
    }


    public void run()
    {
        Registry.activateStandaloneMode();
        Registry.activateMasterTenant();
        JaloSession jaloSession = JaloSession.getCurrentSession();
        System.out.println("Session ID: " + jaloSession.getSessionID());
        System.out.println("User: " + jaloSession.getUser());
        Utilities.printAppInfo();
        RedeployUtilities.shutdown();
    }
}
