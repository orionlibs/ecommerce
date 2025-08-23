package de.hybris.platform.warehousingbackoffice.renderers;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class ConsignmentAssigneePanelRenderer extends DefaultEditorAreaPanelRenderer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsignmentAssigneePanelRenderer.class);
    protected static final String CONSIGNMENT = "Consignment";
    protected static final String QUALIFIER = "taskAssignmentWorkflow";
    @Resource
    protected WorkflowService newestWorkflowService;
    @Resource
    protected TypeFacade typeFacade;
    private String assignee;


    public void render(Component component, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(abstractPanelConfiguration instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel && object instanceof ConsignmentModel && ((ConsignmentModel)object)
                        .getTaskAssignmentWorkflow() != null)
        {
            WorkflowModel taskAssignmentWorkflow = getNewestWorkflowService().getWorkflowForCode(((ConsignmentModel)object).getTaskAssignmentWorkflow());
            Optional<WorkflowActionModel> taskInProgress = taskAssignmentWorkflow.getActions().stream().filter(action -> action.getStatus().equals(WorkflowActionStatus.IN_PROGRESS)).findAny();
            if(taskInProgress.isPresent())
            {
                this.assignee = ((WorkflowActionModel)taskInProgress.get()).getPrincipalAssigned().getDisplayName();
                Attribute attribute = new Attribute();
                attribute.setLabel("warehousingbackoffice.consignment.assignee");
                attribute.setQualifier("taskAssignmentWorkflow");
                attribute.setDescription("warehousingbackoffice.taskassignment.consignment.assignee.description");
                try
                {
                    DataType consignment = getTypeFacade().load("Consignment");
                    createAttributeRenderer().render(component, attribute, consignment.getClazz(), consignment, widgetInstanceManager);
                }
                catch(TypeNotFoundException e)
                {
                    if(LOGGER.isWarnEnabled())
                    {
                        LOGGER.warn(e.getMessage());
                    }
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug(e.getMessage(), (Throwable)e);
                    }
                }
            }
        }
    }


    protected Editor createEditor(DataType genericType, WidgetInstanceManager widgetInstanceManager, Attribute attribute, Object object)
    {
        DataAttribute genericAttribute = genericType.getAttribute(attribute.getQualifier());
        if(genericAttribute == null)
        {
            return null;
        }
        String referencedModelProperty = "Consignment.taskAssignmentWorkflow";
        Editor editor = (new EditorBuilder(widgetInstanceManager, genericAttribute, "Consignment.taskAssignmentWorkflow")).setReadOnly(Boolean.TRUE.booleanValue()).setValueType(resolveEditorType(genericAttribute)).setOptional(!genericAttribute.isMandatory()).setValue(this.assignee).build();
        editor.setLocalized(Boolean.FALSE.booleanValue());
        YTestTools.modifyYTestId((Component)editor, "editor_Consignment.taskAssignmentWorkflow");
        editor.setAttribute("parentObject", object);
        editor.setWritableLocales(getPermissionFacade().getWritableLocalesForInstance(object));
        editor.setReadableLocales(getPermissionFacade().getReadableLocalesForInstance(object));
        editor.setProperty("Consignment.taskAssignmentWorkflow");
        if(StringUtils.isNotBlank(attribute.getEditor()))
        {
            editor.setDefaultEditor(attribute.getEditor());
        }
        editor.setPartOf(genericAttribute.isPartOf());
        editor.setOrdered(genericAttribute.isOrdered());
        editor.afterCompose();
        editor.setSclass("ye-default-editor-readonly");
        return editor;
    }


    protected WorkflowService getNewestWorkflowService()
    {
        return this.newestWorkflowService;
    }


    protected TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }
}
