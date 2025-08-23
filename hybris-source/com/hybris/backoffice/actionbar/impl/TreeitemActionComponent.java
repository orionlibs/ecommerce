/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar.impl;

import com.hybris.backoffice.actionbar.ActionComponent;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

/**
 * {@link com.hybris.backoffice.actionbar.ActionDefinition} represented by {@link Treeitem} component
 */
public class TreeitemActionComponent implements ActionComponent
{
    private final Treecell label;
    private final Treeitem item;


    public TreeitemActionComponent(final Treeitem item)
    {
        this.item = item;
        if(item.getTreerow() == null)
        {
            item.getChildren().add(new Treerow());
        }
        final List<Component> children = item.getTreerow().getChildren();
        if(!children.isEmpty())
        {
            label = (Treecell)children.get(0);
        }
        else
        {
            label = new Treecell();
        }
        children.add(label);
    }


    public Treecell getLabelCell()
    {
        return label;
    }


    @Override
    public void setLabel(final String label)
    {
        this.label.setLabel(label);
    }


    @Override
    public String getLabel()
    {
        return label.getLabel();
    }


    @Override
    public String getImage()
    {
        return label.getImage();
    }


    @Override
    public void setImage(final String image)
    {
        this.label.setImage(image);
    }


    @Override
    public String getTooltiptext()
    {
        return item.getTooltiptext();
    }


    @Override
    public void setTooltiptext(final String tooltiptext)
    {
        item.setTooltiptext(tooltiptext);
    }


    @Override
    public Object getAttribute(final String name)
    {
        return item.getAttribute(name);
    }


    @Override
    public boolean hasAttribute(final String name)
    {
        return item.hasAttribute(name);
    }


    @Override
    public Object setAttribute(final String name, final Object value)
    {
        return item.setAttribute(name, value);
    }


    @Override
    public Object removeAttribute(final String name)
    {
        return item.removeAttribute(name);
    }


    @Override
    public HtmlBasedComponent getComponent()
    {
        return item.getTreerow();
    }
}
