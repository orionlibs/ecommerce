package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class CatalogRestriction extends GeneratedCatalogRestriction
{
    @Deprecated(since = "4.3")
    public String getDescription(SessionContext ctx)
    {
        Collection<Catalog> catalogs = getCatalogs();
        StringBuilder result = new StringBuilder();
        if(!catalogs.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSCatalogRestriction.description.text");
            result.append((localizedString == null) ? "Display for catalogs:" : localizedString);
            for(Catalog cat : catalogs)
            {
                result.append(" ").append(cat.getName(ctx)).append(" (").append(cat.getId()).append(");");
            }
        }
        return result.toString();
    }
}
