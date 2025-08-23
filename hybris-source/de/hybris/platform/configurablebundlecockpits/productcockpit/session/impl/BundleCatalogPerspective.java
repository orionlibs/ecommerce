package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.productcockpit.session.impl.CatalogPerspective;
import org.apache.log4j.Logger;

public class BundleCatalogPerspective extends CatalogPerspective
{
    private static final Logger LOG = Logger.getLogger(BundleCatalogPerspective.class);
    private NavigationAreaListener navigationAreaListenernavListener;


    protected NavigationAreaListener getNavigationAreaListener()
    {
        if(this.navigationAreaListenernavListener == null)
        {
            this.navigationAreaListenernavListener = (NavigationAreaListener)new DefaultBundleNavigationAreaListener(this, (BaseUICockpitPerspective)this);
        }
        return this.navigationAreaListenernavListener;
    }
}
