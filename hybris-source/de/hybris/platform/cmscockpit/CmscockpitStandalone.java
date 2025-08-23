package de.hybris.platform.cmscockpit;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.Utilities;

public class CmscockpitStandalone
{
    public static void main(String[] args)
    {
        (new CmscockpitStandalone()).run();
    }


    public void run()
    {
        Registry.setPreferredClusterID(15);
        JaloSession jaloSession = JaloSession.getCurrentSession();
        System.out.println("Session ID: " + jaloSession.getSessionID());
        System.out.println("User: " + jaloSession.getUser());
        Utilities.printAppInfo();
    }
}
