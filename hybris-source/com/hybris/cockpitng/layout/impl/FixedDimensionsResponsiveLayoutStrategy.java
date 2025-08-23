/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.layout.impl;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.layout.ResponsiveLayoutStrategy;
import java.util.Map;
import org.zkoss.zk.ui.event.ClientInfoEvent;

public class FixedDimensionsResponsiveLayoutStrategy implements ResponsiveLayoutStrategy
{
    public static final String RESPONSIVE_MINIMAL_WIDTH = "responsiveMinimalWidth";
    public static final String RESPONSIVE_MINIMAL_HEIGHT = "responsiveMinimalHeight";
    private int minWidth = 800;
    private int minHeight = 350;


    @Override
    public boolean isSaveSpaceHorizontally(final ClientInfoEvent info, final Map<String, Object> context)
    {
        return checkSaveSpace(info.getDesktopWidth(), RESPONSIVE_MINIMAL_WIDTH, context);
    }


    @Override
    public boolean isSaveSpaceVertically(final ClientInfoEvent info, final Map<String, Object> context)
    {
        return checkSaveSpace(info.getDesktopHeight(), RESPONSIVE_MINIMAL_HEIGHT, context);
    }


    private boolean checkSaveSpace(final int actualSize, final String key, final Map<String, Object> context)
    {
        final Object o = context.get(key);
        if(o instanceof Number)
        {
            final int minSize = ((Number)o).intValue();
            if(minSize > 0)
            {
                return actualSize < minSize;
            }
        }
        return actualSize < minWidth;
    }


    public int getMinWidth()
    {
        return minWidth;
    }


    public void setMinWidth(final int minWidth)
    {
        Validate.assertTrue("Minimal width must be a positive number", minWidth > 0);
        this.minWidth = minWidth;
    }


    public int getMinHeight()
    {
        return minHeight;
    }


    public void setMinHeight(final int minHeight)
    {
        Validate.assertTrue("Minimal height must be a positive number", minHeight > 0);
        this.minHeight = minHeight;
    }
}
