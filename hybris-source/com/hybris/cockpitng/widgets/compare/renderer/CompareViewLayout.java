/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.CompareViewData;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface CompareViewLayout extends WidgetComponentRenderer<HtmlBasedComponent, CompareView, CompareViewData>
{
    default void onUpdateItemType()
    {
    }
}
