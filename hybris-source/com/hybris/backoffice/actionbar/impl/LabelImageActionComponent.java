/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar.impl;

import com.hybris.backoffice.actionbar.ActionComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.impl.LabelImageElement;

/**
 * {@link com.hybris.backoffice.actionbar.ActionDefinition} represented by {@link LabelImageElement} component
 */
public class LabelImageActionComponent implements ActionComponent
{
    private final LabelImageElement labelImage;


    public LabelImageActionComponent(final LabelImageElement labelImage)
    {
        this.labelImage = labelImage;
    }


    @Override
    public void setLabel(final String label)
    {
        labelImage.setLabel(label);
    }


    @Override
    public String getLabel()
    {
        return labelImage.getLabel();
    }


    @Override
    public String getImage()
    {
        return labelImage.getImage();
    }


    @Override
    public void setImage(final String image)
    {
        labelImage.setImage(image);
    }


    @Override
    public String getTooltiptext()
    {
        return labelImage.getTooltiptext();
    }


    @Override
    public void setTooltiptext(final String tooltiptext)
    {
        labelImage.setTooltiptext(tooltiptext);
    }


    @Override
    public Object getAttribute(final String name)
    {
        return labelImage.getAttribute(name);
    }


    @Override
    public boolean hasAttribute(final String name)
    {
        return labelImage.hasAttribute(name);
    }


    @Override
    public Object setAttribute(final String name, final Object value)
    {
        return labelImage.setAttribute(name, value);
    }


    @Override
    public Object removeAttribute(final String name)
    {
        return labelImage.removeAttribute(name);
    }


    @Override
    public Component getComponent()
    {
        return labelImage;
    }
}
