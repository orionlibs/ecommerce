/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.components.WidgetContainer;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;

/**
 * Useful utility method to work with component tree representing the widget tree structure.
 */
public final class WidgetTreeUIUtils
{
    private WidgetTreeUIUtils()
    {
        throw new AssertionError("Using constructor of this class is prohibited.");
    }


    /**
     * Returns the first parent component being an instance of {@link WidgetContainer}, i.e. either a {@link Widgetslot} or
     * a {@link Widgetchildren}, whatever comes first.
     */
    public static WidgetContainer getParentWidgetContainer(final Component comp)
    {
        Component parent = comp.getParent();
        while(parent != null)
        {
            if(parent instanceof WidgetContainer)
            {
                return ((WidgetContainer)parent);
            }
            parent = parent.getParent();
        }
        return null;
    }


    public static Widgetslot getParentWidgetslot(final Component comp)
    {
        if(comp != null)
        {
            Component parent = comp.getParent();
            while(parent != null)
            {
                if(parent instanceof Widgetslot)
                {
                    return ((Widgetslot)parent);
                }
                parent = parent.getParent();
            }
        }
        return null;
    }


    public static Widgetchildren getParentWidgetchildren(final Component comp)
    {
        Component parent = comp.getParent();
        while(parent != null)
        {
            if(parent instanceof Widgetchildren)
            {
                return ((Widgetchildren)parent);
            }
            parent = parent.getParent();
        }
        return null;
    }


    public static Component getFirstWidgetChild(final Component component)
    {
        Component firstWidget = null;
        for(final Component child : component.getChildren())
        {
            if("widget".equals(child.getDefinition().getName()))
            {
                firstWidget = child;
            }
            else
            {
                firstWidget = getFirstWidgetChild(child);
            }
            if(firstWidget != null)
            {
                break;
            }
        }
        return firstWidget;
    }


    public static void updateRootWidget(final Widgetslot widgetComp, final boolean resetWidget)
    {
        Widgetslot currentCmp = widgetComp;
        Widgetslot parentCmp = getParentWidgetslot(currentCmp);
        while(parentCmp != null)
        {
            currentCmp = getParentWidgetslot(currentCmp);
            parentCmp = getParentWidgetslot(currentCmp);
        }
        if(currentCmp != null)
        {
            if(resetWidget)
            {
                currentCmp.setWidgetInstance(null);
            }
            currentCmp.updateView();
        }
    }


    /**
     * Gets a visible widget on slot with provided id.
     *
     * @param parent
     *           parent slot
     * @param childSlotId
     *           id of slot to be checked
     * @param visibilityCheck
     *           a predicate to check whether a widget is visible
     * @return widget on provided slot
     */
    public static Widget getVisibleWidget(final Widgetslot parent, final String childSlotId,
                    final Predicate<Widget> visibilityCheck)
    {
        final List<Widget> visibleChildren = getVisibleWidgets(parent, visibilityCheck);
        final Optional<Widget> first = visibleChildren.stream()
                        .filter(widget -> StringUtils.equals(childSlotId, widget.getSlotId())).findFirst();
        return first.orElse(null);
    }


    /**
     * Returns a list of widgets that are children of the given widget slot that are visible according to
     * <code>visibilityCheck</code> method.
     *
     * @param widgetslot
     *           {@link Widgetslot} for which the visible widgets should be found
     * @return {@link List} of {@link Widget}
     */
    public static List<Widget> getVisibleWidgets(final Widgetslot widgetslot, final Predicate<Widget> visibilityCheck)
    {
        final List<Widget> children = widgetslot.getWidgetInstance().getWidget().getChildren();
        return children.stream().filter(visibilityCheck).collect(Collectors.toList());
    }
}
