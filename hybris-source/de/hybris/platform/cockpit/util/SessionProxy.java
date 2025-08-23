package de.hybris.platform.cockpit.util;

import de.hybris.platform.cockpit.session.UISession;
import org.springframework.beans.factory.annotation.Required;

public class SessionProxy
{
    private UISession session;


    public UISession getSession()
    {
        return this.session;
    }


    @Required
    public void setSession(UISession session)
    {
        this.session = session;
    }
}
