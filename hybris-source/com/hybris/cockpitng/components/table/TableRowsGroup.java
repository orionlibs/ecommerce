/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.table;

import com.hybris.cockpitng.components.table.iterator.DefaultTableComponentIterator;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import java.io.IOException;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.NoDOM;

public class TableRowsGroup extends NoDOM
{
    private static final String PROPERTY_ZCLASS = "zclass";
    private static final String PROPERTY_COLLAPSED = "collapsed";
    private String zclass;
    private boolean collapsed;
    private final TableRow headerRow;


    public TableRowsGroup()
    {
        headerRow = initHeader();
    }


    public String getZclass()
    {
        return this.zclass;
    }


    public void setZclass(String zclass)
    {
        if(StringUtils.isBlank(zclass))
        {
            zclass = null;
        }
        if(!Objects.equals(this.zclass, zclass))
        {
            this.zclass = zclass;
            this.smartUpdate(PROPERTY_ZCLASS, this.zclass);
        }
    }


    public boolean isCollapsed()
    {
        return collapsed;
    }


    public void setCollapsed(final boolean collapsed)
    {
        if(this.collapsed != collapsed)
        {
            this.collapsed = collapsed;
            smartUpdate(PROPERTY_COLLAPSED, collapsed);
        }
    }


    private TableRow initHeader()
    {
        final TableRow header = new TableRow();
        appendChild(header);
        return header;
    }


    public TableRow getHeaderRow()
    {
        return headerRow;
    }


    public TableComponentIterator<TableRow> rowsIterator()
    {
        final TableComponentIterator<TableRow> iterator = new DefaultTableComponentIterator<>(this, TableRow.class,
                        newRowsSupplier());
        iterator.next();
        return iterator;
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
            throw new UiException("Unsupported child for group: " + child);
        }
        super.beforeChildAdded(child, insertBefore);
    }


    @Override
    protected void renderProperties(final ContentRenderer renderer) throws IOException
    {
        super.renderProperties(renderer);
        render(renderer, PROPERTY_COLLAPSED, isCollapsed());
        render(renderer, PROPERTY_ZCLASS, getZclass());
    }
}
