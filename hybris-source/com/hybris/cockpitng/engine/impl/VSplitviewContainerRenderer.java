/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import org.zkoss.zul.Box;
import org.zkoss.zul.Vbox;

/**
 * {@link AbstractSplitviewContainerRenderer} with a vertical alignment.
 */
public class VSplitviewContainerRenderer extends AbstractSplitviewContainerRenderer
{
    /**
     * Returns a {@link Vbox}
     */
    @Override
    protected Box createBoxComponent()
    {
        return new Vbox();
    }
}
