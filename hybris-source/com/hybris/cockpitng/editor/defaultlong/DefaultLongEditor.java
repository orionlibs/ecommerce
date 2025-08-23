/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultlong;

import com.hybris.cockpitng.editor.integral.AbstractIntegralEditor;

/**
 * Default Editor for {@link Long} values.
 */
public class DefaultLongEditor extends AbstractIntegralEditor<Long>
{
    public DefaultLongEditor()
    {
        super(Long.class, Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
