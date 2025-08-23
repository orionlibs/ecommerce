package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowAttachmentService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WorkflowFacade
{
    private TypeService typeService;
    private UserService userService;
    private WorkflowService workflowService;
    private WorkflowProcessingService workflowProcessingService;
    private WorkflowActionService workflowActionService;
    private WorkflowAttachmentService workflowAttachmentService;
    private WorkflowTemplateService workflowTemplateService;


    public String getStatusUrl(TypedObject typedData)
    {
        WorkflowModel workflow = (WorkflowModel)typedData.getObject();
        if(isPlanned(workflow))
        {
            if(isAdhocWorkflow(workflow))
            {
                return "/cockpit/images/icon_workflow_adhoc_not_started.png";
            }
            return "/cockpit/images/icon_workflow_not_started.png";
        }
        if(isRunning(workflow))
        {
            if(isAdhocWorkflow(workflow))
            {
                return "/cockpit/images/icon_workflow_adhoc_runnig.png";
            }
            return "/cockpit/images/icon_workflow_runnig.png";
        }
        if(isFinished(workflow))
        {
            if(isAdhocWorkflow(workflow))
            {
                return "/cockpit/images/icon_workflow_adhoc_finished.png";
            }
            return "/cockpit/images/icon_workflow_finished.png";
        }
        if(isTerminated(workflow))
        {
            if(isAdhocWorkflow(workflow))
            {
                return "/cockpit/images/icon_workflow_adhoc_terminated.png";
            }
            return "/cockpit/images/icon_workflow_terminated.png";
        }
        return "cockpit/images/stop_klein.jpg";
    }


    public UserModel getCurrentUser()
    {
        return this.userService.getCurrentUser();
    }


    public UserGroupModel getUserGroupForUID(String uid)
    {
        return this.userService.getUserGroupForUID(uid);
    }


    public List<TypedObject> wrapItems(Collection<? extends Object> items)
    {
        return this.typeService.wrapItems(items);
    }


    public List<ItemModel> unwrapItems(Collection<TypedObject> typedObjects)
    {
        return this.typeService.unwrapItems(typedObjects);
    }


    public TypedObject wrapItem(Object item)
    {
        return this.typeService.wrapItem(item);
    }


    public boolean canBeStarted(WorkflowModel object)
    {
        return this.workflowService.canBeStarted(object);
    }


    public boolean isPlanned(WorkflowModel modelData)
    {
        return this.workflowService.isPlanned(modelData);
    }


    public boolean isRunning(WorkflowModel object)
    {
        return this.workflowService.isRunning(object);
    }


    public boolean isAdhocWorkflow(WorkflowModel object)
    {
        return this.workflowService.isAdhocWorkflow(object);
    }


    public boolean isFinished(WorkflowModel workflow)
    {
        return this.workflowService.isFinished(workflow);
    }


    public WorkflowModel createAdhocWorkflow(String workflowName, List emptyList, UserModel userModel)
    {
        return this.workflowService.createAdhocWorkflow(workflowName, emptyList, userModel);
    }


    @Deprecated
    public List<WorkflowModel> getAllWorkflows(WorkflowViewOptions wflOptions, WorkflowViewOptions adhocwflOptions)
    {
        List<WorkflowModel> workflows = new ArrayList<>();
        workflows.addAll(this.workflowService
                        .getAllWorkflows(wflOptions.selectedStatuses(), wflOptions.getFilterFrom(), wflOptions.getFilterTo()));
        workflows.addAll(this.workflowService.getAllAdhocWorkflows(adhocwflOptions.selectedStatuses(), adhocwflOptions.getFilterFrom(), adhocwflOptions
                        .getFilterTo()));
        return workflows;
    }


    public SearchResult<WorkflowModel> getAllWorkflows(WorkflowViewOptions wflOptions, WorkflowViewOptions adhocwflOptions, int startIndex, int pageSize)
    {
        List<WorkflowModel> out = new ArrayList<>();
        SearchResult<WorkflowModel> workflows = this.workflowService.getAllWorkflows(wflOptions.selectedStatuses(), wflOptions
                        .getFilterFrom(), wflOptions.getFilterTo(), startIndex, pageSize);
        int workflowTotalCount = workflows.getTotalCount();
        out.addAll(workflows.getResult());
        if(startIndex > workflowTotalCount)
        {
            int adhocStartIndex = startIndex - workflowTotalCount;
            int adhocPageSize = pageSize;
            SearchResult<WorkflowModel> adhocWorkflows = this.workflowService.getAllAdhocWorkflows(adhocwflOptions
                            .selectedStatuses(), adhocwflOptions.getFilterFrom(), adhocwflOptions.getFilterTo(), adhocStartIndex, adhocPageSize);
            out.addAll(adhocWorkflows.getResult());
            workflowTotalCount += adhocWorkflows.getTotalCount();
        }
        else if(workflows.getRequestedCount() != workflows.getCount())
        {
            int adhocStartIndex = 0;
            int adhocPageSize = workflows.getRequestedCount() - workflows.getCount();
            SearchResult<WorkflowModel> adhocWorkflows = this.workflowService.getAllAdhocWorkflows(adhocwflOptions
                            .selectedStatuses(), adhocwflOptions.getFilterFrom(), adhocwflOptions.getFilterTo(), 0, adhocPageSize);
            out.addAll(adhocWorkflows.getResult());
            workflowTotalCount += adhocWorkflows.getTotalCount();
        }
        else
        {
            SearchResult<WorkflowModel> adhocWorkflows = this.workflowService.getAllAdhocWorkflows(adhocwflOptions
                            .selectedStatuses(), adhocwflOptions.getFilterFrom(), adhocwflOptions.getFilterTo(), 0, 1);
            workflowTotalCount += adhocWorkflows.getTotalCount();
        }
        return (SearchResult<WorkflowModel>)new SearchResultImpl(out, workflowTotalCount, pageSize, startIndex);
    }


    public void assignUser(PrincipalModel principalModel, WorkflowModel object)
    {
        this.workflowService.assignUser(principalModel, object);
    }


    public void unassignUser(WorkflowModel object)
    {
        this.workflowService.unassignUser(object);
    }


    public void startWorkflow(WorkflowModel workflow)
    {
        this.workflowProcessingService.startWorkflow(workflow);
    }


    public void terminateWorkflow(WorkflowModel workflow)
    {
        this.workflowProcessingService.terminateWorkflow(workflow);
    }


    public List<WorkflowActionModel> getStartWorkflowActions(WorkflowModel adhocWorkflowModel)
    {
        return this.workflowActionService.getStartWorkflowActions(adhocWorkflowModel);
    }


    public boolean isTerminated(WorkflowModel workflow)
    {
        return this.workflowService.isTerminated(workflow);
    }


    public ItemModel containsItem(WorkflowModel modelData, List<ItemModel> unwrapItems)
    {
        return this.workflowAttachmentService.containsItem(modelData, unwrapItems);
    }


    public void addItems(WorkflowModel modelData, List<ItemModel> unwrapItems)
    {
        this.workflowAttachmentService.addItems(modelData, unwrapItems);
    }


    public boolean hasVisibleWorkflowTemplatesForUser(UserModel userModel)
    {
        return this.workflowTemplateService.getAllVisibleWorkflowTemplatesForUser(userModel).isEmpty();
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
    }


    public void setWorkflowActionService(WorkflowActionService workflowActionService)
    {
        this.workflowActionService = workflowActionService;
    }


    public void setWorkflowAttachmentService(WorkflowAttachmentService workflowAttachmentService)
    {
        this.workflowAttachmentService = workflowAttachmentService;
    }


    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
    }


    public WorkflowModel createWorkflow(String workflowName, WorkflowTemplateModel workflowTemplate, List emptyList, UserModel user)
    {
        return this.workflowService.createWorkflow(workflowName, workflowTemplate, emptyList, user);
    }
}
