package de.hybris.platform.workflow.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.daos.WorkflowActionDao;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultWorkflowActionService implements WorkflowActionService
{
    private static final Logger LOG = Logger.getLogger(DefaultWorkflowActionService.class);
    private ModelService modelService;
    private UserService userService;
    private WorkflowActionDao workflowActionDao;
    private TypeService typeService;
    private I18NService i18nService;


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(String attachmentTypeCodeOrClassName)
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(attachmentTypeCodeOrClassName);
        return getAllUserWorkflowActionsWithAttachment(type);
    }


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(ComposedTypeModel attachmentType)
    {
        return getAllUserWorkflowActionsWithAttachments(Collections.singletonList(attachmentType.getJaloclass().getSimpleName()));
    }


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> attachments)
    {
        return getAllUserWorkflowActionsWithAttachments(attachments, Collections.EMPTY_LIST);
    }


    public List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> attachments, Collection<WorkflowActionStatus> actionStatuses)
    {
        Collection<WorkflowActionStatus> statuses = CollectionUtils.isEmpty(actionStatuses) ? Collections.<WorkflowActionStatus>singletonList(WorkflowActionStatus.IN_PROGRESS) : actionStatuses;
        List<ComposedTypeModel> types = new ArrayList<>();
        if(!attachments.isEmpty())
        {
            for(String attachmentClassName : attachments)
            {
                ComposedTypeModel type = getTypeFromTypeCodeOrClassName(attachmentClassName);
                if(type != null)
                {
                    types.add(type);
                    types.addAll(type.getAllSubTypes());
                }
            }
        }
        return this.workflowActionDao.findWorkflowActionsByStatusAndAttachmentType(types, statuses);
    }


    public boolean isCompleted(WorkflowActionModel action)
    {
        return (WorkflowActionStatus.DISABLED.equals(action.getStatus()) || WorkflowActionStatus.COMPLETED
                        .equals(action.getStatus()));
    }


    public WorkflowActionModel createWorkflowAction(WorkflowActionTemplateModel template, WorkflowModel workflow)
    {
        WorkflowActionModel action = (WorkflowActionModel)this.modelService.create(WorkflowActionModel.class);
        ItemModelCloneCreator cloneCreator = new ItemModelCloneCreator(this.modelService, this.i18nService, this.typeService);
        cloneCreator.copyAttributes((ItemModel)template, (ItemModel)action, getAttributesToCopy());
        action.setWorkflow(workflow);
        action.setStatus(WorkflowActionStatus.PENDING);
        action.setTemplate(template);
        return action;
    }


    public boolean isUserAssignedPrincipal(WorkflowActionModel action)
    {
        UserModel currentUser = this.userService.getCurrentUser();
        return (this.userService.isAdmin(currentUser) || currentUser.equals(action
                        .getPrincipalAssigned()) || currentUser.getAllGroups()
                        .contains(action.getPrincipalAssigned()));
    }


    public List<WorkflowActionModel> getStartWorkflowActions(WorkflowModel wfModel)
    {
        return this.workflowActionDao.findStartWorkflowActions(wfModel);
    }


    public List<WorkflowActionModel> getNormalWorkflowActions(WorkflowModel wfModel)
    {
        return this.workflowActionDao.findNormalWorkflowActions(wfModel);
    }


    public List<WorkflowActionModel> getEndWorkflowActions(WorkflowModel wfModel)
    {
        return this.workflowActionDao.findEndWorkflowActions(wfModel);
    }


    public List<WorkflowActionModel> getWorkflowActionsByType(WorkflowActionType type, WorkflowModel wfModel)
    {
        return this.workflowActionDao.findWorkflowActionsByType(type, wfModel);
    }


    private ComposedTypeModel getTypeFromTypeCodeOrClassName(String typeCodeOrClassName)
    {
        if(!StringUtils.isBlank(typeCodeOrClassName))
        {
            try
            {
                return this.typeService.getComposedTypeForCode(typeCodeOrClassName.trim());
            }
            catch(UnknownIdentifierException e)
            {
                LOG.debug("The type " + typeCodeOrClassName + " could not be found. Trying to find by ClassName instead.");
                try
                {
                    Class<?> typeClass = Class.forName(typeCodeOrClassName.trim());
                    return this.typeService.getComposedTypeForClass(typeClass);
                }
                catch(ClassNotFoundException classNotFoundException)
                {
                    LOG.debug("The class " + typeCodeOrClassName + " could not be found. Please check if the class name is the fully qualified name for the class.");
                }
                catch(UnknownIdentifierException unknownIdentifierException)
                {
                    LOG.debug("The class " + typeCodeOrClassName + " could not be found. Please check if the class name is the fully qualified name for the class.");
                }
                try
                {
                    Class<?> typeClass = Class.forName(typeCodeOrClassName.trim());
                    return (ComposedTypeModel)this.modelService.get(TypeManager.getInstance().getComposedType(typeClass));
                }
                catch(ClassNotFoundException classNotFoundException)
                {
                    LOG.debug("The class " + typeCodeOrClassName + " could not be found. Please check if the class name is the fully qualified name for the class.", classNotFoundException);
                }
            }
        }
        return null;
    }


    public boolean isActive(WorkflowActionModel action)
    {
        return (WorkflowActionStatus.IN_PROGRESS == action.getStatus());
    }


    public boolean isEndedByWorkflow(WorkflowActionModel action)
    {
        return (WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW == action.getStatus());
    }


    public boolean isDisabled(WorkflowActionModel action)
    {
        return (WorkflowActionStatus.DISABLED == action.getStatus());
    }


    public WorkflowActionModel disable(WorkflowActionModel action)
    {
        action.setStatus(WorkflowActionStatus.DISABLED);
        return action;
    }


    public WorkflowActionModel complete(WorkflowActionModel action)
    {
        action.setStatus(WorkflowActionStatus.COMPLETED);
        return action;
    }


    public WorkflowActionModel idle(WorkflowActionModel action)
    {
        action.setStatus(WorkflowActionStatus.PENDING);
        return action;
    }


    public WorkflowActionModel getActionForCode(WorkflowModel workflow, String code)
    {
        Collection<WorkflowActionModel> actions = workflow.getActions();
        for(WorkflowActionModel action : actions)
        {
            if(action.getTemplate().getCode().equals(code))
            {
                return action;
            }
        }
        return null;
    }


    private static final Set<String> EXCLUDED_ATTRIBUTES = new HashSet<>(Arrays.asList(new String[] {"predecessors", "workflowActionComments", "successors", "code", "predecessorsStr"}));


    private Set<String> getAttributesToCopy()
    {
        Set<String> result = new HashSet<>();
        ComposedTypeModel type = this.typeService.getComposedTypeForCode("AbstractWorkflowAction");
        for(AttributeDescriptorModel attributeDescriptor : type.getDeclaredattributedescriptors())
        {
            if(!EXCLUDED_ATTRIBUTES.contains(attributeDescriptor.getQualifier()))
            {
                result.add(attributeDescriptor.getQualifier());
            }
        }
        return result;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public void setWorkflowActionDao(WorkflowActionDao workflowActionDao)
    {
        this.workflowActionDao = workflowActionDao;
    }
}
