/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;

public class WorkflowDeleteActionRenderer implements WidgetComponentRenderer<Menupopup, ListColumn, WorkflowModel>
{
    protected static final String LABEL_WORKFLOWS_ACTION_DELETE = "workflow.action.delete";
    protected static final String LABEL_WORKFLOWS_ACTION_CONFIRMATION_MESSAGE_DELETE = "workflow.action.confirmation.message.delete";
    protected static final String LABEL_WORKFLOWS_ACTION_CONFIRMATION_TITLE_DELETE = "workflow.action.confirmation.title.delete";
    private static final String YTESTID_WORKFLOWS_ACTION_DELETE = "com_hybris_backoffice_widgets_workflows_actions_delete";
    protected static final String NO_ICON = " ";
    private Function<WorkflowModel, Boolean> workflowDeleteActionExecutor;
    private Predicate<WorkflowModel> actionPredicate;
    private LabelService labelService;
    private boolean needsConfirmation = true;


    @Override
    public void render(final Menupopup menupopup, final ListColumn listColumn, final WorkflowModel workflowModel,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        if(actionPredicate.negate().test(workflowModel))
        {
            return;
        }
        final Menuitem menuitem = new Menuitem();
        menuitem.setIconSclass(NO_ICON);
        menuitem.setLabel(Labels.getLabel(LABEL_WORKFLOWS_ACTION_DELETE));
        YTestTools.modifyYTestId(menuitem, YTESTID_WORKFLOWS_ACTION_DELETE);
        menuitem.addEventListener(Events.ON_CLICK, e -> messageBoxPerform(workflowModel));
        menupopup.appendChild(menuitem);
    }


    protected void messageBoxPerform(final WorkflowModel workflowModel)
    {
        if(isNeedsConfirmation())
        {
            Messagebox.show(Labels.getLabel(LABEL_WORKFLOWS_ACTION_CONFIRMATION_MESSAGE_DELETE, new String[]
                                            {getLabelService().getObjectLabel(workflowModel)}), Labels.getLabel(LABEL_WORKFLOWS_ACTION_CONFIRMATION_TITLE_DELETE),
                            new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.CANCEL}, null, Messagebox.QUESTION, null,
                            clickEvent -> {
                                if(Messagebox.Button.YES.equals(clickEvent.getButton()))
                                {
                                    perform(workflowModel);
                                }
                            });
        }
        else
        {
            perform(workflowModel);
        }
    }


    protected void perform(final WorkflowModel workflowModel)
    {
        workflowDeleteActionExecutor.apply(workflowModel);
    }


    public Function<WorkflowModel, Boolean> getWorkflowDeleteActionExecutor()
    {
        return workflowDeleteActionExecutor;
    }


    @Required
    public void setWorkflowDeleteActionExecutor(final Function<WorkflowModel, Boolean> workflowDeleteActionExecutor)
    {
        this.workflowDeleteActionExecutor = workflowDeleteActionExecutor;
    }


    public Predicate<WorkflowModel> getActionPredicate()
    {
        return actionPredicate;
    }


    @Required
    public void setActionPredicate(final Predicate<WorkflowModel> actionPredicate)
    {
        this.actionPredicate = actionPredicate;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public boolean isNeedsConfirmation()
    {
        return needsConfirmation;
    }


    public void setNeedsConfirmation(final boolean needsConfirmation)
    {
        this.needsConfirmation = needsConfirmation;
    }
}
