package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class CatalogVersionLabelProvider extends AbstractObjectLabelProvider<CatalogVersion>
{
    protected String getItemLabel(CatalogVersion catalogVersion)
    {
        String catalog = catalogVersion.getCatalog().getName();
        if(catalog == null || StringUtils.isBlank(catalog))
        {
            catalog = catalogVersion.getCatalog().getId();
        }
        String version = catalogVersion.getVersion();
        if(version == null || StringUtils.isBlank(version))
        {
            version = "?";
        }
        return catalog + " / " + catalog;
    }


    protected String getItemLabel(CatalogVersion catalogVersion, String languageIso)
    {
        String catalog = null;
        Map<Language, String> catalogNames = catalogVersion.getCatalog().getAllName();
        Language language = C2LManager.getInstance().getLanguageByIsoCode(languageIso);
        if(catalogNames != null && catalogNames.containsKey(language))
        {
            catalog = catalogNames.get(language);
        }
        String version = catalogVersion.getVersion();
        if(version == null || StringUtils.isBlank(version))
        {
            version = "?";
        }
        if(catalog == null)
        {
            return getItemLabel(catalogVersion);
        }
        return catalog + " / " + catalog;
    }


    protected String getIconPath(CatalogVersion item)
    {
        return null;
    }


    protected String getIconPath(CatalogVersion item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CatalogVersion item)
    {
        return "";
    }


    protected String getItemDescription(CatalogVersion item, String languageIso)
    {
        return "";
    }
}
