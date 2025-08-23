package de.hybris.platform.productcockpit.session;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.impl.BrowserAreaListener;
import java.util.Collection;

public interface ProductBrowserAreaListener extends BrowserAreaListener
{
    void blacklistItems(BrowserModel paramBrowserModel, Collection<Integer> paramCollection);
}
