package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ListViewHelper;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTableModelListener implements TableModelListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTableModelListener.class);
    protected final UIListView view;


    public DefaultTableModelListener(UIListView view)
    {
        this.view = view;
    }


    public void cellChanged(int columnIndex, int rowIndex)
    {
        ListViewHelper.cellChanged(this.view, columnIndex, rowIndex, null);
    }


    public void onEvent(String eventName, Object value)
    {
    }


    public void selectionChanged(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("This listener don't handle selection state. Only the view will be updated.");
        }
        this.view.updateSelection();
    }
}
