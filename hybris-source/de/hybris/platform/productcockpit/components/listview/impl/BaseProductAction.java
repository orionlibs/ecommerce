package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;

public class BaseProductAction extends VariantsStatus
{
    private static final Logger LOG = LoggerFactory.getLogger(VariantsAction.class);
    public static final String PROPERTY_DESCRIPTOR = "propertyDescriptor";
    private static final String ICON_FUNC_OPEN_BASEPRODUCTLIST = "cockpit/images/icon_func_open_baseproduct.png";
    private static final String ICON_FUNC_OPEN_BASEPRODUCTLIST_UNAVAILABLE = "cockpit/images/icon_func_open_baseproduct_unavailable.png";


    public String getImageURI(ListViewAction.Context context)
    {
        TypedObject item = getItem(context);
        if(item != null && item.getObject() instanceof de.hybris.platform.variants.model.VariantProductModel)
        {
            return "cockpit/images/icon_func_open_baseproduct.png";
        }
        return "cockpit/images/icon_func_open_baseproduct_unavailable.png";
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        Object object = null;
        TypedObject item = getItem(context);
        if(item != null && item.getObject() instanceof de.hybris.platform.variants.model.VariantProductModel)
        {
            object = new Object(this, item);
        }
        return (EventListener)object;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("gridview.item.base.product.info");
    }
}
