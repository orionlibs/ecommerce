package de.hybris.platform.cockpit.model.general.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.Collection;
import java.util.List;

public class DefaultBrowserListModelListener implements ListComponentModelListener
{
    private final UIListView view;
    private final BrowserModel browser;


    public DefaultBrowserListModelListener(BrowserModel browser, UIListView view)
    {
        this.browser = browser;
        this.view = view;
    }


    public void activationChanged(Collection<TypedObject> items)
    {
        this.view.updateActivation();
    }


    public void changed()
    {
    }


    public void itemMoved(int fromIndex, int toIndex)
    {
    }


    public void itemsChanged()
    {
        if(this.browser instanceof AdvancedBrowserModel && ("LIST"
                        .equals(((AdvancedBrowserModel)this.browser).getViewMode()) || "MULTI_TYPE_LIST"
                        .equals(((AdvancedBrowserModel)this.browser).getViewMode())))
        {
            this.view.updateItems();
        }
    }


    public void itemsRemoved(Collection<? extends Object> items)
    {
    }


    public void selectionChanged(List<Integer> indexes)
    {
    }


    public void onEvent(String eventName, Object value)
    {
    }
}
