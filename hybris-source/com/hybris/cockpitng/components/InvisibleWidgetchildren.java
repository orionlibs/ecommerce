/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.Widget;
import java.util.List;

/**
 *
 */
public class InvisibleWidgetchildren extends Widgetchildren
{
    private static final long serialVersionUID = 6615419353721722391L;
    private final List<Widget> additionalChildren;


    public InvisibleWidgetchildren(final List<Widget> additionalChildren)
    {
        super();
        this.additionalChildren = additionalChildren;
    }


    public List<Widget> getAdditionalChildren()
    {
        return additionalChildren;
    }
}
