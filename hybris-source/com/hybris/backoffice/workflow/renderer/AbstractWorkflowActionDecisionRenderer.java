/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer;

import com.hybris.backoffice.workflow.impl.DefaultWorkflowDecisionMaker;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractWorkflowActionDecisionRenderer<PARENT, CONFIG, DATA>
                extends AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA>
{
    private LabelService labelService;
    private DefaultWorkflowDecisionMaker workflowDecisionMaker;


    protected void makeDecision(final WorkflowActionModel workflowAction, final WorkflowDecisionModel selectedDecision,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        getWorkflowDecisionMaker().makeDecision(workflowAction, selectedDecision, widgetInstanceManager);
    }


    protected String getDecisionLabel(final WorkflowDecisionModel workflowDecision)
    {
        return getLabelService().getObjectLabel(workflowDecision);
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected DefaultWorkflowDecisionMaker getWorkflowDecisionMaker()
    {
        return workflowDecisionMaker;
    }


    @Required
    public void setWorkflowDecisionMaker(final DefaultWorkflowDecisionMaker workflowDecisionMaker)
    {
        this.workflowDecisionMaker = workflowDecisionMaker;
    }
}
