/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

/**
 * Names of parameters used for EditorArea attributes configuration
 */
public enum ConfigurableFlowParameterNames
{
    /**
     * Number of rows in text editor
     */
    MULTILINE_EDITOR_ROWS("multilineEditorRows"),
    /**
     * Number of rows in text editor
     *
     * 	Since ages see com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowParameterNames#MULTILINE_EDITOR_ROWS
     */
    @Deprecated(since = "ages", forRemoval = true)
    ROWS("rows");
    private String name;


    ConfigurableFlowParameterNames(final String name)
    {
        this.name = name;
    }


    /**
     * Internal name
     *
     * @return internal name
     */
    public String getName()
    {
        return name;
    }
}
