package de.hybris.platform.admincockpit.services.config.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTextCellRenderer;
import de.hybris.platform.cockpit.services.config.impl.DefaultCustomColumnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstraintGroupsCustomColumnConfiguration extends DefaultCustomColumnConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTextCellRenderer.class);
    private DefaultColumnDescriptor colDescr = null;


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            ConstraintGroupCellRenderer constraintGroupCellRenderer = new ConstraintGroupCellRenderer(this);
            setCellRenderer((CellRenderer)constraintGroupCellRenderer);
        }
        return super.getCellRenderer();
    }


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        if(this.colDescr == null)
        {
            this.colDescr = new DefaultColumnDescriptor(getName());
            this.colDescr.setEditable(false);
            this.colDescr.setSelectable(false);
            this.colDescr.setSortable(true);
            this.colDescr.setVisible(this.visible);
        }
        return this.colDescr;
    }
}
