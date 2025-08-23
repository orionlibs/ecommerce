package de.hybris.platform.cmscockpit.model.gridview.impl;

import de.hybris.platform.cockpit.model.gridview.impl.DefaultGridItemRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;

public class CmsGridItemRenderer extends DefaultGridItemRenderer
{
    protected String getFallbackImage(TypedObject item)
    {
        String result;
        if(item != null && item.getObject() != null)
        {
            result = "/cmscockpit/images/" + item.getObject().getClass().getSimpleName() + "_nopreview.gif";
        }
        else
        {
            result = getFallbackImage();
        }
        return result;
    }
}
