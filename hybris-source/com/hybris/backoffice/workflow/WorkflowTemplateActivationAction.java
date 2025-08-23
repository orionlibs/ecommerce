/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import de.hybris.platform.workflow.model.WorkflowTemplateModel;

/**
 * Enum representation of
 * {@link de.hybris.platform.workflow.constants.WorkflowConstants.WorkflowActivationScriptActions}. It is used to
 * determine type of action for {@link WorkflowTemplateModel#getActivationScript()} evaluation.
 */
public enum WorkflowTemplateActivationAction
{
    CREATE, SAVE, REMOVE
}
