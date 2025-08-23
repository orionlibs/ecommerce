package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CategoryRestriction extends GeneratedCategoryRestriction
{
    @Deprecated(since = "4.3")
    public String getDescription(SessionContext ctx)
    {
        Collection<Category> categories = getCategories();
        StringBuilder result = new StringBuilder();
        if(!categories.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSCategoryRestriction.description.text");
            result.append((localizedString == null) ? "Display for categories:" : localizedString);
            for(Category cat : categories)
            {
                result.append(" ").append(cat.getName(ctx)).append(" (").append(cat.getCode()).append(");");
            }
        }
        return result.toString();
    }


    @Deprecated(since = "4.3")
    public List<String> getCategoryCodes(SessionContext ctx)
    {
        SessionContext localSessionContext = null;
        JaloSession jaloSession = JaloSession.getCurrentSession();
        try
        {
            localSessionContext = jaloSession.createLocalSessionContext(ctx);
            localSessionContext.setAttribute("disableRestrictions", Boolean.TRUE);
            List<PK> catPKs = getCategoryPKs(localSessionContext);
            if(!catPKs.isEmpty())
            {
                ArrayList<String> codes = new ArrayList<>(catPKs.size());
                for(Category c : jaloSession.getItems(localSessionContext, catPKs, true, false))
                {
                    codes.add(c.getCode(localSessionContext));
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


    protected List<PK> getCategoryPKs(SessionContext ctx)
    {
        return FlexibleSearch.getInstance().search(ctx, "SELECT {target} FROM {" + GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION + " } WHERE {source} = ?me ",
                                        Collections.singletonMap("me", this), PK.class)
                        .getResult();
    }
}
