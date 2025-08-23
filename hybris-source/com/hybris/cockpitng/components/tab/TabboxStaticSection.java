/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.tab;

import org.zkoss.zul.Div;

/**
 * Static section rendered next to a tab panels {@link org.zkoss.zul.Tabpanels} in a {@link TabboxWithStatic}
 */
public class TabboxStaticSection extends Div
{
    public TabboxStaticSection()
    {
        TabboxStaticSection.this.setWidgetOverride("staticSection", "true");
        TabboxStaticSection.this.setZclass("z-tabbox-static-section");
    }
}
