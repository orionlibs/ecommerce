package de.hybris.platform.cmscockpit.components.listsectionbrowser.impl;

import de.hybris.platform.cmscockpit.components.listview.impl.CmsBrowserSectionTableController;
import de.hybris.platform.cmscockpit.session.impl.CmsListBrowserSectionModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.AbstractTableController;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.ListSectionModel;

public class CmsStructListBrowserSectionComponent extends CmsListBrowserSectionComponent
{
    public CmsStructListBrowserSectionComponent(CmsListBrowserSectionModel sectionModel)
    {
        super((ListBrowserSectionModel)sectionModel);
    }


    protected AbstractTableController createTableController(ListSectionModel listSectionModel, MutableTableModel mutableTableModel, UIListView listView)
    {
        if(listSectionModel instanceof ListBrowserSectionModel)
        {
            return (AbstractTableController)new CmsBrowserSectionTableController((ListBrowserSectionModel)listSectionModel, mutableTableModel, listView);
        }
        throw new IllegalArgumentException("List section model not a list browser section model.");
    }
}
