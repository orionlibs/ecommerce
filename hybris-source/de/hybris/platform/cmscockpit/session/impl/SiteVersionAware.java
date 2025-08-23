package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

public interface SiteVersionAware
{
    void setActiveSite(CMSSiteModel paramCMSSiteModel);


    void setActiveCatalogVersion(CatalogVersionModel paramCatalogVersionModel);
}
