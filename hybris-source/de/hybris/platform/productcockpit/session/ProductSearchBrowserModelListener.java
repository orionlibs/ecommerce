package de.hybris.platform.productcockpit.session;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModelListener;
import java.util.Collection;

public interface ProductSearchBrowserModelListener extends SearchBrowserModelListener
{
    void blacklistItems(BrowserModel paramBrowserModel, Collection<Integer> paramCollection);
}
