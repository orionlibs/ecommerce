package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.services.config.impl.DefaultCustomColumnConfiguration;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleColumnConfiguration extends DefaultCustomColumnConfiguration
{
    private ListViewAction iconAction = null;
    private List<ListViewAction> actions = new ArrayList<>();
    private CellRenderer actionCellRenderer = null;


    public void setIconAction(ListViewAction iconAction)
    {
        this.iconAction = iconAction;
    }


    public ListViewAction getIconAction()
    {
        return this.iconAction;
    }


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            setCellRenderer((CellRenderer)new SingleColumnCellRenderer(this));
        }
        return super.getCellRenderer();
    }


    public void setActions(List<ListViewAction> actions)
    {
        this.actions = actions;
    }


    public List<ListViewAction> getActions()
    {
        return this.actions;
    }


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        DefaultColumnDescriptor defaultColumnDescriptor = new DefaultColumnDescriptor(getName());
        defaultColumnDescriptor.setVisible(true);
        defaultColumnDescriptor.setDynamic(false);
        defaultColumnDescriptor.setEditable(false);
        defaultColumnDescriptor.setSelectable(true);
        return defaultColumnDescriptor;
    }
}
