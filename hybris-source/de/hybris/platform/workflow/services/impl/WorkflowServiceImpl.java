package de.hybris.platform.workflow.services.impl;

import bsh.EvalError;
import bsh.Interpreter;
import de.hybris.platform.core.BeanShellUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import de.hybris.platform.workflow.dao.WorkflowDAO;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.jalo.Workflow;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.jalo.WorkflowActionComment;
import de.hybris.platform.workflow.jalo.WorkflowActionDecideException;
import de.hybris.platform.workflow.jalo.WorkflowDecision;
import de.hybris.platform.workflow.jalo.WorkflowItemAttachment;
import de.hybris.platform.workflow.jalo.WorkflowManager;
import de.hybris.platform.workflow.jalo.WorkflowTemplate;
import de.hybris.platform.workflow.jalo.WorkflowTerminatedException;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import de.hybris.platform.workflow.services.WorkflowService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = true)
public class WorkflowServiceImpl implements WorkflowService
{
    private static final Logger LOG = Logger.getLogger(WorkflowServiceImpl.class);
    private Interpreter shellInterpreter = null;
    private ModelService modelService;
    private WorkflowDAO workflowDao;
    private UserService userService;


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(String attachmentClassName)
    {
        List<WorkflowActionModel> ret = new ArrayList<>();
        List<WorkflowAction> actions = WorkflowManager.getInstance().getAllUserWorkflowActionsWithAttachment(attachmentClassName);
        for(WorkflowAction workflowAction : actions)
        {
            ret.add((WorkflowActionModel)getModelService().get(workflowAction.getPK()));
        }
        return ret;
    }


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(ComposedType attachmentType)
    {
        List<WorkflowActionModel> ret = new ArrayList<>();
        List<WorkflowAction> actions = WorkflowManager.getInstance().getAllUserWorkflowActionsWithAttachment(attachmentType);
        for(WorkflowAction workflowAction : actions)
        {
            ret.add((WorkflowActionModel)getModelService().get(workflowAction.getPK()));
        }
        return ret;
    }


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> attachmentClassNames)
    {
        return getAllUserWorkflowActionsWithAttachments(attachmentClassNames, Collections.EMPTY_LIST);
    }


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> attachmentClassNames, Collection<WorkflowActionStatus> actionStatuses)
    {
        List<EnumerationValue> jaloActionStatuses = new ArrayList<>();
        for(WorkflowActionStatus actionStatus : actionStatuses)
        {
            jaloActionStatuses.add(EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONSTATUS, actionStatus
                            .getCode()));
        }
        List<WorkflowActionModel> ret = new ArrayList<>();
        List<WorkflowAction> actions = WorkflowManager.getInstance().getAllUserWorkflowActionsWithAttachments(attachmentClassNames, jaloActionStatuses);
        for(WorkflowAction workflowAction : actions)
        {
            ret.add((WorkflowActionModel)getModelService().get(workflowAction.getPK()));
        }
        return ret;
    }


    public List<WorkflowActionCommentModel> getCommentsForAction(WorkflowActionModel action)
    {
        List<WorkflowActionCommentModel> ret = new ArrayList<>();
        Collection<WorkflowActionComment> comments = ((WorkflowAction)getModelService().getSource(action)).getWorkflowActionComments();
        for(WorkflowActionComment comment : comments)
        {
            ret.add((WorkflowActionCommentModel)getModelService().get(comment.getPK()));
        }
        return ret;
    }


    public List<ItemModel> getAttachmentsForAction(WorkflowActionModel action)
    {
        List<ItemModel> ret = new ArrayList<>();
        List<Item> attachments = ((WorkflowAction)getModelService().getSource(action)).getAttachmentItems();
        for(Item attachment : attachments)
        {
            if(TypeManager.getInstance().getComposedType(Product.class).isAssignableFrom((Type)attachment.getComposedType()))
            {
                ret.add((ItemModel)getModelService().get(attachment.getPK()));
            }
        }
        return ret;
    }


    public List<ItemModel> getAttachmentsForAction(WorkflowActionModel action, String attachmentClassName)
    {
        return getAttachmentsForAction(action, Collections.singletonList(attachmentClassName));
    }


    public List<ItemModel> getAttachmentsForAction(WorkflowActionModel action, List<String> attachmentClassNames)
    {
        List<ItemModel> ret = new ArrayList<>();
        for(String attachmentClassName : attachmentClassNames)
        {
            try
            {
                Class<?> typeClass = Class.forName(attachmentClassName);
                ComposedType type = TypeManager.getInstance().getComposedType(typeClass);
                List<Item> attachments = ((WorkflowAction)getModelService().getSource(action)).getAttachmentItems();
                if(attachments != null)
                {
                    for(Item attachment : attachments)
                    {
                        if(attachment != null && type.isAssignableFrom((Type)attachment.getComposedType()))
                        {
                            ret.add((ItemModel)getModelService().get(attachment.getPK()));
                        }
                    }
                }
            }
            catch(ClassNotFoundException e)
            {
                LOG.error("The class " + attachmentClassName + " could not be found. Please check if the class name is the fully qualified name for the class.", e);
            }
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<WorkflowDecisionModel> getDecisionsForAction(WorkflowActionModel action)
    {
        List<WorkflowDecisionModel> ret = new ArrayList<>();
        Collection<WorkflowDecision> decisions = ((WorkflowAction)getModelService().getSource(action)).getDecisions();
        for(WorkflowDecision decision : decisions)
        {
            ret.add((WorkflowDecisionModel)getModelService().get(decision.getPK()));
        }
        return ret;
    }


    public void decideAction(WorkflowActionModel action, WorkflowDecisionModel decision)
    {
        if(isTerminated(action.getWorkflow()))
        {
            throw new WorkflowTerminatedException("The workflow " + action.getWorkflow().getName() + " is terminated.");
        }
        WorkflowAction workAct = (WorkflowAction)getModelService().getSource(action);
        WorkflowDecision workDec = (WorkflowDecision)getModelService().getSource(decision);
        workAct.setSelectedDecision(workDec);
        try
        {
            workAct.decide();
        }
        catch(WorkflowActionDecideException e)
        {
            throw new WorkflowActionDecideException(e);
        }
    }


    public void addComment(Object comment, WorkflowActionModel action)
    {
        if(isTerminated(action.getWorkflow()))
        {
            throw new WorkflowTerminatedException("The workflow " + action.getWorkflow().getName() + " is terminated.");
        }
        if(comment instanceof String)
        {
            Map<String, Serializable> commentMap = new HashMap<>();
            commentMap.put("comment", (Serializable)comment);
            commentMap.put("user", JaloSession.getCurrentSession().getUser());
            ((WorkflowAction)getModelService().getSource(action)).addToWorkflowActionComments(WorkflowManager.getInstance()
                            .createWorkflowActionComment(commentMap));
        }
    }


    public void evaluteActivationScripts(ItemModel itemModel, Map currentValues, Map initialValues, String action)
    {
        Interpreter interpreter = getInterpreter();
        synchronized(interpreter)
        {
            if(itemModel != null)
            {
                try
                {
                    Item item = (Item)getModelService().getSource(itemModel);
                    interpreter.set("action", action);
                    interpreter.set("item", item);
                    interpreter.set("itemType", item.getComposedType());
                    interpreter.set("initialValues", initialValues);
                    interpreter.set("currentValues", currentValues);
                    for(WorkflowTemplate wft : WorkflowManager.getInstance().getAllWorkflowTemplates())
                    {
                        String activationScript = wft.getActivationScript();
                        if(activationScript != null)
                        {
                            Object activate = interpreter.eval(activationScript);
                            if(activate instanceof Boolean && ((Boolean)activate).booleanValue())
                            {
                                createWorkflow((WorkflowTemplateModel)this.modelService.get(wft), (ItemModel)this.modelService
                                                .get(item));
                            }
                        }
                    }
                }
                catch(EvalError e)
                {
                    LOG.error("Error occured during call to Bean Shell", (Throwable)e);
                }
            }
        }
    }


    protected Interpreter getInterpreter()
    {
        if(this.shellInterpreter == null)
        {
            this.shellInterpreter = BeanShellUtils.createInterpreter();
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.jalo.type");
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.jalo.product");
        }
        return this.shellInterpreter;
    }


    public WorkflowModel createWorkflow(WorkflowTemplateModel templateModel, ItemModel itemModel)
    {
        WorkflowTemplate template = (WorkflowTemplate)this.modelService.getSource(templateModel);
        Workflow workflow = null;
        Item item = (Item)this.modelService.getSource(itemModel);
        if(template != null)
        {
            workflow = template.createWorkflow();
            Map<String, Object> map = new HashMap<>();
            map.put("code", "toCheck");
            if(item != null)
            {
                map.put("item", item);
            }
            map.put("workflow", workflow);
            WorkflowItemAttachment att = WorkflowManager.getInstance().createWorkflowItemAttachment(map);
            workflow.addToAttachments(att);
            for(WorkflowAction action : workflow.getActions())
            {
                action.addToAttachments(att);
            }
            workflow.toggleActions();
        }
        return (WorkflowModel)this.modelService.get(workflow);
    }


    public WorkflowModel createWorkflow(String workflowName, WorkflowTemplateModel template, List<ItemModel> itemsToAdd)
    {
        Workflow workflow = null;
        if(template != null)
        {
            workflow = ((WorkflowTemplate)getModelService().getSource(template)).createWorkflow();
            workflow.setName(workflowName);
            Map<String, Object> map = new HashMap<>();
            List<WorkflowItemAttachment> workflowItemAttachments = new ArrayList<>();
            for(ItemModel item2add : itemsToAdd)
            {
                Item item = (Item)getModelService().getSource(item2add);
                map.put("code", "toCheck");
                map.put("item", item);
                map.put("workflow", workflow);
                WorkflowItemAttachment att = WorkflowManager.getInstance().createWorkflowItemAttachment(map);
                workflowItemAttachments.add(att);
            }
            workflow.setAttachments(workflowItemAttachments);
            for(WorkflowAction action : workflow.getActions())
            {
                action.setAttachments(workflowItemAttachments);
            }
            return (WorkflowModel)getModelService().get(workflow);
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public boolean isAutomatedComment(WorkflowActionCommentModel comment)
    {
        return (((WorkflowActionComment)getModelService().getSource(comment)).getUser() == null);
    }


    public List<WorkflowTemplateModel> getAllWorkflowTemplates()
    {
        return (List<WorkflowTemplateModel>)getModelService().getAll(WorkflowManager.getInstance().getAllWorkflowTemplates(), new ArrayList());
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<WorkflowModel> getWorkflowsByTemplate(WorkflowTemplateModel template)
    {
        return (List<WorkflowModel>)getModelService().getAll(
                        WorkflowManager.getInstance().getWorkflowsByTemplate((WorkflowTemplate)getModelService().getSource(template)), new ArrayList());
    }


    public List<WorkflowModel> getAllWorkflows(int workflowsStatuses, int adhocWorkflowsStatuses, Date dateFrom, Date dateTo, Date adhocDateFrom, Date adhocDateTo)
    {
        List<WorkflowModel> ret = new ArrayList<>();
        if(workflowsStatuses > 0)
        {
            ret.addAll(filterWorkflows(workflowsStatuses, getWorkflowDao().getAllWorkflows(dateFrom, dateTo)));
        }
        if(adhocWorkflowsStatuses > 0)
        {
            ret.addAll(
                            filterWorkflows(adhocWorkflowsStatuses, getWorkflowDao().getAllAdhocWorkflows(adhocDateFrom, adhocDateTo)));
        }
        return ret;
    }


    private Collection<? extends WorkflowModel> filterWorkflows(int workflowsStatuses, List<WorkflowModel> workflows)
    {
        List<WorkflowModel> ret = new ArrayList<>();
        for(WorkflowModel workflow : workflows)
        {
            if((workflowsStatuses & 0x1) == 1 && isPlanned(workflow))
            {
                ret.add(workflow);
            }
            if((workflowsStatuses & 0x2) == 2 && isRunning(workflow))
            {
                ret.add(workflow);
            }
            if((workflowsStatuses & 0x4) == 4 && isFinished(workflow))
            {
                ret.add(workflow);
            }
            if((workflowsStatuses & 0x10) == 16 && isTerminated(workflow))
            {
                ret.add(workflow);
            }
        }
        return ret;
    }


    public WorkflowDAO getWorkflowDao()
    {
        return this.workflowDao;
    }


    public WorkflowModel createAdhocWorkflow(String defaultName, List<ItemModel> itemsToAdd)
    {
        WorkflowTemplateModel adhocTemplate = getWorkflowDao().getAdhocWorkflowTemplate();
        WorkflowModel workflowModel = createWorkflow(defaultName, adhocTemplate, itemsToAdd);
        UserModel curUser = getUserService().getCurrentUser();
        workflowModel.setOwner((ItemModel)curUser);
        WorkflowActionModel approveAction = getNormalWorkflowActions(workflowModel).get(0);
        approveAction.setPrincipalAssigned((PrincipalModel)curUser);
        WorkflowActionModel endAction = getEndWorkflowActions(workflowModel).get(0);
        endAction.setPrincipalAssigned((PrincipalModel)curUser);
        getModelService().save(workflowModel);
        getModelService().save(approveAction);
        getModelService().save(endAction);
        return workflowModel;
    }


    public void setWorkflowDao(WorkflowDAO workflowDao)
    {
        this.workflowDao = workflowDao;
    }


    public void assignUser(PrincipalModel principalModel, WorkflowModel workflowModel)
    {
        if(isAdhocWorkflow(workflowModel))
        {
            List<WorkflowActionModel> actions = getStartWorkflowActions(workflowModel);
            for(WorkflowActionModel action : actions)
            {
                action.setPrincipalAssigned(principalModel);
            }
        }
        else
        {
            List<WorkflowActionModel> actions = workflowModel.getActions();
            for(WorkflowActionModel workflowActionModel : actions)
            {
                workflowActionModel.setPrincipalAssigned(principalModel);
            }
        }
        this.modelService.saveAll();
    }


    private ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public List<WorkflowTemplateModel> getAllVisibleWorkflowTemplates()
    {
        List<? extends WorkflowTemplateModel> tmpls = getWorkflowDao().getUsersWorkflowTemplates();
        Set<WorkflowTemplateModel> templates = new HashSet<>(tmpls);
        List<WorkflowTemplateModel> visibleTmpls = getWorkflowDao().getWorkflowTemplatesVisibleForUser((PrincipalModel)
                        getUserService().getCurrentUser());
        if(!visibleTmpls.isEmpty())
        {
            templates.addAll(visibleTmpls);
        }
        return new ArrayList<>(templates);
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public boolean isPaused(Object workflowModel)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            String pausedStatus = GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.PAUSED;
            for(WorkflowActionModel action : ((WorkflowModel)workflowModel).getActions())
            {
                if(pausedStatus.equalsIgnoreCase(action.getStatus().getCode()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public void startWorkflow(ItemModel workflowModel)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            Workflow workflow = (Workflow)getModelService().getSource(workflowModel);
            workflow.startWorkflow();
        }
    }


    public void terminateWorkflow(ItemModel workflowModel)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            Workflow workflow = (Workflow)getModelService().getSource(workflowModel);
            workflow.terminateWorkflow();
        }
    }


    public boolean isTerminated(Object workflowModel)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            WorkflowActionStatus terminatedStatus = WorkflowActionStatus.TERMINATED;
            for(WorkflowActionModel action : ((WorkflowModel)workflowModel).getActions())
            {
                if(terminatedStatus.equals(action.getStatus()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isPlanned(Object workflowModel)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            Workflow workflow = (Workflow)getModelService().getSource(workflowModel);
            return workflow.isPlanned();
        }
        return false;
    }


    public boolean isRunning(Object workflowModel)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            for(WorkflowActionModel action : ((WorkflowModel)workflowModel).getActions())
            {
                if(WorkflowActionStatus.IN_PROGRESS.equals(action.getStatus()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isFinished(Object workflowModel)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            Workflow workflow = (Workflow)getModelService().getSource(workflowModel);
            if(workflow.isCompleted())
            {
                return true;
            }
            for(WorkflowAction workflowAction : workflow.getActions())
            {
                if(workflowAction.getStatus().equals(WorkflowAction.getEndedByWorkflowStatus()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public void addItems(ItemModel workflowModel, List<ItemModel> itemsToAdd)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            Workflow workflow = (Workflow)getModelService().getSource(workflowModel);
            List<WorkflowItemAttachment> workflowItemAttachments = new ArrayList<>();
            for(int i = 0; i < itemsToAdd.size(); i++)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("code", "toCheck");
                Item localItem = JaloSession.getCurrentSession().getItem(((ItemModel)itemsToAdd.get(i)).getPk());
                map.put("item", localItem);
                map.put("workflow", workflow);
                WorkflowItemAttachment att = WorkflowManager.getInstance().createWorkflowItemAttachment(map);
                workflowItemAttachments.add(att);
            }
            for(WorkflowAction action : workflow.getActions())
            {
                List<WorkflowItemAttachment> actionatt = new ArrayList<>();
                List<WorkflowItemAttachment> existingatt = action.getAttachments();
                actionatt.addAll(existingatt);
                actionatt.addAll(workflowItemAttachments);
                action.setAttachments(actionatt);
            }
        }
    }


    public ItemModel containsItem(ItemModel workflowModel, List<ItemModel> itemsToAdd)
    {
        if(workflowModel instanceof WorkflowModel)
        {
            List<WorkflowItemAttachmentModel> attachments = ((WorkflowModel)workflowModel).getAttachments();
            for(WorkflowItemAttachmentModel workflowItemAttachmentModel : attachments)
            {
                ItemModel itemModel = workflowItemAttachmentModel.getItem();
                if(itemsToAdd.contains(itemModel))
                {
                    return itemModel;
                }
            }
        }
        return null;
    }


    public boolean isAdhocWorkflow(WorkflowModel workflowModel)
    {
        WorkflowTemplateModel adhocTemplate = getWorkflowDao().getAdhocWorkflowTemplate();
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
            List<WorkflowActionModel> startActions = getStartWorkflowActions(workf);
            boolean hasAssignedPrincipal = true;
            for(WorkflowActionModel action : startActions)
            {
                PrincipalModel assigne = action.getPrincipalAssigned();
                if(assigne == null || getModelService().getSource(assigne)
                                .equals(this.workflowDao.getAdhocWorkflowTemplateDummyOwner()))
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
            Iterator<WorkflowActionModel> iterator = getStartWorkflowActions(workflow).iterator();
            if(iterator.hasNext())
            {
                WorkflowActionModel action = iterator.next();
                return action.getFirstActivated();
            }
        }
        return startTime;
    }


    public void unassignUser(PrincipalModel principalModel, WorkflowModel workflowModel)
    {
        if(isAdhocWorkflow(workflowModel))
        {
            List<WorkflowActionModel> actions = workflowModel.getActions();
            for(WorkflowActionModel actionModel : actions)
            {
                ((WorkflowAction)getModelService().getSource(actionModel)).setPrincipalAssigned((Principal)getWorkflowDao()
                                .getAdhocWorkflowTemplateDummyOwner());
            }
        }
    }


    public List<WorkflowActionModel> getStartWorkflowActions(WorkflowModel wfModel)
    {
        List<WorkflowActionModel> ret = new ArrayList<>();
        ret.addAll(getWorkflowDao().getStartWorkflowActions(wfModel));
        return ret;
    }


    public List<WorkflowActionModel> getNormalWorkflowActions(WorkflowModel wfModel)
    {
        List<WorkflowActionModel> ret = new ArrayList<>();
        ret.addAll(getWorkflowDao().getNormalWorkflowActions(wfModel));
        return ret;
    }


    public List<WorkflowActionModel> getEndWorkflowActions(WorkflowModel wfModel)
    {
        List<WorkflowActionModel> ret = new ArrayList<>();
        ret.addAll(getWorkflowDao().getEndWorkflowActions(wfModel));
        return ret;
    }


    public List<WorkflowActionModel> getWorkflowActionsByType(EnumerationValue type, WorkflowModel wfModel)
    {
        List<WorkflowActionModel> ret = new ArrayList<>();
        ret.addAll(getWorkflowDao().getWorkflowActionsByType(type, wfModel));
        return ret;
    }
}
