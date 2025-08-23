package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CMSWorkflowDao
{
    List<WorkflowModel> findAllWorkflowsByAttachedItems(List<? extends CMSItemModel> paramList, Set<CronJobStatus> paramSet);


    SearchResult<WorkflowModel> findWorkflowsByAttachedItems(List<? extends CMSItemModel> paramList, Set<CronJobStatus> paramSet, PageableData paramPageableData);


    Optional<WorkflowModel> findWorkflowForCode(String paramString);
}
