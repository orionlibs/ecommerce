package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ActionCellRenderer;
import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.services.config.impl.DefaultCustomColumnConfiguration;
import java.util.ArrayList;
import java.util.List;

public class DefaultActionColumnConfiguration extends DefaultCustomColumnConfiguration implements ActionColumnConfiguration
{
    private List<? extends ListViewAction> listViewActions = null;


    public DefaultActionColumnConfiguration(String name)
    {
        this(name, null);
    }


    public DefaultActionColumnConfiguration(String name, List<? extends ListViewAction> listViewActions)
    {
        this.name = name;
        if(listViewActions == null)
        {
            this.listViewActions = new ArrayList<>();
        }
        else
        {
            this.listViewActions = listViewActions;
        }
        loadValues();
        setEditable(false);
    }


    private void loadValues()
    {
        this.visible = true;
        this.editable = false;
        this.selectable = false;
        this.sortable = false;
    }


    public List<ListViewAction> getActions()
    {
        return (List)this.listViewActions;
    }


    public void setActions(List<? extends ListViewAction> listViewActions)
    {
        if(!this.listViewActions.equals(listViewActions))
        {
            this.listViewActions = listViewActions;
            setCellRenderer(null);
            if(!this.listViewActions.isEmpty())
            {
                DefaultActionCellRenderer defaultActionCellRenderer = new DefaultActionCellRenderer();
                List<ListViewAction> actions = new ArrayList<>(listViewActions);
                defaultActionCellRenderer.setActions(actions);
                setCellRenderer((CellRenderer)defaultActionCellRenderer);
            }
        }
    }


    public UIEditor getCellEditor()
    {
        return null;
    }


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            DefaultActionCellRenderer defaultActionCellRenderer = new DefaultActionCellRenderer();
            setCellRenderer((CellRenderer)defaultActionCellRenderer);
        }
        if(super.getCellRenderer() instanceof ActionCellRenderer)
        {
            List<ListViewAction> actions = new ArrayList<>(this.listViewActions);
            ((ActionCellRenderer)super.getCellRenderer()).setActions(actions);
        }
        return super.getCellRenderer();
    }


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        DefaultColumnDescriptor colDescr = new DefaultColumnDescriptor(getName());
        colDescr.setEditable(isEditable());
        colDescr.setSelectable(isSelectable());
        colDescr.setSortable(isSortable());
        colDescr.setVisible(isVisible());
        return colDescr;
    }


    public ValueHandler getValueHandler()
    {
        return null;
    }
}
