package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.services.label.LabelService;
import org.zkoss.spring.SpringUtil;

public class IconLabelColumnConfiguration extends DefaultCustomColumnConfiguration
{
    private DefaultColumnDescriptor colDescr = null;
    private LabelService labelService = null;


    protected LabelService getLabelService()
    {
        if(this.labelService == null)
        {
            this.labelService = (LabelService)SpringUtil.getBean("labelService", LabelService.class);
        }
        return this.labelService;
    }


    public IconLabelColumnConfiguration()
    {
        setEditable(false);
    }


    public IconLabelColumnConfiguration(String name)
    {
        this.name = name;
    }


    public UIEditor getCellEditor()
    {
        return null;
    }


    public ValueHandler getValueHandler()
    {
        return null;
    }


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            setCellRenderer((CellRenderer)new IconLabelCellRenderer(this));
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
}
