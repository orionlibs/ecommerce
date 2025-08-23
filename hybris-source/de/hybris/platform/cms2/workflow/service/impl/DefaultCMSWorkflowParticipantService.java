package de.hybris.platform.cms2.workflow.service.impl;

import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSWorkflowParticipantService implements CMSWorkflowParticipantService
{
    private CMSWorkflowService cmsWorkflowService;
    private UserService userService;


    public boolean isWorkflowParticipant(WorkflowModel workflow)
    {
        WorkflowTemplateModel workflowTemplate = workflow.getJob();
        UserModel currentUser = getUserService().getCurrentUser();
        Collection<PrincipalModel> currentPrincipals = getRelatedPrincipals((PrincipalModel)currentUser);
        Collection<PrincipalModel> visibleForPrincipals = workflowTemplate.getVisibleForPrincipals();
        Objects.requireNonNull(currentPrincipals);
        return visibleForPrincipals.stream().anyMatch(currentPrincipals::contains);
    }


    public boolean isActiveWorkflowActionParticipant(WorkflowModel workflow)
    {
        List<WorkflowActionModel> activeActions = (List<WorkflowActionModel>)workflow.getActions().stream().filter(action -> Arrays.<WorkflowActionStatus>asList(new WorkflowActionStatus[] {WorkflowActionStatus.IN_PROGRESS, WorkflowActionStatus.PAUSED}).contains(action.getStatus()))
                        .collect(Collectors.toList());
        return activeActions.stream().anyMatch(this::isWorkflowActionParticipant);
    }


    public boolean isWorkflowActionParticipant(WorkflowActionModel workflowAction)
    {
        UserModel currentUser = getUserService().getCurrentUser();
        Collection<PrincipalModel> currentPrincipals = getRelatedPrincipals((PrincipalModel)currentUser);
        PrincipalModel visibleForPrincipal = workflowAction.getPrincipalAssigned();
        return currentPrincipals.contains(visibleForPrincipal);
    }


    public boolean isParticipantForAttachedItems(List<? extends CMSItemModel> cmsItems)
    {
        return getCmsWorkflowService()
                        .findAllWorkflowsByAttachedItems(cmsItems, Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES).stream()
                        .allMatch(this::isWorkflowParticipant);
    }


    public boolean isActiveWorkflowActionParticipantForAttachmentItems(List<? extends CMSItemModel> cmsItems)
    {
        return getCmsWorkflowService()
                        .findAllWorkflowsByAttachedItems(cmsItems, Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES).stream()
                        .allMatch(this::isActiveWorkflowActionParticipant);
    }


    public Collection<PrincipalModel> getRelatedPrincipals(PrincipalModel user)
    {
        Collection<PrincipalModel> principals = new HashSet<>();
        principals.add(user);
        principals.addAll(user.getAllGroups());
        return principals;
    }


    protected CMSWorkflowService getCmsWorkflowService()
    {
        return this.cmsWorkflowService;
    }


    @Required
    public void setCmsWorkflowService(CMSWorkflowService cmsWorkflowService)
    {
        this.cmsWorkflowService = cmsWorkflowService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
