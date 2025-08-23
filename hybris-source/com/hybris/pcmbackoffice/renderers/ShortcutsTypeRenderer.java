package com.hybris.pcmbackoffice.renderers;

import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Span;

public class ShortcutsTypeRenderer extends AbstractWidgetComponentRenderer<Component, Object, ProductModel>
{
    private static final String YW_IMAGE_SHORTCUTS_PRODUCT_TYPE = "yw-image-shortcuts-product-type-";
    private static final String YW_IMAGE_ATTRIBUTE_ICON = "yw-image-attribute-approval-icon";
    private static final String BLCOKED_LIST_TOOLTIP = "shortcuts.blocked.list.status.icon.tooltip";
    private static final String QUICK_LIST_TOOLTIP = "shortcuts.quick.list.status.icon.tooltip";
    private static final String BLOCKED_LIST = "blockedlist";
    private static final String QUICK_LIST = "quicklist";
    private ShortcutsService shortcutsService;


    public void render(Component parent, Object configuration, ProductModel data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        BackofficeObjectSpecialCollectionModel blockedlistCollection = getShortcutsService().initCollection("blockedlist");
        BackofficeObjectSpecialCollectionModel quicklistCollection = getShortcutsService().initCollection("quicklist");
        if(getShortcutsService().collectionContainsItem(data, blockedlistCollection))
        {
            parent.appendChild((Component)createIcon("blockedlist", Labels.getLabel("shortcuts.blocked.list.status.icon.tooltip")));
        }
        else if(getShortcutsService().collectionContainsItem(data, quicklistCollection))
        {
            parent.appendChild((Component)createIcon("quicklist", Labels.getLabel("shortcuts.quick.list.status.icon.tooltip")));
        }
        fireComponentRendered(parent, configuration, data);
    }


    protected HtmlBasedComponent createIcon(String productType, String iconTooltip)
    {
        Span icon = new Span();
        UITools.addSClass((HtmlBasedComponent)icon, "yw-image-attribute-approval-icon");
        String sclass = "yw-image-shortcuts-product-type-".concat(productType);
        UITools.addSClass((HtmlBasedComponent)icon, sclass);
        icon.setTooltiptext(iconTooltip);
        return (HtmlBasedComponent)icon;
    }


    @Required
    public void setShortcutsService(ShortcutsService shortcutsService)
    {
        this.shortcutsService = shortcutsService;
    }


    public ShortcutsService getShortcutsService()
    {
        return this.shortcutsService;
    }
}
