package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProductRestriction extends GeneratedProductRestriction
{
    @Deprecated(since = "4.3")
    public String getDescription(SessionContext ctx)
    {
        Collection<Product> products = getProducts();
        StringBuilder result = new StringBuilder();
        if(!products.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSProductRestriction.description.text");
            result.append((localizedString == null) ? "Display for products:" : localizedString);
            for(Product product : products)
            {
                result.append(" ").append(product.getName(ctx)).append(" (").append(product.getCode()).append(");");
            }
        }
        return result.toString();
    }


    @Deprecated(since = "4.3")
    public List<String> getProductCodes(SessionContext ctx)
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        SessionContext localSessionContext = null;
        try
        {
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            List<PK> prodPKs = getProductPKs(localSessionContext);
            if(!prodPKs.isEmpty())
            {
                ArrayList<String> codes = new ArrayList<>(prodPKs.size());
                for(Product p : jaloSession.getItems(localSessionContext, prodPKs, true, false))
                {
                    codes.add(p.getCode(localSessionContext));
                }
                return codes;
            }
            return Collections.EMPTY_LIST;
        }
        finally
        {
            if(localSessionContext != null)
            {
                jaloSession.removeLocalSessionContext();
            }
        }
    }


    protected List<PK> getProductPKs(SessionContext ctx)
    {
        return FlexibleSearch.getInstance().search(ctx, "SELECT {target} FROM {" + GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION + " } WHERE {source} = ?me ",
                                        Collections.singletonMap("me", this), PK.class)
                        .getResult();
    }
}
