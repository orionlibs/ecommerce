package de.hybris.platform.admincockpit.services.config.impl;

import de.hybris.platform.admincockpit.services.ResourceAwareConfiguration;
import de.hybris.platform.admincockpit.services.TypeAwareResourceResolver;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.impl.ImageCellRenderer;
import de.hybris.platform.cockpit.services.config.impl.DefaultCustomColumnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class IconTypeConstraintCustomColumnConfiguration extends DefaultCustomColumnConfiguration implements ResourceAwareConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(ImageCellRenderer.class);
    private TypeAwareResourceResolver<String> resolver;
    private DefaultColumnDescriptor colDescr = null;


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            TypeAwareImageCellRenderer typeAwareImageCellRenderer = new TypeAwareImageCellRenderer(this);
            setCellRenderer((CellRenderer)typeAwareImageCellRenderer);
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


    @Required
    public void setTypeAwareResourceResolver(TypeAwareResourceResolver<String> resolver)
    {
        this.resolver = resolver;
    }
}
