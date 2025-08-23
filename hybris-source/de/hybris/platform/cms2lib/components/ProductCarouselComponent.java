package de.hybris.platform.cms2lib.components;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductCarouselComponent extends GeneratedProductCarouselComponent
{
    public List<String> getCategoryCodes(SessionContext ctx)
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        SessionContext localSessionContext = null;
        try
        {
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            List<PK> catPKs = getCategoryPKs(localSessionContext);
            if(catPKs != null && !catPKs.isEmpty())
            {
                ArrayList<String> codes = new ArrayList<>(catPKs.size());
                for(Category c : jaloSession.getItems(localSessionContext, catPKs, true, false))
                {
                    codes.add(c.getCode(localSessionContext));
                }
                return codes;
            }
            return (List)Collections.emptyList();
        }
        finally
        {
            if(localSessionContext != null)
            {
                jaloSession.removeLocalSessionContext();
            }
        }
    }


    protected List<PK> getCategoryPKs(SessionContext ctx)
    {
        return FlexibleSearch.getInstance().search(ctx, "SELECT {target} FROM {" + GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT + " } WHERE {source} = ?me ",
                                        Collections.singletonMap("me", this), PK.class)
                        .getResult();
    }


    public List<String> getProductCodes(SessionContext ctx)
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        SessionContext localSessionContext = null;
        try
        {
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            List<PK> prodPKs = getProductPKs(localSessionContext);
            if(prodPKs != null && !prodPKs.isEmpty())
            {
                ArrayList<String> codes = new ArrayList<>(prodPKs.size());
                for(Product p : jaloSession.getItems(localSessionContext, prodPKs, true, false))
                {
                    codes.add(p.getCode(localSessionContext));
                }
                return codes;
            }
            return (List)Collections.emptyList();
        }
        finally
        {
            if(localSessionContext != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    protected List<PK> getProductPKs(SessionContext ctx)
    {
        return FlexibleSearch.getInstance().search(ctx, "SELECT {target} FROM {" + GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT + " } WHERE {source} = ?me ",
                                        Collections.singletonMap("me", this), PK.class)
                        .getResult();
    }
}
