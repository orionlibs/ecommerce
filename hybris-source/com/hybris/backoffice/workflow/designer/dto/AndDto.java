/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.dto;

import de.hybris.platform.core.model.link.LinkModel;

/**
 * Represents the 'and' node modelled in Workflow Designer
 */
public class AndDto extends ElementDto<LinkModel>
{
    public AndDto(final Operation operation, final LinkModel model)
    {
        AndDto.super.setOperation(operation);
        AndDto.super.setModel(model);
    }
}
