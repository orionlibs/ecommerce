package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ListViewHelper;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBrowserSectionTableModelListener implements TableModelListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBrowserSectionTableModelListener.class);
    protected final ListBrowserSectionModel sectionModel;
    protected final UIListView view;


    public DefaultBrowserSectionTableModelListener(ListBrowserSectionModel sectionModel, UIListView view)
    {
        this.sectionModel = sectionModel;
        this.view = view;
    }


    public void cellChanged(int columnIndex, int rowIndex)
    {
        ListViewHelper.cellChanged(this.view, columnIndex, rowIndex, this.sectionModel.getSectionBrowserModel().getArea());
    }


    public void onEvent(String eventName, Object value)
    {
        if("updateitems".equalsIgnoreCase(eventName))
        {
            this.sectionModel.update();
        }
    }


    public void selectionChanged(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        this.sectionModel.setSelectedIndexes(rowIndexes);
    }
}
