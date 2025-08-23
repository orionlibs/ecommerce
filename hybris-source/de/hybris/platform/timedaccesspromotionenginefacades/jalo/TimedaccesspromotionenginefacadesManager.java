package de.hybris.platform.timedaccesspromotionenginefacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class TimedaccesspromotionenginefacadesManager extends GeneratedTimedaccesspromotionenginefacadesManager
{
    private static final Logger log = Logger.getLogger(TimedaccesspromotionenginefacadesManager.class.getName());


    public static final TimedaccesspromotionenginefacadesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (TimedaccesspromotionenginefacadesManager)em.getExtension("timedaccesspromotionenginefacades");
    }
}
