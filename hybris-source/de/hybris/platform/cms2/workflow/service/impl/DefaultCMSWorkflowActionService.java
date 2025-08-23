package de.hybris.platform.cms2.workflow.service.impl;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowActionDao;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowActionService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.impl.DefaultWorkflowActionService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSWorkflowActionService extends DefaultWorkflowActionService implements CMSWorkflowActionService
{
    private CMSWorkflowActionDao cmsWorkflowActionDao;


    public WorkflowActionModel getWorkflowActionForCode(WorkflowModel workflow, String actionCode)
    {
        return (WorkflowActionModel)workflow.getActions()
                        .stream()
                        .filter(action -> action.getCode().equalsIgnoreCase(actionCode))
                        .findFirst()
                        .orElseThrow(() -> new UnknownIdentifierException("No workflow action item found for code '" + actionCode + "'"));
    }


    public WorkflowDecisionModel getActionDecisionForCode(WorkflowActionModel action, String decisionCode)
    {
        return (WorkflowDecisionModel)action.getDecisions()
                        .stream()
                        .filter(decision -> decision.getCode().equalsIgnoreCase(decisionCode))
                        .findFirst()
                        .orElseThrow(() -> new UnknownIdentifierException("No action decision item found for code '" + decisionCode + "'"));
    }


    public SearchResult<WorkflowActionModel> findAllActiveWorkflowActionsByStatusAndPrincipals(Set<CronJobStatus> workflowStatuses, Collection<PrincipalModel> currentPrincipals, PageableData pageableData)
    {
        return getCmsWorkflowActionDao().findAllActiveWorkflowActionsByStatusAndPrincipals(workflowStatuses, currentPrincipals, pageableData);
    }


    protected CMSWorkflowActionDao getCmsWorkflowActionDao()
    {
        return this.cmsWorkflowActionDao;
    }


    @Required
    public void setCmsWorkflowActionDao(CMSWorkflowActionDao cmsWorkflowActionDao)
    {
        this.cmsWorkflowActionDao = cmsWorkflowActionDao;
    }
}
