package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cmscockpit.session.CmsCatalogBrowserModelFactory;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public class DefaultCmsCatalogBrowserModelFactory implements CmsCatalogBrowserModelFactory
{
    public CmsCatalogBrowserModel getInstance(ObjectTemplate rootType)
    {
        return new CmsCatalogBrowserModel(rootType);
    }
}
