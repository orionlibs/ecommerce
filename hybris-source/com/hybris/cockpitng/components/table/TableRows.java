/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.table;

import com.hybris.cockpitng.components.table.iterator.DefaultTableComponentIterator;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import java.util.function.Supplier;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.UiException;

public class TableRows extends HtmlBasedComponent
{
    public TableComponentIterator<TableRow> rowsIterator()
    {
        return new DefaultTableComponentIterator<>(this, TableRow.class, newRowsSupplier());
    }


    protected Supplier<TableRow> newRowsSupplier()
    {
        return TableRow::new;
    }


    public TableComponentIterator<TableRowsGroup> groupsIterator()
    {
        return new DefaultTableComponentIterator<>(this, TableRowsGroup.class, newGroupsSupplier());
    }


    protected Supplier<TableRowsGroup> newGroupsSupplier()
    {
        return TableRowsGroup::new;
    }


    @Override
    public void beforeChildAdded(final Component child, final Component insertBefore)
    {
        if(!(child instanceof TableRow) && !(child instanceof TableRowsGroup))
        {
            throw new UiException("Unsupported child for rows: " + child);
        }
        super.beforeChildAdded(child, insertBefore);
    }
}
