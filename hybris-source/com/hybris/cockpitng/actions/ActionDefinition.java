/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;

/**
 * Describes a reusable cockpit action. Managed by action registry.
 */
public class ActionDefinition extends AbstractCockpitComponentDefinition
{
    private String iconUri;
    private String iconDisabledUri;
    private String iconHoverUri;
    private String inputType;
    private String outputType;
    private String actionClassName;
    private String customRendererClassName;


    public String getIconDisabledUri()
    {
        return iconDisabledUri;
    }


    public String getIconUri()
    {
        return iconUri;
    }


    public void setIconUri(final String iconUri)
    {
        this.iconUri = iconUri;
    }


    public void setIconDisabledUri(final String iconDisabledUri)
    {
        this.iconDisabledUri = iconDisabledUri;
    }


    public String getIconHoverUri()
    {
        return iconHoverUri;
    }


    public void setIconHoverUri(final String iconHoverUri)
    {
        this.iconHoverUri = iconHoverUri;
    }


    public String getInputType()
    {
        return inputType;
    }


    public void setInputType(final String inputType)
    {
        this.inputType = inputType;
    }


    public String getOutputType()
    {
        return outputType;
    }


    public void setOutputType(final String outputType)
    {
        this.outputType = outputType;
    }


    public String getActionClassName()
    {
        return actionClassName;
    }


    public void setActionClassName(final String actionClassName)
    {
        this.actionClassName = actionClassName;
    }


    public String getCustomRendererClassName()
    {
        return customRendererClassName;
    }


    public void setCustomRendererClassName(final String customRendererClassName)
    {
        this.customRendererClassName = customRendererClassName;
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
        if(!super.equals(o))
        {
            return false;
        }
        final ActionDefinition that = (ActionDefinition)o;
        if(iconUri != null ? !iconUri.equals(that.iconUri) : that.iconUri != null)
        {
            return false;
        }
        if(iconDisabledUri != null ? !iconDisabledUri.equals(that.iconDisabledUri) : that.iconDisabledUri != null)
        {
            return false;
        }
        if(iconHoverUri != null ? !iconHoverUri.equals(that.iconHoverUri) : that.iconHoverUri != null)
        {
            return false;
        }
        if(inputType != null ? !inputType.equals(that.inputType) : that.inputType != null)
        {
            return false;
        }
        if(outputType != null ? !outputType.equals(that.outputType) : that.outputType != null)
        {
            return false;
        }
        if(actionClassName != null ? !actionClassName.equals(that.actionClassName) : that.actionClassName != null)
        {
            return false;
        }
        return customRendererClassName != null ? customRendererClassName.equals(that.customRendererClassName)
                        : that.customRendererClassName == null;
    }


    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (iconUri != null ? iconUri.hashCode() : 0);
        result = 31 * result + (iconDisabledUri != null ? iconDisabledUri.hashCode() : 0);
        result = 31 * result + (iconHoverUri != null ? iconHoverUri.hashCode() : 0);
        result = 31 * result + (inputType != null ? inputType.hashCode() : 0);
        result = 31 * result + (outputType != null ? outputType.hashCode() : 0);
        result = 31 * result + (actionClassName != null ? actionClassName.hashCode() : 0);
        result = 31 * result + (customRendererClassName != null ? customRendererClassName.hashCode() : 0);
        return result;
    }
}
