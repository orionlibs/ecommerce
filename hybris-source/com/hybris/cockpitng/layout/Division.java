/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.layout;

import java.util.Collection;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class Division
{
    private final int width;
    private final int height;
    private final boolean successful;
    private final Collection<HtmlBasedComponent> draggableElements;


    public Division(final int width, final int height, final boolean successful,
                    final Collection<HtmlBasedComponent> draggableElements)
    {
        this.width = width;
        this.height = height;
        this.successful = successful;
        this.draggableElements = draggableElements;
    }


    public int getWidth()
    {
        return width;
    }


    public int getHeight()
    {
        return height;
    }


    public boolean isSuccessful()
    {
        return successful;
    }


    public Collection<HtmlBasedComponent> getDraggableElements()
    {
        return draggableElements;
    }
}
