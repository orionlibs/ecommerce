package de.hybris.platform.workflow.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.daos.WorkflowDao;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import de.hybris.platform.workflow.services.internal.WorkflowFactory;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWorkflowService implements WorkflowService
{
    private static final Logger LOG = Logger.getLogger(DefaultWorkflowService.class);
    private ModelService modelService;
    private WorkflowDao workflowDao;
    private WorkflowActionService workflowActionService;
    private WorkflowTemplateService workflowTemplateService;
    private WorkflowFactory<WorkflowModel, WorkflowTemplateModel, List<WorkflowActionModel>> actionsFromWorkflowTemplate;
    private WorkflowFactory<WorkflowModel, WorkflowActionTemplateModel, List<WorkflowDecisionModel>> decisionsFromActionTemplate;
    private WorkflowFactory<WorkflowModel, WorkflowActionTemplateModel, List<AbstractWorkflowActionModel>> predecessorsFromActionTemplate;


    @Required
    public void setActionsWorkflowTemplateFactory(WorkflowFactory<WorkflowModel, WorkflowTemplateModel, List<WorkflowActionModel>> actionsFromWorkflowTemplate)
    {
        this.actionsFromWorkflowTemplate = actionsFromWorkflowTemplate;
    }


    @Required
    public void setDecisionsActionTemplateFactory(WorkflowFactory<WorkflowModel, WorkflowActionTemplateModel, List<WorkflowDecisionModel>> decisionsFromActionTemplate)
    {
        this.decisionsFromActionTemplate = decisionsFromActionTemplate;
    }


    @Required
    public void setPredecessorsActionTemplateFactory(WorkflowFactory<WorkflowModel, WorkflowActionTemplateModel, List<AbstractWorkflowActionModel>> predecessorsFromActionTemplate)
    {
        this.predecessorsFromActionTemplate = predecessorsFromActionTemplate;
    }


    public List<WorkflowModel> getWorkflowsForTemplateAndUser(WorkflowTemplateModel template, UserModel user)
    {
        return this.workflowDao.findWorkflowsByUserAndTemplate(user, template);
    }


    public boolean isTerminated(WorkflowModel workflowModel)
    {
        for(WorkflowActionModel action : workflowModel.getActions())
        {
            if(WorkflowActionStatus.TERMINATED == action.getStatus())
            {
                return true;
            }
        }
        return false;
    }


    public WorkflowModel createWorkflow(WorkflowTemplateModel template, ItemModel attachmentItem, UserModel owner)
    {
        ServicesUtil.validateParameterNotNull(template, "Template must not be null");
        WorkflowModel workflow = createWorkflow(template, owner);
        WorkflowItemAttachmentModel att = (WorkflowItemAttachmentModel)this.modelService.create(WorkflowItemAttachmentModel.class);
        att.setCode("toCheck");
        att.setItem(attachmentItem);
        att.setWorkflow(workflow);
        List<WorkflowItemAttachmentModel> attachments = new ArrayList<>(workflow.getAttachments());
        attachments.add(att);
        workflow.setAttachments(attachments);
        for(WorkflowActionModel action : workflow.getActions())
        {
            List<WorkflowItemAttachmentModel> actionAttachments = new ArrayList<>(action.getAttachments());
            actionAttachments.add(att);
            action.setAttachments(actionAttachments);
        }
        return workflow;
    }


    public WorkflowModel createWorkflow(String workflowName, WorkflowTemplateModel template, List<ItemModel> itemsToAdd, UserModel owner)
    {
        ServicesUtil.validateParameterNotNull(template, "Template must not be null");
        WorkflowModel workflow = createWorkflow(template, owner);
        workflow.setName(workflowName);
        List<WorkflowItemAttachmentModel> workflowItemAttachments = new ArrayList<>();
        for(ItemModel item2add : itemsToAdd)
        {
            WorkflowItemAttachmentModel att = (WorkflowItemAttachmentModel)this.modelService.create(WorkflowItemAttachmentModel.class);
            att.setCode("toCheck");
            att.setItem(item2add);
            att.setWorkflow(workflow);
            workflowItemAttachments.add(att);
        }
        workflow.setAttachments(workflowItemAttachments);
        Set<ItemModel> modelsToSave = new LinkedHashSet<>();
        for(WorkflowActionModel action : workflow.getActions())
        {
            action.setAttachments(workflowItemAttachments);
            modelsToSave.add(action);
        }
        modelsToSave.add(workflow);
        this.modelService.saveAll(modelsToSave);
        return workflow;
    }


    public WorkflowModel createWorkflow(WorkflowTemplateModel template, UserModel owner)
    {
        WorkflowModel workflow = (WorkflowModel)this.modelService.create(WorkflowModel.class);
        workflow.setOwner((ItemModel)owner);
        workflow.setJob((JobModel)template);
        workflow.setName(template.getName());
        workflow.setDescription(template.getDescription());
        createActions(template, workflow);
        return workflow;
    }


    public List<WorkflowModel> getAllWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date dateFrom, Date dateTo)
    {
        return filterWorkflows(workflowsStatuses, this.workflowDao.findAllWorkflows(dateFrom, dateTo));
    }


    public List<WorkflowModel> getAllAdhocWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date adhocDateFrom, Date adhocDateTo)
    {
        return filterWorkflows(workflowsStatuses, this.workflowDao.findAllAdhocWorkflows(adhocDateFrom, adhocDateTo));
    }


    public SearchResult<WorkflowModel> getAllWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date dateFrom, Date dateTo, int startIndex, int pageSize)
    {
        return this.workflowDao.findAllWorkflows(dateFrom, dateTo, workflowsStatuses, startIndex, pageSize);
    }


    public SearchResult<WorkflowModel> getAllAdhocWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date adhocDateFrom, Date adhocDateTo, int startIndex, int pageSize)
    {
        return this.workflowDao.findAllAdhocWorkflows(adhocDateFrom, adhocDateTo, workflowsStatuses, startIndex, pageSize);
    }


    private List<WorkflowModel> filterWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, List<WorkflowModel> workflows)
    {
        List<WorkflowModel> ret = new ArrayList<>();
        for(WorkflowModel workflow : workflows)
        {
            if(workflowsStatuses.contains(WorkflowStatus.PLANNED) && isPlanned(workflow))
            {
                ret.add(workflow);
            }
            if(workflowsStatuses.contains(WorkflowStatus.RUNNING) && isRunning(workflow))
            {
                ret.add(workflow);
            }
            if(workflowsStatuses.contains(WorkflowStatus.FINISHED) && isFinished(workflow))
            {
                ret.add(workflow);
            }
            if(workflowsStatuses.contains(WorkflowStatus.TERMINATED) && isTerminated(workflow))
            {
                ret.add(workflow);
            }
        }
        return ret;
    }


    public boolean isPlanned(WorkflowModel workflowModel)
    {
        for(WorkflowActionModel action : workflowModel.getActions())
        {
            if(WorkflowActionStatus.PENDING != action.getStatus())
            {
                return false;
            }
        }
        return true;
    }


    public boolean isRunning(WorkflowModel workflowModel)
    {
        for(WorkflowActionModel action : workflowModel.getActions())
        {
            if(WorkflowActionStatus.IN_PROGRESS == action.getStatus())
            {
                return true;
            }
        }
        return false;
    }


    public boolean isFinished(WorkflowModel workflowModel)
    {
        if(isCompleted(workflowModel))
        {
            return true;
        }
        for(WorkflowActionModel workflowAction : workflowModel.getActions())
        {
            if(workflowAction.getStatus() == WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW)
            {
                return true;
            }
        }
        return false;
    }


    public boolean isPaused(WorkflowModel workflowModel)
    {
        for(WorkflowActionModel action : workflowModel.getActions())
        {
            if(WorkflowActionStatus.PAUSED == action.getStatus())
            {
                return true;
            }
        }
        return false;
    }


    public boolean isCompleted(WorkflowModel workflowModel)
    {
        for(WorkflowActionModel action : workflowModel.getActions())
        {
            if(!this.workflowActionService.isCompleted(action))
            {
                return false;
            }
        }
        return true;
    }


    private List<WorkflowActionModel> createActions(WorkflowTemplateModel template, WorkflowModel workflow)
    {
        List<WorkflowActionModel> workFlowActionList = (List<WorkflowActionModel>)this.actionsFromWorkflowTemplate.create(workflow, template);
        for(WorkflowActionTemplateModel templateAction : template.getActions())
        {
            this.predecessorsFromActionTemplate.create(workflow, templateAction);
            this.decisionsFromActionTemplate.create(workflow, templateAction);
        }
        return workFlowActionList;
    }


    protected WorkflowActionModel getWorkAction(WorkflowActionTemplateModel templateAction, List<WorkflowActionModel> workflowActions)
    {
        for(WorkflowActionModel act : workflowActions)
        {
            if(act.getTemplate().equals(templateAction))
            {
                return act;
            }
        }
        return null;
    }


    public void writeAutomatedComment(WorkflowActionModel action, String message, String... messageParams)
    {
        List<WorkflowActionCommentModel> comments = new ArrayList<>(action.getWorkflowActionComments());
        WorkflowActionCommentModel comment = (WorkflowActionCommentModel)this.modelService.create(WorkflowActionCommentModel.class);
        comment.setComment(MessageFormat.format(Localization.getLocalizedString(message, (Object[])messageParams), (Object[])null));
        comment.setUser(null);
        comments.add(comment);
        action.setWorkflowActionComments(comments);
    }


    public boolean isAdhocWorkflow(WorkflowModel workflowModel)
    {
        WorkflowTemplateModel adhocTemplate = this.workflowTemplateService.getAdhocWorkflowTemplate();
        WorkflowTemplateModel workflowTemplate = workflowModel.getJob();
        if(adhocTemplate == null)
        {
            return false;
        }
        if(workflowTemplate == null)
        {
            LOG.warn("Found workflow: " + workflowModel.getName() + "(" + workflowModel.getPk() + ") with no template assigned.");
            return false;
        }
        return workflowTemplate.equals(adhocTemplate);
    }


    public boolean canBeStarted(WorkflowModel workf)
    {
        if(isAdhocWorkflow(workf))
        {
            List<WorkflowActionModel> startActions = this.workflowActionService.getStartWorkflowActions(workf);
            boolean hasAssignedPrincipal = true;
            for(WorkflowActionModel action : startActions)
            {
                PrincipalModel assigne = action.getPrincipalAssigned();
                if(assigne == null || assigne.equals(this.workflowTemplateService.getAdhocWorkflowTemplateDummyOwner()))
                {
                    hasAssignedPrincipal = false;
                }
            }
            return (!startActions.isEmpty() && hasAssignedPrincipal);
        }
        return true;
    }


    public Date getStartTime(WorkflowModel workflow)
    {
        Date startTime = workflow.getStartTime();
        if(startTime == null)
        {
            Iterator<WorkflowActionModel> iterator = this.workflowActionService.getStartWorkflowActions(workflow).iterator();
            if(iterator.hasNext())
            {
                WorkflowActionModel action = iterator.next();
                return action.getFirstActivated();
            }
        }
        return startTime;
    }


    public WorkflowModel createAdhocWorkflow(String defaultName, List<ItemModel> itemsToAdd, UserModel owner)
    {
        WorkflowTemplateModel adhocTemplate = this.workflowTemplateService.getAdhocWorkflowTemplate();
        WorkflowModel workflowModel = createWorkflow(defaultName, adhocTemplate, itemsToAdd, owner);
        workflowModel.setOwner((ItemModel)owner);
        this.modelService.save(workflowModel);
        WorkflowActionModel approveAction = this.workflowActionService.getNormalWorkflowActions(workflowModel).get(0);
        approveAction.setPrincipalAssigned((PrincipalModel)owner);
        this.modelService.save(approveAction);
        WorkflowActionModel endAction = this.workflowActionService.getEndWorkflowActions(workflowModel).get(0);
        endAction.setPrincipalAssigned((PrincipalModel)owner);
        this.modelService.save(endAction);
        return workflowModel;
    }


    public boolean assignUser(PrincipalModel principalModel, WorkflowModel workflowModel)
    {
        boolean assigned = false;
        if(isAdhocWorkflow(workflowModel))
        {
            List<WorkflowActionModel> actions = this.workflowActionService.getStartWorkflowActions(workflowModel);
            for(WorkflowActionModel action : actions)
            {
                action.setPrincipalAssigned(principalModel);
                this.modelService.save(action);
                assigned = true;
            }
        }
        else
        {
            List<WorkflowActionModel> actions = workflowModel.getActions();
            for(WorkflowActionModel workflowActionModel : actions)
            {
                workflowActionModel.setPrincipalAssigned(principalModel);
                this.modelService.save(workflowActionModel);
                assigned = true;
            }
        }
        return assigned;
    }


    public boolean unassignUser(WorkflowModel workflowModel)
    {
        boolean unassigned = false;
        if(isAdhocWorkflow(workflowModel))
        {
            List<WorkflowActionModel> actions = workflowModel.getActions();
            for(WorkflowActionModel actionModel : actions)
            {
                EmployeeModel owner = this.workflowTemplateService.getAdhocWorkflowTemplateDummyOwner();
                actionModel.setPrincipalAssigned((PrincipalModel)owner);
                this.modelService.save(actionModel);
                unassigned = true;
            }
        }
        return unassigned;
    }


    public WorkflowModel getWorkflowForCode(String code)
    {
        List<WorkflowModel> workflows = this.workflowDao.findWorkflowsByCode(code);
        ServicesUtil.validateIfSingleResult(workflows, "No WorkflowActionTemplate found for code " + code, "Too much WorkflowActionTemplates found for code " + code);
        return workflows.get(0);
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setWorkflowActionService(WorkflowActionService workflowActionService)
    {
        this.workflowActionService = workflowActionService;
    }


    public void setWorkflowDao(WorkflowDao workflowDao)
    {
        this.workflowDao = workflowDao;
    }


    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
    }
}
