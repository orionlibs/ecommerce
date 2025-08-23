/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.config.compareview.jaxb.Header;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.CompareViewController;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;

public class DefaultRemoveItemRenderer extends AbstractWidgetComponentRenderer<HtmlBasedComponent, Header, Object>
{
    private static final String YW_IMAGE_REMOVE_ITEM_ICON = "yw-image-remove-item-icon";
    private static final String SCLASS_YE_TEXT_BUTTON = "y-btn";
    private static final String LABEL_REMOVE_ITEM_TOOLTIP_TEXT = "remove.item.tooltip";
    private WidgetRenderingUtils widgetRenderingUtils;


    @Override
    public void render(final HtmlBasedComponent parent, final Header configuration, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        parent.appendChild(createRemoveItemButton(parent, item, widgetInstanceManager));
        fireComponentRendered(parent, configuration, item);
    }


    /**
     * @deprecated since 1811, please use {@link #createRemoveItemButton(Component, Object, WidgetInstanceManager)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Component createRemoveItemButton(final Component parent, final Object item)
    {
        final Button removeItemButton = new Button();
        assignSClassToRemoveItemButton(removeItemButton);
        getWidgetRenderingUtils().markComponent(parent, removeItemButton, CompareViewController.MARK_NAME_REMOVE_ITEM, item);
        return removeItemButton;
    }


    protected Component createRemoveItemButton(final Component parent, final Object item, final WidgetInstanceManager widgetInstanceManager)
    {
        final Component removeItemButton = createRemoveItemButton(parent, item);
        if(removeItemButton instanceof Button)
        {
            final String tooltipText = widgetInstanceManager.getLabel(LABEL_REMOVE_ITEM_TOOLTIP_TEXT);
            ((Button)removeItemButton).setTooltiptext(tooltipText);
        }
        return removeItemButton;
    }


    protected void assignSClassToRemoveItemButton(final HtmlBasedComponent htmlBasedComponent)
    {
        UITools.addSClass(htmlBasedComponent, YW_IMAGE_REMOVE_ITEM_ICON);
        UITools.addSClass(htmlBasedComponent, SCLASS_YE_TEXT_BUTTON);
    }


    protected WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    @Required
    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
    }
}
