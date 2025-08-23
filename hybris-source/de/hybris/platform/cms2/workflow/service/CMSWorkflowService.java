package de.hybris.platform.cms2.workflow.service;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CMSWorkflowService extends WorkflowService
{
    boolean isAnyItemInWorkflow(List<? extends CMSItemModel> paramList);


    SearchResult<WorkflowModel> findWorkflowsForAttachedItemsAndStatuses(List<? extends CMSItemModel> paramList, Set<CronJobStatus> paramSet, PageableData paramPageableData);


    List<WorkflowModel> findAllWorkflowsByAttachedItems(List<? extends CMSItemModel> paramList, Set<CronJobStatus> paramSet);


    List<WorkflowModel> getRelatedWorkflowsForItem(CMSItemModel paramCMSItemModel, Set<CronJobStatus> paramSet);


    boolean isItemEditableBySessionUser(CMSItemModel paramCMSItemModel);


    Optional<WorkflowModel> getWorkflowWhereItemEditable(CMSItemModel paramCMSItemModel);
}
