package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class TimedaccesspromotionengineservicesManager extends GeneratedTimedaccesspromotionengineservicesManager
{
    private static final Logger log = Logger.getLogger(TimedaccesspromotionengineservicesManager.class.getName());


    public static final TimedaccesspromotionengineservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (TimedaccesspromotionengineservicesManager)em.getExtension("timedaccesspromotionengineservices");
    }
}
