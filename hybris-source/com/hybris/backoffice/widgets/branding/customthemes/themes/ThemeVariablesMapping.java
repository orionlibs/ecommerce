/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.themes;

import java.util.List;

public class ThemeVariablesMapping
{
    private String id;
    private List<String> variables;
    private String labelKey;


    public String getId()
    {
        return id;
    }


    public void setId(final String id)
    {
        this.id = id;
    }


    public List<String> getVariables()
    {
        return variables;
    }


    public void setVariables(final List<String> variables)
    {
        this.variables = variables;
    }


    public String getLabelKey()
    {
        return labelKey;
    }


    public void setLabelKey(final String labelKey)
    {
        this.labelKey = labelKey;
    }
}
