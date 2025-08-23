package de.hybris.platform.processengine.definition;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.definition.xml.UserGroupType;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.helpers.WorkflowIntegrationService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Date;
import java.util.List;

public class NotifyUserGroupNode extends AbstractNode
{
    private final WaitNode nextVirtualWait;
    private final List<UserGroupType> userGroup;
    private final WorkflowIntegrationService workflowService;
    private final ModelService modelService;
    private final ProcessParameterHelper processParameterHelper;


    public NotifyUserGroupNode(String nodeId, List<UserGroupType> userGroup, WaitNode nextVirtualWait)
    {
        super(nodeId);
        this.nextVirtualWait = nextVirtualWait;
        this.userGroup = userGroup;
        this.modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        this
                        .workflowService = (WorkflowIntegrationService)Registry.getApplicationContext().getBean("workflowIntegrationService", WorkflowIntegrationService.class);
        this.processParameterHelper = (ProcessParameterHelper)Registry.getApplicationContext().getBean("processParameterHelper", ProcessParameterHelper.class);
    }


    public void trigger(BusinessProcessModel process)
    {
        ProcessTaskModel task = new ProcessTaskModel();
        task.setAction(getId());
        task.setExecutionDate(new Date());
        bindTaskToProcessDefaultNodeGroup(task, process);
        task.setProcess(process);
        task.setRunnerBean("taskRunner");
        getTaskManager().scheduleTask((TaskModel)task);
    }


    public String execute(BusinessProcessModel process)
    {
        WorkflowTemplateModel workflowTemplate = this.workflowService.createOrReadWorkflowTemplate(this.userGroup);
        BusinessProcessParameterModel param = this.processParameterHelper.getProcessParameterByName(process, "EVENT_AFTER_WORKFLOW_PARAM");
        if(param == null)
        {
            param = new BusinessProcessParameterModel();
            param.setName("EVENT_AFTER_WORKFLOW_PARAM");
            param.setProcess(process);
        }
        param.setValue(this.nextVirtualWait.getEvent());
        this.modelService.save(param);
        WorkflowModel workflow = this.workflowService.createWorkflow(workflowTemplate, process);
        this.workflowService.startWorkflow(workflow);
        this.nextVirtualWait.trigger(process);
        return "OK";
    }
}
