/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.table;

import java.io.IOException;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

public class TableCell extends HtmlBasedComponent
{
    private static final String PROPERTY_STICKY = "sticky";
    private boolean sticky;


    public boolean isSticky()
    {
        return sticky;
    }


    public void setSticky(final boolean sticky)
    {
        if(this.sticky != sticky)
        {
            this.sticky = sticky;
            smartUpdate(PROPERTY_STICKY, sticky);
        }
    }


    @Override
    protected void renderProperties(final ContentRenderer renderer) throws IOException
    {
        super.renderProperties(renderer);
        render(renderer, PROPERTY_STICKY, sticky);
    }
}
