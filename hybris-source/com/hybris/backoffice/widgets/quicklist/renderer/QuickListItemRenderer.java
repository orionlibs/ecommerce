/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicklist.renderer;

import org.zkoss.zk.ui.Component;

/**
 * Interface for renderers capable of rendering items of QuickList widget
 */
public interface QuickListItemRenderer
{
    /**
     * Checks if given {@link Component} is a hyperlink opening the current item
     * @param component ui component
     * @return true if is open hyperlink
     */
    boolean isOpenItemHyperlink(Component component);


    /**
     * Checks if given {@link Component} is a hyperlink opening the current item
     * @param component ui component
     * @return true if is remove item
     */
    boolean isRemoveItemButton(Component component);
}
