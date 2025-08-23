/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.form;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Common class for Workflow Designer Wizard Forms
 */
public abstract class AbstractWorkflowTemplateCreateForm
{
    private String code;
    private Map<Locale, String> name = new HashMap<>();


    public String getCode()
    {
        return code;
    }


    public void setCode(final String code)
    {
        this.code = code;
    }


    public Map<Locale, String> getName()
    {
        return name;
    }


    public void setName(final Map<Locale, String> name)
    {
        this.name = name;
    }
}
