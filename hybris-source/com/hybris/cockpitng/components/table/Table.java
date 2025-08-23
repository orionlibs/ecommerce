/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.table;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.ContentRenderer;

public class Table extends HtmlBasedComponent
{
    private static final String PROPERTY_FROZEN = "frozen";
    private final TableHeader headerRows;
    private boolean frozen;


    public Table()
    {
        headerRows = initHeader();
    }


    private TableHeader initHeader()
    {
        final TableHeader header = new TableHeader();
        appendChild(header);
        return header;
    }


    @Override
    public void beforeChildAdded(final Component child, final Component insertBefore)
    {
        if(child instanceof TableHeader)
        {
            if(this.headerRows != null && this.headerRows != child)
            {
                throw new UiException(
                                "Only one header rows child is allowed: " + this + "\nNote: header rows is created automatically if live data");
            }
        }
        else if(!(child instanceof TableRows))
        {
            throw new UiException("Unsupported child for table: " + child);
        }
        super.beforeChildAdded(child, insertBefore);
    }


    public TableHeader getHeaderRows()
    {
        return headerRows;
    }


    public TableRows getRows()
    {
        if(getLastChild() == getHeaderRows())
        {
            appendChild(new TableRows());
        }
        return (TableRows)getLastChild();
    }


    public Collection<TableRows> getAllRows()
    {
        if(getLastChild() == getHeaderRows())
        {
            appendChild(new TableRows());
        }
        return getChildren().stream().filter(component -> !(component instanceof TableHeader))
                        .map(component -> (TableRows)component).collect(Collectors.toList());
    }


    public boolean isFrozen()
    {
        return frozen;
    }


    public void setFrozen(final boolean frozen)
    {
        if(this.frozen != frozen)
        {
            this.frozen = frozen;
            smartUpdate(PROPERTY_FROZEN, frozen);
        }
    }


    @Override
    protected void renderProperties(final ContentRenderer renderer) throws IOException
    {
        super.renderProperties(renderer);
        render(renderer, PROPERTY_FROZEN, frozen);
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        final Table table = (Table)o;
        return headerRows.equals(table.headerRows) && super.equals(o);
    }


    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + headerRows.hashCode();
        return result;
    }
}
