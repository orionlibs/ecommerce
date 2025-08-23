/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.grid;

import org.zkoss.zk.ui.Component;

public interface GridBoxItemRenderer
{
    void render(Component parent, Object data, int position);
}
