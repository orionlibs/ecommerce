package de.hybris.platform.cmscockpit.session;

import de.hybris.platform.cmscockpit.session.impl.CmsCatalogBrowserModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public interface CmsCatalogBrowserModelFactory
{
    CmsCatalogBrowserModel getInstance(ObjectTemplate paramObjectTemplate);
}
