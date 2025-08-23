package de.hybris.platform.platformbackoffice.renderers;

import com.google.common.collect.Lists;
import com.hybris.backoffice.workflow.renderer.AbstractWorkflowActionDecisionRenderer;
import com.hybris.cockpitng.common.configuration.EditorConfigurationUtil;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomSection;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelUtils;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Vlayout;

public class WorkflowDecisionActionSectionRenderer extends AbstractWorkflowActionDecisionRenderer<Component, CustomSection, Object>
{
    protected static final String SCLASS_CELL_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label";
    protected static final String SELECTED_DECISION_QUALIFIER = "selectedDecision";
    protected static final String MODEL_SELECTED_WFL_DECISION = "__selectedWflDecision";
    protected static final String MODEL_VALUE_CHANGED = "valueChanged";
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowDecisionActionSectionRenderer.class);
    private static final String WFL_DECISION_AFTER_OPERATION = "wfl_decision_after_operation";
    @Deprecated(since = "6.5", forRemoval = true)
    protected WorkflowProcessingService workflowProcessingService;
    protected ObjectPreviewService objectPreviewService;
    protected ObjectFacade objectFacade;
    protected ModelService modelService;
    protected PermissionFacade permissionFacade;


    public void render(Component parent, CustomSection configuration, Object value, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        renderSectionInternal(widgetInstanceManager, (WorkflowActionModel)value, parent, dataType);
        fireComponentRendered(parent, configuration, value);
    }


    protected void renderSectionInternal(WidgetInstanceManager widgetInstanceManager, WorkflowActionModel action, Component parent, DataType dataType)
    {
        Div labelCnt = new Div();
        UITools.modifySClass((HtmlBasedComponent)labelCnt, "yw-editorarea-label-container", true);
        Label label = new Label(resolveAttributeLabel("selectedDecision", dataType.getCode()));
        UITools.modifySClass((HtmlBasedComponent)label, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label", true);
        label.setTooltiptext("selectedDecision");
        labelCnt.appendChild((Component)label);
        Vlayout vlayout = new Vlayout();
        vlayout.setSclass("yw-workflowaction-decision-section");
        vlayout.appendChild((Component)labelCnt);
        Combobox decisionCombobox = createSlectedDecisionCombobox(widgetInstanceManager, action, (Component)vlayout, dataType);
        Hlayout hlayout = new Hlayout();
        hlayout.appendChild((Component)decisionCombobox);
        Button makeDecisionButton = createActionButton(decisionCombobox, action, widgetInstanceManager);
        hlayout.appendChild((Component)makeDecisionButton);
        vlayout.appendChild((Component)hlayout);
        addOnValueChangeListener(widgetInstanceManager, decisionCombobox, makeDecisionButton);
        if(!hasPermissionToMakeDecision(action))
        {
            decisionCombobox.setDisabled(true);
            makeDecisionButton.setDisabled(true);
        }
        parent.appendChild((Component)vlayout);
    }


    protected boolean hasPermissionToMakeDecision(WorkflowActionModel action)
    {
        return (hasPermissions(action) && !getPermittedDecisions(action).isEmpty());
    }


    protected boolean hasPermissions(WorkflowActionModel action)
    {
        return (this.permissionFacade.canReadInstance(action) && this.permissionFacade.canChangeInstance(action));
    }


    protected List<WorkflowDecisionModel> getPermittedDecisions(WorkflowActionModel action)
    {
        return (List<WorkflowDecisionModel>)action.getDecisions().stream().filter(this::hasPermissions).collect(Collectors.toList());
    }


    private boolean hasPermissions(WorkflowDecisionModel decision)
    {
        return this.permissionFacade.canReadInstance(decision);
    }


    protected Base loadBaseConfiguration(WidgetInstanceManager widgetInstanceManager, String typeCode)
    {
        Base config = EditorConfigurationUtil.getBaseConfiguration(widgetInstanceManager, typeCode);
        if(config == null)
        {
            LOG.warn("Loaded UI configuration is null. Ignoring.");
        }
        return config;
    }


    protected Button createActionButton(Combobox combobox, WorkflowActionModel workflowActionModel, WidgetInstanceManager widgetInstanceManager)
    {
        Button button = new Button();
        button.setSclass("y-workflow-apply");
        button.setLabel(Labels.getLabel("workflow.decision.apply"));
        button.addEventListener("onClick", event -> {
            Comboitem selected = combobox.getSelectedItem();
            if(selected != null)
            {
                WorkflowDecisionModel selectedDecision = (WorkflowDecisionModel)selected.getValue();
                performAction(workflowActionModel, selectedDecision, widgetInstanceManager);
                refreshWidgetModelAfterSaveListeners(workflowActionModel, widgetInstanceManager);
            }
        });
        return button;
    }


    protected void refreshWidgetModelAfterSaveListeners(WorkflowActionModel actionModel, WidgetInstanceManager widgetInstanceManager)
    {
        try
        {
            WorkflowActionModel freshWorkflowActionModel = (WorkflowActionModel)this.objectFacade.reload(actionModel);
            widgetInstanceManager.getModel().setValue("currentObject", freshWorkflowActionModel);
            widgetInstanceManager.getModel().setValue("valueChanged", Boolean.FALSE);
        }
        catch(ObjectNotFoundException notFoundException)
        {
            LOG.error("Workflow action model not found on reload", (Throwable)notFoundException);
        }
    }


    protected void performAction(WorkflowActionModel workflowActionModel, WorkflowDecisionModel selectedDecision, WidgetInstanceManager wim)
    {
        if(!((Boolean)wim.getModel().getValue("valueChanged", Boolean.class)).booleanValue())
        {
            makeDecision(workflowActionModel, selectedDecision, wim);
        }
    }


    protected void addOnValueChangeListener(WidgetInstanceManager widgetInstanceManager, Combobox decisionCombobox, Button makeDecisionButton)
    {
        widgetInstanceManager.getModel().addObserver("valueChanged", () -> {
            boolean modelValueChanged = ((Boolean)widgetInstanceManager.getModel().getValue("valueChanged", Boolean.class)).booleanValue();
            updateSection(modelValueChanged, decisionCombobox, makeDecisionButton);
        });
    }


    protected void updateSection(boolean modelValueChanged, Combobox decisionCombobox, Button makeDecisionButton)
    {
        decisionCombobox.setDisabled(modelValueChanged);
        makeDecisionButton.setDisabled(modelValueChanged);
    }


    protected Combobox createSlectedDecisionCombobox(WidgetInstanceManager widgetInstanceManager, WorkflowActionModel action, Component component, DataType dataType)
    {
        Combobox combobox = new Combobox();
        combobox.setSclass("yw-workflowaction-selected-decision");
        Base config = loadBaseConfiguration(widgetInstanceManager, dataType.getCode());
        ListModelList<WorkflowDecisionModel> model = new ListModelList(getWorkflowDecisions(action));
        model.setSelection(Collections.singletonList(action.getSelectedDecision()));
        widgetInstanceManager.getModel().setValue("__selectedWflDecision", action.getSelectedDecision());
        combobox.setModel((ListModel)model);
        combobox.setItemRenderer((comboitem, decision, index) -> renderSingleComboitem(comboitem, decision, config));
        combobox.addEventListener("onSelect",
                        createSelectedDecisionListener(widgetInstanceManager, action, component, dataType));
        return combobox;
    }


    protected void renderSingleComboitem(Comboitem comboitem, Object decision, Base config)
    {
        comboitem.setLabel(getDecisionLabel((WorkflowDecisionModel)decision));
        ObjectPreview preview = this.objectPreviewService.getPreview(decision, config);
        if(preview != null)
        {
            comboitem.setImage(preview.getUrl());
        }
        comboitem.setValue(decision);
    }


    protected EventListener createSelectedDecisionListener(WidgetInstanceManager widgetInstanceManager, WorkflowActionModel actionModel, Component parent, DataType dataType)
    {
        return event -> {
            if(CollectionUtils.isNotEmpty(event.getSelectedObjects()))
            {
                WorkflowDecisionModel selectedDecision = event.getSelectedObjects().iterator().next();
                actionModel.setSelectedDecision(selectedDecision);
                widgetInstanceManager.getModel().setValue("__selectedWflDecision", selectedDecision);
                EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager.getModel(), "wfl_decision_after_operation", (), false);
                EditorAreaRendererUtils.setAfterSaveListener(widgetInstanceManager.getModel(), "wfl_decision_after_operation", (), false);
            }
        };
    }


    protected void refreshAfterCancel(WidgetInstanceManager widgetInstanceManager, WorkflowActionModel actionModel, Component parent, DataType dataType)
    {
        this.modelService.refresh(actionModel);
        parent.getChildren().clear();
        renderSectionInternal(widgetInstanceManager, actionModel, parent, dataType);
    }


    protected void refreshAfterSave(WidgetInstanceManager widgetInstanceManager, WorkflowActionModel actionModel)
    {
        WorkflowDecisionModel selected = (WorkflowDecisionModel)widgetInstanceManager.getModel().getValue("__selectedWflDecision", WorkflowDecisionModel.class);
        actionModel.setSelectedDecision(selected);
        this.modelService.save(actionModel);
    }


    private List<WorkflowDecisionModel> getWorkflowDecisions(WorkflowActionModel action)
    {
        return Lists.newArrayList(action.getDecisions());
    }


    private String resolveAttributeLabel(String attributeName, String typeCode)
    {
        String label = getLabelService().getObjectLabel(ObjectValuePath.getPath(new String[] {typeCode, attributeName}));
        if(StringUtils.isBlank(label))
        {
            label = LabelUtils.getFallbackLabel(attributeName);
        }
        return label;
    }


    @Required
    public void setObjectPreviewService(ObjectPreviewService objectPreviewService)
    {
        this.objectPreviewService = objectPreviewService;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Deprecated(since = "6.5", forRemoval = true)
    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
    }
}
