/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.table;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

public class TableHeader extends TableRows
{
    @Override
    public void beforeChildAdded(final Component child, final Component insertBefore)
    {
        if(!(child instanceof TableRow))
        {
            throw new UiException("Unsupported child for header: " + child);
        }
        super.beforeChildAdded(child, insertBefore);
    }
}
