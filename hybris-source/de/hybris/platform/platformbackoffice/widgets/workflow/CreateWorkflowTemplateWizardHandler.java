package de.hybris.platform.platformbackoffice.widgets.workflow;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class CreateWorkflowTemplateWizardHandler implements FlowActionHandler
{
    static final String WORKFLOW_KEY = "workflowTemplate";
    static final String SOCKET_OUT_TEMPLATE = "workflowDesignerCreateTemplate";
    private ModelService modelService;
    private NotificationStack notificationStack;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        Optional<WorkflowTemplateModel> workflowTemplate = getWorkflowTemplate(adapter);
        workflowTemplate.ifPresent(template -> {
            getModelService().save(template);
            removeConfigurableFlowIdFromNotificationStack(adapter);
            adapter.getWidgetInstanceManager().sendOutput("workflowDesignerCreateTemplate", template);
        });
        adapter.done();
    }


    protected void removeConfigurableFlowIdFromNotificationStack(FlowActionHandlerAdapter adapter)
    {
        getNotificationStack().onTemplateClosed(adapter.getWidgetInstanceManager().getWidgetslot().getWidgetInstance());
    }


    private Optional<WorkflowTemplateModel> getWorkflowTemplate(FlowActionHandlerAdapter adapter)
    {
        WidgetModel widgetModel = adapter.getWidgetInstanceManager().getModel();
        return Optional.ofNullable((WorkflowTemplateModel)widgetModel.getValue("workflowTemplate", WorkflowTemplateModel.class));
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public NotificationStack getNotificationStack()
    {
        return this.notificationStack;
    }


    @Required
    public void setNotificationStack(NotificationStack notificationStack)
    {
        this.notificationStack = notificationStack;
    }
}
