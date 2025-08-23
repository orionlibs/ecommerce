/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.dto;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

public enum NodeType
{
    ACTION(WorkflowActionTemplateModel._TYPECODE), DECISION(WorkflowDecisionTemplateModel._TYPECODE), AND("AND");
    private final String typeCode;


    NodeType(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return typeCode;
    }
}
