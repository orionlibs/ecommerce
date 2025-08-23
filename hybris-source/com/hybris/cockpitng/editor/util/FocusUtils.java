/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.util;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Focusable;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.util.Clients;

public class FocusUtils
{
    private FocusUtils()
    {
    }


    public static void focusComponent(final Component component, final String property)
    {
        Component theComponent = component;
        if(component instanceof Editor)
        {
            theComponent = ((Editor)component).getDefaultFocusComponent();
        }
        if(theComponent instanceof Focusable)
        {
            ((Focusable)theComponent).focus(property);
        }
        else if(theComponent instanceof HtmlBasedComponent)
        {
            ((HtmlBasedComponent)theComponent).focus();
            Clients.scrollIntoView(component);
        }
    }
}
