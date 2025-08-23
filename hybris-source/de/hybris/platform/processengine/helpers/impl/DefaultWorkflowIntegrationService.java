package de.hybris.platform.processengine.helpers.impl;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.processengine.action.AfterWorkflowAction;
import de.hybris.platform.processengine.definition.xml.Localizedmessage;
import de.hybris.platform.processengine.definition.xml.UserGroupType;
import de.hybris.platform.processengine.helpers.WorkflowIntegrationService;
import de.hybris.platform.processengine.impl.WorkflowIntegrationDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.jalo.AbstractWorkflowAction;
import de.hybris.platform.workflow.jalo.AbstractWorkflowDecision;
import de.hybris.platform.workflow.jalo.Workflow;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.jalo.WorkflowActionTemplate;
import de.hybris.platform.workflow.jalo.WorkflowDecisionTemplate;
import de.hybris.platform.workflow.jalo.WorkflowItemAttachment;
import de.hybris.platform.workflow.jalo.WorkflowManager;
import de.hybris.platform.workflow.jalo.WorkflowTemplate;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DefaultWorkflowIntegrationService implements WorkflowIntegrationService
{
    private ModelService modelService;
    private UserService userService;
    private WorkflowIntegrationDao workflowIntegrationDao;


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public WorkflowModel createWorkflow(WorkflowTemplateModel template, Object object)
    {
        if(template != null)
        {
            WorkflowTemplate workflowTemplate = (WorkflowTemplate)this.modelService.toPersistenceLayer(template);
            Item item = (Item)this.modelService.toPersistenceLayer(object);
            Workflow workflow = workflowTemplate.createWorkflow();
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
            return (WorkflowModel)this.modelService.toModelLayer(workflow);
        }
        return null;
    }


    public WorkflowTemplateModel getWorkflowTemplateModelById(String id)
    {
        return this.workflowIntegrationDao.getWorkflowTemplateModelById(id);
    }


    public void startWorkflow(WorkflowModel workflowModel)
    {
        Workflow workflow = (Workflow)this.modelService.toPersistenceLayer(workflowModel);
        workflow.startWorkflow();
    }


    public void setWorkflowIntegrationDao(WorkflowIntegrationDao workflowIntegrationDao)
    {
        this.workflowIntegrationDao = workflowIntegrationDao;
    }


    public WorkflowIntegrationDao getWorkflowIntegrationDao()
    {
        return this.workflowIntegrationDao;
    }


    private void createActionLink(WorkflowActionTemplateModel fromAction, WorkflowActionTemplateModel toAction, String message, List<Localizedmessage> msgLocaleList)
    {
        WorkflowDecisionTemplateModel workflowDecisionTemplate = (WorkflowDecisionTemplateModel)this.modelService.create(WorkflowDecisionTemplateModel.class);
        if(msgLocaleList == null || msgLocaleList.isEmpty())
        {
            workflowDecisionTemplate.setName(message);
        }
        else
        {
            for(Localizedmessage locMsg : msgLocaleList)
            {
                workflowDecisionTemplate.setName(locMsg.getName(), new Locale(locMsg.getLanguage()));
            }
        }
        workflowDecisionTemplate.setCode(fromAction.getCode());
        workflowDecisionTemplate.setActionTemplate(fromAction);
        this.modelService.save(workflowDecisionTemplate);
        WorkflowDecisionTemplate tmp = (WorkflowDecisionTemplate)this.modelService.toPersistenceLayer(workflowDecisionTemplate);
        tmp.addToToTemplateActions((WorkflowActionTemplate)this.modelService.toPersistenceLayer(toAction));
        List<Link> incomingLinkList = (List<Link>)WorkflowManager.getInstance().getLinkTemplates((AbstractWorkflowDecision)tmp, (AbstractWorkflowAction)this.modelService
                        .toPersistenceLayer(fromAction));
        for(Link link : incomingLinkList)
        {
            WorkflowManager.getInstance().setAndConnectionTemplate(link, Boolean.TRUE);
        }
    }


    public WorkflowTemplateModel createOrReadWorkflowTemplate(List<UserGroupType> userGroup)
    {
        StringBuffer id = new StringBuffer("GEN");
        String dash = "_";
        for(UserGroupType s : userGroup)
        {
            id.append("_");
            id.append(s.getName());
            id.append("_");
            id.append(s.getMessage());
        }
        WorkflowTemplateModel workflowTemplate = getWorkflowTemplateModelById(id.toString());
        if(workflowTemplate != null)
        {
            return workflowTemplate;
        }
        return createWorkflowTemplate(userGroup, id.toString(), "WorkfowTemplate automaticly created by processEngine");
    }


    private WorkflowTemplateModel createWorkflowTemplate(List<UserGroupType> userGroup, String code, String description)
    {
        WorkflowTemplateModel workflowTemplateModel = (WorkflowTemplateModel)this.modelService.create(WorkflowTemplateModel.class);
        workflowTemplateModel.setCode(code);
        workflowTemplateModel.setDescription(description);
        this.modelService.save(workflowTemplateModel);
        WorkflowActionType workflowActionType = WorkflowActionType.START;
        WorkflowActionTemplateModel action = null;
        WorkflowActionTemplateModel prevAction = null;
        UserGroupModel userGroupModel = null;
        String lastMessage = "";
        List<Localizedmessage> lastMsgLoc = null;
        for(UserGroupType uid : userGroup)
        {
            userGroupModel = this.userService.getUserGroupForUID(uid.getName());
            action = new WorkflowActionTemplateModel();
            action.setActionType(workflowActionType);
            action.setPrincipalAssigned((PrincipalModel)userGroupModel);
            action.setWorkflow(workflowTemplateModel);
            action.setCode(code + "_" + code);
            this.modelService.save(action);
            if(prevAction != null)
            {
                createActionLink(prevAction, action, uid.getMessage(), uid.getLocmessage());
            }
            prevAction = action;
            lastMessage = uid.getMessage();
            lastMsgLoc = uid.getLocmessage();
            workflowActionType = WorkflowActionType.NORMAL;
        }
        AutomatedWorkflowActionTemplateModel autoActionTemplate = new AutomatedWorkflowActionTemplateModel();
        autoActionTemplate.setActionType(WorkflowActionType.NORMAL);
        autoActionTemplate.setPrincipalAssigned((PrincipalModel)userGroupModel);
        autoActionTemplate.setWorkflow(workflowTemplateModel);
        autoActionTemplate.setJobClass(AfterWorkflowAction.class);
        autoActionTemplate.setCode(code + "_BACK_TO_PROCESSENGINE");
        this.modelService.save(autoActionTemplate);
        if(action == null)
        {
            throw new IllegalStateException("action must not be null");
        }
        createActionLink(action, (WorkflowActionTemplateModel)autoActionTemplate, lastMessage, lastMsgLoc);
        action = new WorkflowActionTemplateModel();
        action.setActionType(WorkflowActionType.END);
        action.setPrincipalAssigned((PrincipalModel)userGroupModel);
        action.setWorkflow(workflowTemplateModel);
        action.setCode(code + "_END");
        this.modelService.save(action);
        createActionLink((WorkflowActionTemplateModel)autoActionTemplate, action, "Workflow finish", null);
        return workflowTemplateModel;
    }
}
