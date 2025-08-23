package de.hybris.platform.cms2.workflow.service;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Set;

public interface CMSWorkflowActionService extends WorkflowActionService
{
    WorkflowActionModel getWorkflowActionForCode(WorkflowModel paramWorkflowModel, String paramString);


    WorkflowDecisionModel getActionDecisionForCode(WorkflowActionModel paramWorkflowActionModel, String paramString);


    SearchResult<WorkflowActionModel> findAllActiveWorkflowActionsByStatusAndPrincipals(Set<CronJobStatus> paramSet, Collection<PrincipalModel> paramCollection, PageableData paramPageableData);
}
