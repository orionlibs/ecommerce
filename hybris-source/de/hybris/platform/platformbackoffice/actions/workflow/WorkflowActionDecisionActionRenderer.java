package de.hybris.platform.platformbackoffice.actions.workflow;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.actions.AbstractStatefulActionRenderer;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.components.Action;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.WidgetUtils;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

@Deprecated(since = "6.6", forRemoval = true)
public class WorkflowActionDecisionActionRenderer extends AbstractStatefulActionRenderer<WorkflowActionModel, WorkflowDecisionModel>
{
    private static final String CONFIRM_LABEL = "workflowactiondecisionaction.confirm";
    private static final String CHOOSE_ACTION_LABEL = "workflowactiondecisionaction.chooseaction";
    private static final String SCLASS_WORKFLOW_ACTION_SELECTED_DECISION = "yw-workflowaction-selected-decision";
    private static final String SCLASS_WORKFLOW_ACTION_DECISION_CONTAINER = "yw-workflowaction-decision-container";
    private LabelService labelService;
    private ObjectFacade objectFacade;
    private WidgetUtils widgetUtils;
    private PermissionFacade permissionFacade;


    public void render(Component parent, CockpitAction<WorkflowActionModel, WorkflowDecisionModel> action, ActionContext<WorkflowActionModel> context, boolean updateMode, ActionListener<WorkflowDecisionModel> listener)
    {
        if(!shouldRenderAction(context))
        {
            return;
        }
        WidgetInstanceManager wim = ((Action)parent).getWidgetInstanceManager();
        context.setParameter("wim", wim);
        WorkflowActionModel workflowAction = (WorkflowActionModel)context.getData();
        Button confirmDecisionButton = createConfirmDecisionButton(context, action, listener);
        Combobox decisionCombobox = createDecisionCombobox(context, confirmDecisionButton);
        Hlayout container = new Hlayout();
        container.appendChild((Component)decisionCombobox);
        container.appendChild((Component)confirmDecisionButton);
        container.setSclass("yw-workflowaction-decision-container");
        parent.appendChild((Component)container);
        registerWorkflowActionUpdateListener(workflowAction, container, parent);
    }


    protected boolean shouldRenderAction(ActionContext<WorkflowActionModel> context)
    {
        if(context.getData() == null)
        {
            return false;
        }
        WorkflowActionModel workflowAction = (WorkflowActionModel)context.getData();
        return (workflowAction.getStatus() != null && workflowAction.getStatus() == WorkflowActionStatus.IN_PROGRESS &&
                        hasPermissionToMakeDecision(workflowAction));
    }


    protected boolean hasPermissionToMakeDecision(WorkflowActionModel action)
    {
        return (hasPermissions(action) && !getPermittedDecisions(action).isEmpty());
    }


    protected boolean hasPermissions(WorkflowActionModel action)
    {
        return (getPermissionFacade().canReadType("WorkflowAction") && getPermissionFacade().canReadInstance(action) &&
                        getPermissionFacade().canChangeType("WorkflowAction") &&
                        getPermissionFacade().canChangeInstance(action));
    }


    protected boolean hasPermissions(WorkflowDecisionModel decision)
    {
        return (getPermissionFacade().canReadType("WorkflowDecision") &&
                        getPermissionFacade().canReadInstance(decision));
    }


    protected List<WorkflowDecisionModel> getPermittedDecisions(WorkflowActionModel action)
    {
        return (List<WorkflowDecisionModel>)action.getDecisions().stream().filter(this::hasPermissions).collect(Collectors.toList());
    }


    protected Button createConfirmDecisionButton(ActionContext<WorkflowActionModel> context, CockpitAction<WorkflowActionModel, WorkflowDecisionModel> action, ActionListener<WorkflowDecisionModel> listener)
    {
        Button confirmDecisionButton = new Button(context.getLabel("workflowactiondecisionaction.confirm"));
        confirmDecisionButton.addEventListener("onClick", event -> {
            confirmDecisionButton.setDisabled(true);
            perform(action, context, wrapActionListener(listener, confirmDecisionButton));
        });
        return confirmDecisionButton;
    }


    protected ActionListener<WorkflowDecisionModel> wrapActionListener(ActionListener<WorkflowDecisionModel> listener, Button confirmButton)
    {
        return result -> {
            if("error".equals(result.getResultCode()))
            {
                confirmButton.setDisabled(false);
            }
            listener.actionPerformed(result);
        };
    }


    protected Combobox createDecisionCombobox(ActionContext<WorkflowActionModel> context, Button confirmButton)
    {
        Combobox combobox = new Combobox();
        combobox.setItemRenderer((comboitem, decision, index) -> renderSingleComboitem(comboitem, decision));
        combobox.setSclass("yw-workflowaction-selected-decision");
        combobox.setReadonly(true);
        combobox.setPlaceholder(context.getLabel("workflowactiondecisionaction.chooseaction"));
        WorkflowActionModel workflowAction = (WorkflowActionModel)context.getData();
        ListModelList<WorkflowDecisionModel> model = new ListModelList(Lists.newArrayList(getPermittedDecisions(workflowAction)));
        combobox.setModel((ListModel)model);
        setInitialComboboxValue(combobox, context);
        combobox.addEventListener("onChange", event -> {
            Object selectedValue = combobox.getSelectedItem().getValue();
            setValue(context, "selectedDecision", selectedValue);
            confirmButton.setDisabled(shouldDisableConfirmDecisionButton(combobox, workflowAction.getStatus()));
        });
        confirmButton.setDisabled(shouldDisableConfirmDecisionButton(combobox, workflowAction.getStatus()));
        return combobox;
    }


    protected boolean shouldDisableConfirmDecisionButton(Combobox combobox, WorkflowActionStatus workflowActionStatus)
    {
        return (combobox.getSelectedItem() == null || workflowActionStatus != WorkflowActionStatus.IN_PROGRESS);
    }


    protected void renderSingleComboitem(Comboitem comboitem, Object decision)
    {
        comboitem.setLabel(getLabelService().getObjectLabel(decision));
        comboitem.setValue(decision);
    }


    protected void setInitialComboboxValue(Combobox combobox, ActionContext<WorkflowActionModel> context)
    {
        Optional<Comboitem> comboitemToSelect = combobox.getItems().stream().filter(p -> p.getValue().equals(getValue(context, "selectedDecision"))).findFirst();
        if(comboitemToSelect.isPresent())
        {
            combobox.setSelectedItem(comboitemToSelect.get());
        }
        else
        {
            combobox.setSelectedItem(null);
        }
    }


    protected void registerWorkflowActionUpdateListener(WorkflowActionModel workflowAction, Hlayout container, Component parent)
    {
        WidgetInstanceManager widgetInstanceManager = ((Action)parent).getWidgetInstanceManager();
        getWidgetUtils().addGlobalEventListener("objectsUpdated", widgetInstanceManager.getWidgetslot(), event -> {
            CockpitEvent data = (CockpitEvent)event.getData();
            boolean actionMatches = data.getDataAsCollection().stream().anyMatch(());
            if(actionMatches)
            {
                WorkflowActionModel updatedWorkflowAction = (WorkflowActionModel)getObjectFacade().reload(workflowAction);
                widgetInstanceManager.getModel().setValue("$_selectedAction", updatedWorkflowAction);
                if(updatedWorkflowAction.getStatus() != WorkflowActionStatus.IN_PROGRESS)
                {
                    parent.removeChild((Component)container);
                }
            }
        }"session");
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    @Autowired
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    @Autowired
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    protected WidgetUtils getWidgetUtils()
    {
        return this.widgetUtils;
    }


    @Autowired
    public void setWidgetUtils(WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return this.permissionFacade;
    }


    @Autowired
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
