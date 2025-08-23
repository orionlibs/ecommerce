package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModelListener;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;

public class DefaultTableController extends AbstractTableController
{
    public DefaultTableController(MutableTableModel model, UIListView view)
    {
        super(model, view);
    }


    protected ColumnModelListener createColumnModelListener()
    {
        return (ColumnModelListener)new DefaultColumnModelListener(this.model, this.view);
    }


    protected ListComponentModelListener createListComponentModelListener()
    {
        return (ListComponentModelListener)new DefaultListModelListener(this.view);
    }


    protected ListViewListener createListViewListener()
    {
        return (ListViewListener)new DefaultListViewListener(this.model);
    }


    protected TableModelListener createTableModelListener()
    {
        return (TableModelListener)new DefaultTableModelListener(this.view);
    }
}
