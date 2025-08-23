package de.hybris.datasupplier;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;

public class HybrisDataSupplierStandalone
{
    public static void main(String[] args)
    {
        (new HybrisDataSupplierStandalone()).run();
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
