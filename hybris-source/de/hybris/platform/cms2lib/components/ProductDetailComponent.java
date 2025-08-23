package de.hybris.platform.cms2lib.components;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;

public class ProductDetailComponent extends GeneratedProductDetailComponent
{
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
