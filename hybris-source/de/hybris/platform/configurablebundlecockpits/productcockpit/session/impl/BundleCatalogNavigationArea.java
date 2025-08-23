package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.productcockpit.session.impl.CatalogNavigationArea;

public class BundleCatalogNavigationArea extends CatalogNavigationArea
{
    public CatalogVersionModel getSelectedCatalogVersion()
    {
        CatalogVersionModel ret;
        UIBrowserArea browserArea = getPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            ret = ((BundleNavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
        }
        else
        {
            ret = null;
        }
        return ret;
    }
}
