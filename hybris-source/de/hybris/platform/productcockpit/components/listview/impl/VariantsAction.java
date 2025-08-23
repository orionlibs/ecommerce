package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;

public class VariantsAction extends VariantsStatus
{
    private static final Logger LOG = LoggerFactory.getLogger(VariantsAction.class);
    protected static final String IMAGE_URI = "cockpit/images/icon_func_variant_list_available.png";
    protected static final String IMAGE_URI_UNAVAILABLE = "cockpit/images/icon_func_variant_list_unavailable.png";


    public EventListener getEventListener(ListViewAction.Context context)
    {
        Object object = null;
        TypedObject item = getItem(context);
        if(item != null && getProductCockpitProductService().isBaseProduct(item))
        {
            object = new Object(this, item);
        }
        return (EventListener)object;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        TypedObject item = getItem(context);
        if(item != null && getProductCockpitProductService().isBaseProduct(item))
        {
            return "cockpit/images/icon_func_variant_list_available.png";
        }
        return "cockpit/images/icon_func_variant_list_unavailable.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("gridview.item.variant.product.info");
    }
}
