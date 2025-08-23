package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultBrowserSectionTableController;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;

public class CmsBrowserSectionTableController extends DefaultBrowserSectionTableController
{
    public CmsBrowserSectionTableController(ListBrowserSectionModel sectionModel, MutableTableModel model, UIListView view)
    {
        super(sectionModel, model, view);
    }


    protected TableModelListener createTableModelListener()
    {
        return (TableModelListener)new CmsBrowserSectionTableModelListener(this.sectionModel, this.view);
    }


    protected ListViewListener createListViewListener()
    {
        return (ListViewListener)new CmsBrowserSectionListViewlListener(this.sectionModel, this.model);
    }
}
