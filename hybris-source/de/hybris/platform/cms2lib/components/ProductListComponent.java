package de.hybris.platform.cms2lib.components;

import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.List;

public class ProductListComponent extends GeneratedProductListComponent
{
    private static final String query = "SELECT {product:code}  FROM {Product as product} WHERE {product:pk}  IN ( {{  SELECT {relation:target}  FROM {" + GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT + " as relation } WHERE {relation:source} = ?me  }} )";


    public List<String> getProductCodes(SessionContext ctx)
    {
        SessionContext localSessionContext = null;
        try
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            return jaloSession.getFlexibleSearch().search(localSessionContext, query, Collections.singletonMap("me", this), String.class)
                            .getResult();
        }
        finally
        {
            if(localSessionContext != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


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
}
