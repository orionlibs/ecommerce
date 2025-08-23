package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.AdvancedBrowserModelListener;
import de.hybris.platform.cockpit.session.AdvancedContextBrowserModelListener;
import de.hybris.platform.cockpit.session.BrowserModelListener;

public abstract class AbstractAdvancedContextBrowserModel extends AbstractAdvancedBrowserModel
{
    protected void fireContextItemsChanged(boolean cleanContextHeader)
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            if(listener instanceof AdvancedContextBrowserModelListener)
            {
                ((AdvancedContextBrowserModelListener)listener).contextItemsChanged((AdvancedBrowserModel)this, cleanContextHeader);
                continue;
            }
            if(listener instanceof AdvancedBrowserModelListener)
            {
                ((AdvancedBrowserModelListener)listener).contextItemsChanged((AdvancedBrowserModel)this);
            }
        }
    }
}
