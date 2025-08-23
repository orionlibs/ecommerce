/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultfloat;

import com.hybris.cockpitng.editor.decimal.AbstractDecimalEditor;

/**
 * Default Editor for {@link java.lang.Float} values.
 */
public class DefaultFloatEditor extends AbstractDecimalEditor<Float>
{
    public DefaultFloatEditor()
    {
        super(Float.class, -Float.MAX_VALUE, Float.MAX_VALUE);
    }
}
