/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection;

/**
 * List of all available node groups used by WorkflowDesigner feature
 */
public enum WorkflowDesignerGroup
{
    START_ACTION("startAction"), ACTION("action"), END_ACTION("endAction"), DECISION("decision"), AND("andConnection"), UNKNOWN(
                "unknown");
    private String value;


    WorkflowDesignerGroup(final String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return value;
    }
}
