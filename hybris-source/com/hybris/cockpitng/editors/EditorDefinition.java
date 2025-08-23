/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;

/**
 * Describes a reusable cockpit editor. Managed by editor registry.
 */
public class EditorDefinition extends AbstractCockpitComponentDefinition
{
    private String type;
    private String editorClassName;
    private String viewSrc;
    private Boolean handleLocalization;


    public String getType()
    {
        return type;
    }


    public void setType(final String type)
    {
        this.type = type;
    }


    public String getEditorClassName()
    {
        return editorClassName;
    }


    public void setEditorClassName(final String editorClassName)
    {
        this.editorClassName = editorClassName;
    }


    public void setViewSrc(final String viewSrc)
    {
        this.viewSrc = viewSrc;
    }


    public String getViewSrc()
    {
        return viewSrc;
    }


    public Boolean getHandlesLocalization()
    {
        return handleLocalization;
    }


    public void setHandlesLocalization(final Boolean handlesLocalization)
    {
        this.handleLocalization = handlesLocalization;
    }
}
