package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.InlineItemColumnDescriptor;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;

public class InlineItemCreateButtonColumn extends DefaultCustomColumnConfiguration
{
    private InlineItemColumnDescriptor colDescr = null;


    public InlineItemCreateButtonColumn()
    {
        setEditable(false);
        String label = Labels.getLabel("contextarea.button.createnewitem");
        if(!StringUtils.isBlank(label))
        {
            long length = Math.round(label.length() * 0.6D);
            setWidth("" + length + "em");
        }
    }


    public InlineItemCreateButtonColumn(String name)
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
            setCellRenderer((CellRenderer)new Object(this));
        }
        return super.getCellRenderer();
    }


    public InlineItemColumnDescriptor getColumnDescriptor()
    {
        if(this.colDescr == null)
        {
            this.colDescr = new InlineItemColumnDescriptor(getName());
            this.colDescr.setEditable(false);
            this.colDescr.setSelectable(false);
            this.colDescr.setSortable(false);
            this.colDescr.setVisible(true);
        }
        return this.colDescr;
    }
}
