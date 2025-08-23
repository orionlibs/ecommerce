/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import com.hybris.cockpitng.core.Widget;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

/**
 * Useful utility methods to work with {@link Widget} tree structure.
 */
public final class WidgetTreeUtils
{
    public static final String COMPOSED_SLOT_SEPARATOR = "$";


    private WidgetTreeUtils()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    public static Widget getRootWidget(final Widget widget)
    {
        Widget ret = widget;
        Widget current = widget.getParent();
        while(current != null)
        {
            ret = current;
            current = current.getParent();
        }
        return ret;
    }


    public static String getSlotIdPathToComposedRoot(final Widget widget)
    {
        String ret = widget.getSlotId();
        if(widget.getGroupContainer() == null && widget.getParent() != null)
        {
            ret = getSlotIdPathToComposedRoot(widget.getParent()) + COMPOSED_SLOT_SEPARATOR + ret;
        }
        return ret;
    }


    /**
     * @param widget
     * @return
     */
    public static Set<Widget> getAllChildWidgetsRecursively(final Widget widget)
    {
        if(CollectionUtils.isEmpty(widget.getChildren()))
        {
            return Collections.<Widget>emptySet();
        }
        else
        {
            final Set<Widget> ret = new HashSet<Widget>();
            final List<Widget> children = widget.getChildren();
            ret.addAll(children);
            for(final Widget child : children)
            {
                ret.addAll(getAllChildWidgetsRecursively(child));
            }
            return ret;
        }
    }


    public static Widget getCommonParent(final Widget widget1, final Widget widget2)
    {
        final List<Widget> path1 = getPath(widget1);
        final List<Widget> path2 = getPath(widget2);
        for(int i = path1.size() - 1; i >= 0; i--)
        {
            final Widget parentWidget1 = path1.get(i);
            for(int j = path2.size() - 1; j >= 0; j--)
            {
                final Widget parentWidget2 = path2.get(j);
                if(parentWidget1.equals(parentWidget2))
                {
                    return parentWidget1;
                }
            }
        }
        return path1.get(0);
    }


    private static List<Widget> getPath(final Widget widget)
    {
        final LinkedList<Widget> path = new LinkedList<Widget>();
        Widget current = widget;
        do
        {
            path.addFirst(current);
            current = current.getParent();
        }
        while(current != null);
        return path;
    }
}
