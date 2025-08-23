package de.hybris.platform.cockpit.components.menu.impl;

import de.hybris.platform.cockpit.components.menu.AbstractAssignedListRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.UIListView;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class ColumnsAssignedListViewRenderer extends AbstractAssignedListRenderer<ColumnDescriptor>
{
    private static final Logger LOG = LoggerFactory.getLogger(ColumnsAssignedListViewRenderer.class);
    private final UIListView listView;
    private final EventListener hideEventListener;


    public ColumnsAssignedListViewRenderer(List<ColumnDescriptor> assignedValuesList, UIListView listView, EventListener hideEventListener)
    {
        super(assignedValuesList);
        this.listView = listView;
        this.hideEventListener = hideEventListener;
    }


    public EventListener getRemoveImageListener(Listitem item, Object data)
    {
        return (EventListener)new Object(this, data);
    }


    public ListitemRenderer createAvailableCollectionMenuItemListRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }
}
