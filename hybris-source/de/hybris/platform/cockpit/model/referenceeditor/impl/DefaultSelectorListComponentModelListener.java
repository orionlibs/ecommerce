package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import java.util.Collection;
import java.util.List;

public class DefaultSelectorListComponentModelListener implements ListComponentModelListener
{
    private final UIListView view;
    private final AbstractReferenceSelectorModel referenceSelectorModel;


    public DefaultSelectorListComponentModelListener(AbstractReferenceSelectorModel referenceSelectorModel, UIListView view)
    {
        this.referenceSelectorModel = referenceSelectorModel;
        this.view = view;
    }


    public void activationChanged(Collection<TypedObject> items)
    {
        this.referenceSelectorModel.setActiveItems(items);
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
