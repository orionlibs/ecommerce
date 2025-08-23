package de.hybris.platform.cockpit.components.duallistbox.impl;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.util.TreeUtils;

public class FilterColumnNameResolver implements TreeUtils.FilterStringResolver
{
    public String getStringRepresentation(Object node)
    {
        if(node instanceof ColumnDescriptor)
        {
            return ((ColumnDescriptor)node).getName();
        }
        throw new UnsupportedOperationException("");
    }
}
