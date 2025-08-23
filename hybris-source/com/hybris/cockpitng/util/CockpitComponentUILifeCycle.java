/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.components.AbstractCockpitComponent;
import com.hybris.cockpitng.components.Widgetslot;
import java.util.Iterator;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.ShadowElement;
import org.zkoss.zk.ui.util.UiLifeCycle;

/**
 * Removes widget model observers of detached components like actions and editors.
 */
public class CockpitComponentUILifeCycle implements UiLifeCycle
{
    @Override
    public void afterComponentAttached(final Component comp, final Page page)
    {
        // NOOP
    }


    @Override
    public void afterComponentDetached(final Component comp, final Page prevpage)
    {
        removeObserversAndStubs(comp);
    }


    private void removeObserversAndStubs(final Component comp)
    {
        if(comp == null)
        {
            return;
        }
        if(comp instanceof Widgetslot)
        {
            final Object attribute = comp.getAttribute(AbstractCockpitComponent.COCKPIT_COMPONENTS_ATTIBUTE);
            if(attribute instanceof Set<?>)
            {
                final Set<Object> ccomps = (Set<Object>)attribute;
                for(final Object component : ccomps)
                {
                    if(component instanceof AbstractCockpitComponent)
                    {
                        ((AbstractCockpitComponent)component).unregisterWidgetStubInstance();
                    }
                }
            }
            return;
        }
        if(comp instanceof AbstractCockpitComponent)
        {
            ((AbstractCockpitComponent)comp).destroy();
            return;
        }
        for(final Iterator<Component> it = comp.getChildren().iterator(); it.hasNext(); )
        {
            final Component child = it.next();
            if(child != null)
            {
                removeObserversAndStubs(child);
            }
        }
    }


    @Override
    public void afterComponentMoved(final Component parent, final Component child, final Component prevparent)
    {
        // NOOP
    }


    @Override
    public void afterPageAttached(final Page page, final Desktop desktop)
    {
        // NOOP
    }


    @Override
    public void afterPageDetached(final Page page, final Desktop prevdesktop)
    {
        // NOOP
    }


    @Override
    public void afterShadowAttached(ShadowElement shadowElement, Component component)
    {
        // Not implemented
    }


    @Override
    public void afterShadowDetached(ShadowElement shadowElement, Component component)
    {
        // Not implemented
    }
}
