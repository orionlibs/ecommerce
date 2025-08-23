package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.impl.ImageCellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.WorkflowImgValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.WorkflowStatusImageCellRenderer;

public class WorkflowImgColumnConfiguration extends DefaultCustomColumnConfiguration
{
    private DefaultColumnDescriptor columnDescriptor;
    private ValueHandler valueHandler;
    private WorkflowStatusImageCellRenderer workflowStatusRenderer;


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        if(this.columnDescriptor == null)
        {
            this.columnDescriptor = new DefaultColumnDescriptor(getName());
            this.columnDescriptor.setVisible(isVisible());
            this.columnDescriptor.setSelectable(isSelectable());
            this.columnDescriptor.setSortable(isSortable());
            this.columnDescriptor.setEditable(isEditable());
        }
        return this.columnDescriptor;
    }


    public ValueHandler getValueHandler()
    {
        if(this.valueHandler == null)
        {
            this.valueHandler = (ValueHandler)new WorkflowImgValueHandler(getName());
        }
        return this.valueHandler;
    }


    public CellRenderer getCellRenderer()
    {
        if("Status".equalsIgnoreCase(getName()))
        {
            if(getWorkflowStatusRenderer() == null)
            {
                setWorkflowStatusRenderer(new WorkflowStatusImageCellRenderer());
            }
            return (CellRenderer)getWorkflowStatusRenderer();
        }
        if(super.getCellRenderer() == null)
        {
            setCellRenderer((CellRenderer)new ImageCellRenderer());
        }
        return super.getCellRenderer();
    }


    private void setWorkflowStatusRenderer(WorkflowStatusImageCellRenderer workflowStatusImageCellRenderer)
    {
        this.workflowStatusRenderer = workflowStatusImageCellRenderer;
    }


    private WorkflowStatusImageCellRenderer getWorkflowStatusRenderer()
    {
        return this.workflowStatusRenderer;
    }
}
