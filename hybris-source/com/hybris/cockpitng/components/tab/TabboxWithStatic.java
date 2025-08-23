/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.tab;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.Tabbox;

/**
 * Extension of a {@link Tabbox} which allows to add a static section {@link TabboxStaticSection}. Static section should
 * be added as last child of the tabbox.
 */
public class TabboxWithStatic extends Tabbox
{
    private TabboxStaticSection staticSection;


    @Override
    public void beforeChildAdded(final Component child, final Component refChild)
    {
        if(child instanceof TabboxStaticSection)
        {
            if(staticSection != null && staticSection != child)
            {
                throw new UiException("Only one Static Content Allowed: " + this);
            }
        }
        else
        {
            super.beforeChildAdded(child, refChild);
        }
    }


    @Override
    public boolean insertBefore(final Component child, final Component refChild)
    {
        if(child instanceof TabboxStaticSection && super.insertBefore(child, refChild))
        {
            staticSection = (TabboxStaticSection)child;
            return true;
        }
        return super.insertBefore(child, refChild);
    }


    @Override
    public void onChildRemoved(final Component child)
    {
        if(staticSection == child)
        {
            staticSection = null;
        }
        super.onChildRemoved(child);
    }


    public TabboxStaticSection getStaticSection()
    {
        return staticSection;
    }
}
