/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPositioned;
import java.util.Comparator;

/**
 * Compares AbstractPositioned objects based on AbstractPositioned#getPosition field. Null position is considered as greater
 * than not null position.
 */
public class PositionedElementsComparator implements Comparator<AbstractPositioned>
{
    @Override
    public int compare(final AbstractPositioned o1, final AbstractPositioned o2)
    {
        if(o1.getPosition() == null)
        {
            return o2.getPosition() == null ? 0 : 1;
        }
        else if(o2.getPosition() == null)
        {
            return o1.getPosition() == null ? 0 : -1;
        }
        return o1.getPosition().compareTo(o2.getPosition());
    }
}
