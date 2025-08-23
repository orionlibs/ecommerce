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

public class TableRow extends HtmlBasedComponent
{
    public TableComponentIterator<TableCell> cellsIterator()
    {
        return new DefaultTableComponentIterator<>(this, TableCell.class, newCellsSupplier());
    }


    protected Supplier<TableCell> newCellsSupplier()
    {
        return TableCell::new;
    }


    @Override
    public void beforeChildAdded(final Component child, final Component insertBefore)
    {
        if(!(child instanceof TableCell))
        {
            throw new UiException("Unsupported child for header row: " + child);
        }
        super.beforeChildAdded(child, insertBefore);
    }
}
