package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class CatalogVersionModelLabelProvider extends AbstractModelLabelProvider<CatalogVersionModel>
{
    protected String getItemLabel(CatalogVersionModel catalogVersion)
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


    protected String getItemLabel(CatalogVersionModel catalogVersion, String languageIso)
    {
        String catalog = null;
        Locale locale = new Locale(languageIso);
        catalog = catalogVersion.getCatalog().getName(locale);
        String version = catalogVersion.getVersion();
        if(version == null || StringUtils.isBlank(version))
        {
            version = "?";
        }
        if(StringUtils.isEmpty(catalog))
        {
            return getItemLabel(catalogVersion);
        }
        return catalog + " / " + catalog;
    }


    protected String getIconPath(CatalogVersionModel item)
    {
        return null;
    }


    protected String getIconPath(CatalogVersionModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CatalogVersionModel item)
    {
        return "";
    }


    protected String getItemDescription(CatalogVersionModel item, String languageIso)
    {
        return "";
    }
}
