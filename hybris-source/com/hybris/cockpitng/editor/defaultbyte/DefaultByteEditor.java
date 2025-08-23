/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultbyte;

import com.hybris.cockpitng.editor.integral.AbstractIntegralEditor;

/**
 * Default Editor for {@link Byte} values.
 */
public class DefaultByteEditor extends AbstractIntegralEditor<Byte>
{
    public DefaultByteEditor()
    {
        super(Byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }
}
