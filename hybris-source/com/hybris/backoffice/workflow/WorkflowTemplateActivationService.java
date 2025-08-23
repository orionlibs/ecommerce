/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.cockpitng.dataaccess.context.Context;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service which allows to create workflow based on workflow templates and its activation scripts
 * {@link WorkflowTemplateModel#getActivationScript()}
 */
public interface WorkflowTemplateActivationService
{
    /**
     * Prepares {@link WorkflowTemplateActivationCtx} for every item in given items list. Contexts have to be created
     * before items are saved/created due to information about original values
     * which is removed after saving
     * items.
     *
     * @param items map of items with workflow action.
     * @param invocationCtx context of invocation which will be copied into returned contexts
     * @return list of activation ctx
     */
    List<WorkflowTemplateActivationCtx> prepareWorkflowTemplateActivationContexts(
                    final Map<? extends ItemModel, WorkflowTemplateActivationAction> items, final Context invocationCtx);


    /**
     * For every given activation ctx, seeks {@link de.hybris.platform.workflow.model.WorkflowTemplateModel} with
     * activation script and runs the script which activation ctx. Workflows should be activated after items are
     * saved/created.For more information please read {@link de.hybris.platform.workflow.ScriptEvaluationService}
     *
     * @param activationCtxList list fo activation contexts which should be created before items are saved.
     */
    void activateWorkflowTemplates(final Collection<WorkflowTemplateActivationCtx> activationCtxList);
}
