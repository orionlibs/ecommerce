package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.Collection;
import java.util.Set;

public interface CMSWorkflowActionDao
{
    SearchResult<WorkflowActionModel> findAllActiveWorkflowActionsByStatusAndPrincipals(Set<CronJobStatus> paramSet, Collection<PrincipalModel> paramCollection, PageableData paramPageableData);
}
