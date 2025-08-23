package de.hybris.platform.cockpit.model.general.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import java.util.Collection;
import java.util.List;

public class DefaultBrowserSectionListModelListener implements ListComponentModelListener
{
    protected final ListBrowserSectionModel sectionModel;
    protected final UIListView view;


    public DefaultBrowserSectionListModelListener(ListBrowserSectionModel sectionModel, UIListView view)
    {
        this.sectionModel = sectionModel;
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
        this.view.updateItems();
    }


    public void itemsRemoved(Collection<? extends Object> items)
    {
    }


    public void selectionChanged(List<Integer> indexes)
    {
        this.view.updateSelection();
    }


    public void onEvent(String eventName, Object value)
    {
    }
}
