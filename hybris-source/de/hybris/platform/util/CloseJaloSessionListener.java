package de.hybris.platform.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CloseJaloSessionListener implements HttpSessionListener
{
    public void sessionCreated(HttpSessionEvent event)
    {
    }


    public void sessionDestroyed(HttpSessionEvent event)
    {
        boolean improvedSessionHandling = Registry.getMasterTenant().getConfig().getBoolean("hybris.improvedsessionhandling", false);
        if(improvedSessionHandling)
        {
            JaloSession session = (JaloSession)event.getSession().getAttribute("jalosession");
            if(session != null)
            {
                session.close();
            }
        }
    }
}
