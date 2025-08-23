/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Span;

public class ProductTypeMarkerRenderer extends AbstractWidgetComponentRenderer<Component, Object, ItemModel>
{
    public static final String YW_IMAGE_ATTRIBUTE_VARIANT_PRODUCT = "yw-image-attribute-product-type-variant";
    public static final String YW_IMAGE_ATTRIBUTE_BASE_PRODUCT = "yw-image-attribute-product-type-base";
    private LabelService labelService;


    @Override
    public void render(final Component parent, final Object configuration, final ItemModel data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        if(data instanceof VariantProductModel)
        {
            renderIcon(parent, YW_IMAGE_ATTRIBUTE_VARIANT_PRODUCT, data);
        }
        else if(data instanceof ProductModel)
        {
            renderIcon(parent, YW_IMAGE_ATTRIBUTE_BASE_PRODUCT, data);
        }
        fireComponentRendered(parent, configuration, data);
    }


    protected void renderIcon(final Component parent, final String cssStyle, final ItemModel itemModel)
    {
        final Span icon = new Span();
        UITools.addSClass(icon, cssStyle);
        icon.setTooltiptext(labelService.getObjectLabel(itemModel.getItemtype()));
        parent.appendChild(icon);
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }
}
