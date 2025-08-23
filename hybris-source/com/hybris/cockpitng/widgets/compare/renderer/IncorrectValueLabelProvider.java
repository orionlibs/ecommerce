/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.zkoss.zk.ui.HtmlBasedComponent;

@FunctionalInterface
public interface IncorrectValueLabelProvider<ELEMENT, DATA>
{
    HtmlBasedComponent provideLabel(final ELEMENT configuration, final DATA data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager);
}
