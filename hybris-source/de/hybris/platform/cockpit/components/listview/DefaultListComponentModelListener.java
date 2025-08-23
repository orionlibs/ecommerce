package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.Collection;
import java.util.List;

public class DefaultListComponentModelListener implements ListComponentModelListener
{
    private final UIItemView view;


    public DefaultListComponentModelListener(BrowserModel browser, UIItemView view)
    {
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
    }


    public void onEvent(String eventName, Object value)
    {
    }
}
