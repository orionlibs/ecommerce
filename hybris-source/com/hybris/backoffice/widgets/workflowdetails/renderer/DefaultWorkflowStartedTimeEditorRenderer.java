/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.workflowdetails.renderer;

import com.hybris.backoffice.workflow.WorkflowFacade;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Date;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

public class DefaultWorkflowStartedTimeEditorRenderer extends AbstractCockpitEditorRenderer<Date>
{
    private WorkflowFacade workflowFacade;


    @Override
    public void render(final Component parent, final EditorContext<Date> context, final EditorListener<Date> listener)
    {
        final Editor ancestorEditor = findAncestorEditor(parent);
        final WidgetModel widgetModel = ancestorEditor.getWidgetInstanceManager().getModel();
        if(widgetModel != null)
        {
            final WorkflowModel workflow = widgetModel.getValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT,
                            WorkflowModel.class);
            final Date startDate = workflowFacade.getWorkflowStartTime(workflow);
            if(startDate != null)
            {
                ancestorEditor.setInitialValue(startDate);
            }
        }
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return workflowFacade;
    }


    @Required
    public void setWorkflowFacade(final WorkflowFacade workflowFacade)
    {
        this.workflowFacade = workflowFacade;
    }
}
