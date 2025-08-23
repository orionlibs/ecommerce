/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultshort;

import com.hybris.cockpitng.editor.integral.AbstractIntegralEditor;

/**
 * Default Editor for {@link Short} values.
 */
public class DefaultShortEditor extends AbstractIntegralEditor<Short>
{
    public DefaultShortEditor()
    {
        super(Short.class, Short.MIN_VALUE, Short.MAX_VALUE);
    }
}
