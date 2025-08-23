package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class VariantsStatus extends AbstractProductAction
{
    protected static final String ICON_BASE_PORDUCT_STATUS = "productcockpit/images/icon_status_baseproduct_a.png";
    protected static final String ICON_VARIANT_PORDUCT_STATUS = "productcockpit/images/icon_status_variant_a.png";
    protected static final String ICON_BASE_VARIANT_STATUS = "productcockpit/images/icon_base_variant_a.png";
    private TypeService typeService = null;


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        String uri = "productcockpit/images/icon_status_baseproduct_a.png";
        if(context.getItem().getObject() instanceof de.hybris.platform.variants.model.VariantProductModel)
        {
            uri = "productcockpit/images/icon_status_variant_a.png";
        }
        if(context.getItem().getObject() instanceof de.hybris.platform.variants.model.VariantProductModel &&
                        getProductCockpitProductService().isBaseProduct(context.getItem()))
        {
            uri = "productcockpit/images/icon_base_variant_a.png";
        }
        return uri;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        String label = Labels.getLabel("gridview.item.base.product.tooltip");
        if(context.getItem().getObject() instanceof de.hybris.platform.variants.model.VariantProductModel)
        {
            label = Labels.getLabel("gridview.item.variant.product.tooltip");
        }
        if(context.getItem().getObject() instanceof de.hybris.platform.variants.model.VariantProductModel &&
                        getProductCockpitProductService().hasVariants(context.getItem()))
        {
            label = Labels.getLabel("gridview.item.base.variant.product.tooltip");
        }
        return label;
    }


    public String getStatusCode(ListViewAction.Context context)
    {
        return getProductCockpitProductService().hasVariants(context.getItem()) ? "enabled" : "disabled";
    }
}
