/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar.impl;

import com.hybris.backoffice.actionbar.ActionComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Menuitem;

/**
 * {@link com.hybris.backoffice.actionbar.ActionDefinition} represented by {@link Menuitem} component
 */
public class MenuitemActionComponent implements ActionComponent
{
    private final Menuitem menuitem;


    public MenuitemActionComponent(final Menuitem menuitem)
    {
        this.menuitem = menuitem;
    }


    @Override
    public void setLabel(final String label)
    {
        menuitem.setLabel(label);
    }


    @Override
    public String getLabel()
    {
        return menuitem.getLabel();
    }


    @Override
    public String getImage()
    {
        return menuitem.getImage();
    }


    @Override
    public void setImage(final String image)
    {
        menuitem.setImage(image);
    }


    @Override
    public String getTooltiptext()
    {
        return menuitem.getTooltiptext();
    }


    @Override
    public void setTooltiptext(final String tooltiptext)
    {
        menuitem.setTooltiptext(tooltiptext);
    }


    @Override
    public Object getAttribute(final String name)
    {
        return menuitem.getAttribute(name);
    }


    @Override
    public boolean hasAttribute(final String name)
    {
        return menuitem.hasAttribute(name);
    }


    @Override
    public Object setAttribute(final String name, final Object value)
    {
        return menuitem.setAttribute(name, value);
    }


    @Override
    public Object removeAttribute(final String name)
    {
        return menuitem.removeAttribute(name);
    }


    @Override
    public Component getComponent()
    {
        return menuitem;
    }
}
