/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.tree.model.helper;

import com.hybris.backoffice.navigation.NavigationNode;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public final class AsciiTreePrettyPrinter
{
    private AsciiTreePrettyPrinter()
    {
        //utility class
    }


    public static String prettyPrintTree(final List<NavigationNode> tree)
    {
        final StringBuilder sb = new StringBuilder();
        for(final NavigationNode node : tree)
        {
            printRow(sb, node, StringUtils.EMPTY, StringUtils.EMPTY);
        }
        return sb.toString();
    }


    private static void printRow(final StringBuilder sb, final NavigationNode child, final String prefixTree,
                    final String childPrefix)
    {
        sb.append(prefixTree);
        sb.append(child.getName());
        sb.append(StringUtils.LF);
        final Iterator<NavigationNode> it = child.getChildren().iterator();
        while(it.hasNext())
        {
            final NavigationNode next = it.next();
            if(it.hasNext())
            {
                printRow(sb, next, childPrefix + "├─ ", childPrefix + "│ ");
            }
            else
            {
                printRow(sb, next, childPrefix + "╰─ ", childPrefix + " ");
            }
        }
    }
}
