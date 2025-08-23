/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import org.zkoss.zul.Box;
import org.zkoss.zul.Hbox;

/**
 * {@link AbstractSplitviewContainerRenderer} with a horizontal alignment.
 */
public class HSplitviewContainerRenderer extends AbstractSplitviewContainerRenderer
{
    /**
     * Returns a {@link Hbox}
     */
    @Override
    protected Box createBoxComponent()
    {
        return new Hbox();
    }
}
