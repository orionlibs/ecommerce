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

public class DefaultPinRenderer extends AbstractWidgetComponentRenderer<HtmlBasedComponent, Header, Object>
{
    private static final String YW_IMAGE_PIN_ICON = "yw-image-pin-icon";
    private static final String SCLASS_YE_TEXT_BUTTON = "y-btn";
    private static final String LABEL_PIN_TOOLTIP_TEXT = "pin.tooltip";
    private WidgetRenderingUtils widgetRenderingUtils;


    @Override
    public void render(final HtmlBasedComponent parent, final Header configuration, final Object data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        parent.appendChild(createPinButton(parent, data, widgetInstanceManager));
        fireComponentRendered(parent, configuration, data);
    }


    /**
     * @deprecated since 1811, please use {@link #createPinButton(Component, Object, WidgetInstanceManager)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Component createPinButton(final Component parent, final Object data)
    {
        final Button pinButton = new Button();
        assignSClassToPinButton(pinButton);
        getWidgetRenderingUtils().markComponent(parent, pinButton, CompareViewController.MARK_NAME_PIN, data);
        return pinButton;
    }


    protected Component createPinButton(final Component parent, final Object data,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Component pinButton = createPinButton(parent, data);
        if(pinButton instanceof Button)
        {
            final String tooltipText = widgetInstanceManager.getLabel(LABEL_PIN_TOOLTIP_TEXT);
            ((Button)pinButton).setTooltiptext(tooltipText);
        }
        return pinButton;
    }


    protected void assignSClassToPinButton(final HtmlBasedComponent htmlBasedComponent)
    {
        UITools.addSClass(htmlBasedComponent, YW_IMAGE_PIN_ICON);
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
