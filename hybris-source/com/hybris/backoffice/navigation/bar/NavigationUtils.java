/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation.bar;

import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.backoffice.actionbar.impl.ActionComponentUtils;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

/**
 * Utilities methods to help using navigation components
 */
public class NavigationUtils extends ActionComponentUtils
{
    private static final String SCLASS_SELECTED_NODE = "yw-selectednode";
    private static final String ATTRIBUTE_SELECTED_NODE = "selectedAction";


    private NavigationUtils()
    {
        super();
    }


    /**
     * Executes all required actions after a navigation node has been selected
     *
     * @param parent
     *           navigation bar
     * @param context
     *           navigation context
     * @param item
     *           a component representation of selected navigation node
     */
    public static void nodeSelected(final Component parent, final NavigationContext context, final HtmlBasedComponent item)
    {
        final ActionDefinition node = getDefinition(item);
        context.getActionListener().actionExecuted(node);
        final HtmlBasedComponent currentSelection = getSelectedItem(parent);
        if(currentSelection != null && !currentSelection.equals(item))
        {
            UITools.modifySClass(currentSelection, SCLASS_SELECTED_NODE, false);
        }
        if(item != null && !item.equals(currentSelection))
        {
            parent.setAttribute(ATTRIBUTE_SELECTED_NODE, item);
            UITools.modifySClass(item, SCLASS_SELECTED_NODE, true);
        }
    }


    /**
     * Looks for a navigation node that is selected in navigation bar and executes all required actions for this node.
     *
     * @param parent
     *           navigation bar
     * @param context
     *           navigation context
     * @param nodeComponent
     *           a component in which to look for selected node
     */
    public static void nodeSelectionChanged(final Component parent, final NavigationContext context,
                    final HtmlBasedComponent nodeComponent)
    {
        final Object node = getDefinition(nodeComponent);
        if((node instanceof NavigationNode) && context.isSelected((NavigationNode)node))
        {
            nodeSelected(parent, context, nodeComponent);
        }
        if(nodeComponent != null)
        {
            nodeComponent.getChildren().forEach(child -> {
                if(child instanceof HtmlBasedComponent)
                {
                    nodeSelectionChanged(parent, context, (HtmlBasedComponent)child);
                }
            });
        }
    }


    /**
     * Gets component representation of currently selected navigation node for bar
     *
     * @param parent
     *           navigation bar
     * @return currently selected node or <code>null</code> if nothing is selected
     * @see #getDefinition(HtmlBasedComponent)
     */
    public static HtmlBasedComponent getSelectedItem(final Component parent)
    {
        return (HtmlBasedComponent)parent.getAttribute(ATTRIBUTE_SELECTED_NODE);
    }
}
