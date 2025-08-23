package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.general.impl.DefaultBrowserSectionListModelListener;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;

public class DefaultBrowserSectionTableController extends AbstractTableController
{
    protected final ListBrowserSectionModel sectionModel;


    public DefaultBrowserSectionTableController(ListBrowserSectionModel sectionModel, MutableTableModel model, UIListView view)
    {
        super(model, view);
        this.sectionModel = sectionModel;
    }


    protected ColumnModelListener createColumnModelListener()
    {
        return (ColumnModelListener)new DefaultColumnModelListener(this.model, this.view);
    }


    protected ListComponentModelListener createListComponentModelListener()
    {
        return (ListComponentModelListener)new DefaultBrowserSectionListModelListener(this.sectionModel, this.view);
    }


    protected ListViewListener createListViewListener()
    {
        return (ListViewListener)new DefaultBrowserSectionListViewListener(this.sectionModel, this.model);
    }


    protected TableModelListener createTableModelListener()
    {
        return (TableModelListener)new DefaultBrowserSectionTableModelListener(this.sectionModel, this.view);
    }


    protected ListBrowserSectionModel getSectionModel()
    {
        return this.sectionModel;
    }
}
