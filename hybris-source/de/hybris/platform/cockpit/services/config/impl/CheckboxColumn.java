package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;

public class CheckboxColumn extends DefaultCustomColumnConfiguration
{
    private DefaultColumnDescriptor colDescr = null;


    public CheckboxColumn()
    {
    }


    public CheckboxColumn(String name)
    {
        this.name = name;
    }


    public UIEditor getCellEditor()
    {
        return null;
    }


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            setCellRenderer((CellRenderer)new CheckboxCellRenderer(this));
        }
        return super.getCellRenderer();
    }


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        if(this.colDescr == null)
        {
            this.colDescr = new DefaultColumnDescriptor(getName());
            this.colDescr.setEditable(isEditable());
            this.colDescr.setSelectable(isSelectable());
            this.colDescr.setSortable(isSortable());
            this.colDescr.setVisible(isVisible());
        }
        return this.colDescr;
    }


    public ValueHandler getValueHandler()
    {
        return null;
    }


    public boolean isSelectable()
    {
        return true;
    }
}
