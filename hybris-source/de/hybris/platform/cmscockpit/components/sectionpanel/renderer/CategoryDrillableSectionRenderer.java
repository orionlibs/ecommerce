package de.hybris.platform.cmscockpit.components.sectionpanel.renderer;

import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultDrillableSelectorSectionRenderer;
import org.zkoss.zul.ListitemRenderer;

public class CategoryDrillableSectionRenderer extends DefaultDrillableSelectorSectionRenderer
{
    private CategoryDrillableListRenderer listItemRenderer = null;


    public ListitemRenderer getListRenderer()
    {
        if(this.listItemRenderer == null)
        {
            this.listItemRenderer = new CategoryDrillableListRenderer(this);
        }
        return (ListitemRenderer)this.listItemRenderer;
    }
}
