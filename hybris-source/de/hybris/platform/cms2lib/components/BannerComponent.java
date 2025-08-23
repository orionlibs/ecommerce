package de.hybris.platform.cms2lib.components;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;

public class BannerComponent extends GeneratedBannerComponent
{
    public String getPageLabelOrId(SessionContext ctx)
    {
        String ret = null;
        SessionContext localSessionContext = null;
        try
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            if(getPage() != null)
            {
                ret = getPage().getLabelOrId();
            }
            return ret;
        }
        finally
        {
            if(localSessionContext != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }
}
