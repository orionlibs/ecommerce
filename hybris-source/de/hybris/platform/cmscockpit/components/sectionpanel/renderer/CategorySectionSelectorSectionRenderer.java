package de.hybris.platform.cmscockpit.components.sectionpanel.renderer;

import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSectionSelectorSectionRenderer;
import org.zkoss.zul.ListitemRenderer;

public class CategorySectionSelectorSectionRenderer extends DefaultSectionSelectorSectionRenderer
{
    private CategorySelectorListRenderer listItemRenderer = null;


    public ListitemRenderer getListRenderer()
    {
        if(this.listItemRenderer == null)
        {
            this.listItemRenderer = new CategorySelectorListRenderer(this);
        }
        return (ListitemRenderer)this.listItemRenderer;
    }
}
