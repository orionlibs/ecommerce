/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import java.io.Serializable;

/**
 * Representation of a socket connection between two widgets.
 */
public class WidgetConnection implements Serializable
{
    private static final long serialVersionUID = -1104487017505105808L;
    private String name;
    private final String inputId;
    private final String outputId;
    private final Widget source;
    private final Widget target;


    public WidgetConnection(final Widget source, final Widget target, final String inputId, final String outputId)
    {
        this.source = source;
        this.target = target;
        this.inputId = inputId;
        this.outputId = outputId;
    }


    public String getName()
    {
        return name;
    }


    public void setName(final String name)
    {
        this.name = name;
    }


    public String getInputId()
    {
        return inputId;
    }


    public String getOutputId()
    {
        return outputId;
    }


    public Widget getSource()
    {
        return source;
    }


    public Widget getTarget()
    {
        return target;
    }


    @Override
    public String toString()
    {
        return source + ": " + outputId + " -> " + target + ": " + inputId;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final WidgetConnection that = (WidgetConnection)o;
        if(name != null ? !name.equals(that.name) : that.name != null)
        {
            return false;
        }
        if(inputId != null ? !inputId.equals(that.inputId) : that.inputId != null)
        {
            return false;
        }
        if(outputId != null ? !outputId.equals(that.outputId) : that.outputId != null)
        {
            return false;
        }
        if(source != null ? !source.equals(that.source) : that.source != null)
        {
            return false;
        }
        return target != null ? target.equals(that.target) : that.target == null;
    }


    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (inputId != null ? inputId.hashCode() : 0);
        result = 31 * result + (outputId != null ? outputId.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }
}
