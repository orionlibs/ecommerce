/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation;

public final class NavigationNodeDecoratorUtils
{
    private NavigationNodeDecoratorUtils()
    {
        // blocks the possibility of create a new instance
    }


    public static NavigationNode unwrap(final NavigationNode node)
    {
        return node instanceof NavigationNodeDecorator ? ((NavigationNodeDecorator)node).getTarget() : node;
    }
}
