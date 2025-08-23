package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.general.impl.DefaultBrowserListModelListener;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.session.BrowserModel;

public class DefaultBrowserTableController extends AbstractTableController
{
    protected final BrowserModel browser;


    public DefaultBrowserTableController(BrowserModel browser, MutableTableModel model, UIListView view)
    {
        super(model, view);
        this.browser = browser;
    }


    protected TableModelListener createTableModelListener()
    {
        return (TableModelListener)new DefaultBrowserTableModelListener(this.browser, this.view);
    }


    protected ListViewListener createListViewListener()
    {
        return (ListViewListener)new DefaultBrowserListViewListener(this.browser, this.model);
    }


    protected ListComponentModelListener createListComponentModelListener()
    {
        return (ListComponentModelListener)new DefaultBrowserListModelListener(this.browser, this.view);
    }


    protected ColumnModelListener createColumnModelListener()
    {
        return (ColumnModelListener)new DefaultColumnModelListener(this.model, this.view);
    }
}
