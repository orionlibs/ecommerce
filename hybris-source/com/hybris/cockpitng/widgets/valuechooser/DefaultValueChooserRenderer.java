/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.valuechooser;

import com.hybris.cockpitng.config.valuechooser.jaxb.Option;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;

public class DefaultValueChooserRenderer extends AbstractWidgetComponentRenderer<Component, Option, Boolean>
{
    private static final String SCLASS_OPTION = "yw-valuechooser-option";


    @Override
    public void render(final Component component, final Option configuration, final Boolean selected, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Button optionButton = new Button(widgetInstanceManager.getLabel(configuration.getLabel()));
        UITools.addSClass(optionButton, SCLASS_OPTION);
        component.getEventListeners(Events.ON_CLICK)
                        .forEach(eventListener -> optionButton.addEventListener(Events.ON_CLICK, eventListener));
        component.appendChild(optionButton);
        fireComponentRendered(optionButton, component, configuration, selected);
        fireComponentRendered(component, configuration, selected);
    }
}
