package de.hybris.platform.cms2.model;

import com.google.common.base.Strings;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.restrictions.CMSCatalogRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class CatalogRestrictionDescription implements DynamicAttributeHandler<String, CMSCatalogRestrictionModel>
{
    public String get(CMSCatalogRestrictionModel model)
    {
        Collection<CatalogModel> catalogs = model.getCatalogs();
        StringBuilder result = new StringBuilder();
        if(catalogs != null && !catalogs.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSCatalogRestriction.description.text");
            result.append((localizedString == null) ? "Display for catalogs:" : localizedString);
            for(CatalogModel catalog : catalogs)
            {
                if(!Strings.isNullOrEmpty(catalog.getName()))
                {
                    result.append(" ").append(catalog.getName());
                }
                result.append(" (").append(catalog.getId()).append(");");
            }
        }
        return result.toString();
    }


    public void set(CMSCatalogRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
