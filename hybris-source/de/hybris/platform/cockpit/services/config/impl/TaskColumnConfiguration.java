package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.editor.impl.DefaultTextUIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;

public class TaskColumnConfiguration extends DefaultCustomColumnConfiguration
{
    private DefaultColumnDescriptor colDescr = null;
    private TypeService typeService;


    public TaskColumnConfiguration()
    {
        this.name = "Workflow Action";
    }


    public TaskColumnConfiguration(String name)
    {
        this.name = name;
    }


    public UIEditor getCellEditor()
    {
        return (UIEditor)new DefaultTextUIEditor();
    }


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            setCellRenderer((CellRenderer)new TaskCellRenderer((ColumnConfiguration)this));
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


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
