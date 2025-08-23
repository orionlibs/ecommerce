/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultdecimal;

import com.hybris.cockpitng.editor.decimal.AbstractDecimalEditor;

/**
 * Default Editor for {@link Double} values.
 */
public class DefaultDecimalEditor extends AbstractDecimalEditor<Double>
{
    public DefaultDecimalEditor()
    {
        super(Double.class, -Double.MAX_VALUE, Double.MAX_VALUE);
    }
}
