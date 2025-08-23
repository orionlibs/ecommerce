package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;

public class CMSLinkComponent extends GeneratedCMSLinkComponent
{
    @Deprecated(since = "4.3")
    public String getCategoryCode(SessionContext ctx)
    {
        String ret = null;
        SessionContext localSessionContext = null;
        try
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            if(getCategory() != null)
            {
                ret = getCategory().getCode();
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


    @Deprecated(since = "4.3")
    public String getContentPageLabelOrId(SessionContext ctx)
    {
        String ret = null;
        SessionContext localSessionContext = null;
        try
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            if(getContentPage() != null)
            {
                ret = getContentPage().getLabelOrId();
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


    @Deprecated(since = "4.3")
    public String getProductCode(SessionContext ctx)
    {
        String ret = null;
        SessionContext localSessionContext = null;
        try
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            if(getProduct() != null)
            {
                ret = getProduct().getCode();
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
