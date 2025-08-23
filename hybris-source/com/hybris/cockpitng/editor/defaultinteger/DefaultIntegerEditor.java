/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultinteger;

import com.hybris.cockpitng.editor.integral.AbstractIntegralEditor;

/**
 * Default Editor for {@link Integer} values.
 */
public class DefaultIntegerEditor extends AbstractIntegralEditor<Integer>
{
    public DefaultIntegerEditor()
    {
        super(Integer.class, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
