package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import java.util.List;

public class DefaultSelectorTableModelListener implements TableModelListener
{
    protected final UIListView view;
    protected final AbstractReferenceSelectorModel referenceSelectorModel;


    public DefaultSelectorTableModelListener(AbstractReferenceSelectorModel referenceSelectorModel, UIListView view)
    {
        this.view = view;
        this.referenceSelectorModel = referenceSelectorModel;
    }


    public void cellChanged(int columnIndex, int rowIndex)
    {
        this.view.updateCell(columnIndex, rowIndex);
    }


    public void selectionChanged(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        this.view.updateSelection();
    }


    public void onEvent(String eventName, Object value)
    {
        if(eventName == null || eventName.length() < 1)
        {
            throw new IllegalArgumentException("An event name must be specified");
        }
    }
}
