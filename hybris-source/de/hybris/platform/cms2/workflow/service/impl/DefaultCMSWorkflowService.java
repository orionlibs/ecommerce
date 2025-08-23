package de.hybris.platform.cms2.workflow.service.impl;

import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowDao;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.impl.DefaultWorkflowService;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSWorkflowService extends DefaultWorkflowService implements CMSWorkflowService
{
    private CMSWorkflowDao cmsWorkflowDao;
    private RelatedItemsService relatedItemsService;
    private CMSWorkflowParticipantService cmsWorkflowParticipantService;


    public boolean isAnyItemInWorkflow(List<? extends CMSItemModel> itemUids)
    {
        List<WorkflowModel> workflows = findAllWorkflowsByAttachedItems(itemUids, Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES);
        return CollectionUtils.isNotEmpty(workflows);
    }


    public SearchResult<WorkflowModel> findWorkflowsForAttachedItemsAndStatuses(List<? extends CMSItemModel> items, Set<CronJobStatus> statuses, PageableData pageableData)
    {
        return getCmsWorkflowDao().findWorkflowsByAttachedItems(items, statuses, pageableData);
    }


    public List<WorkflowModel> findAllWorkflowsByAttachedItems(List<? extends CMSItemModel> items, Set<CronJobStatus> statuses)
    {
        return getCmsWorkflowDao().findAllWorkflowsByAttachedItems(items, statuses);
    }


    public List<WorkflowModel> getRelatedWorkflowsForItem(CMSItemModel item, Set<CronJobStatus> statuses)
    {
        List<CMSItemModel> relatedItems = getRelatedItemsService().getRelatedItems((ItemModel)item);
        if(relatedItems.isEmpty())
        {
            return Collections.emptyList();
        }
        return (List<WorkflowModel>)findAllWorkflowsByAttachedItems(relatedItems, statuses)
                        .stream()
                        .sorted(Comparator.comparing(ItemModel::getCreationtime))
                        .collect(Collectors.toList());
    }


    public boolean isItemEditableBySessionUser(CMSItemModel item)
    {
        Optional<WorkflowModel> workflowWhereItemEditable = getWorkflowWhereItemEditable(item);
        return ((Boolean)workflowWhereItemEditable
                        .<Boolean>map(workflow -> Boolean.valueOf(getCmsWorkflowParticipantService().isActiveWorkflowActionParticipant(workflow))).orElse(Boolean.valueOf(true))).booleanValue();
    }


    public Optional<WorkflowModel> getWorkflowWhereItemEditable(CMSItemModel item)
    {
        return getRelatedWorkflowsForItem(item, Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES).stream().findFirst();
    }


    protected CMSWorkflowDao getCmsWorkflowDao()
    {
        return this.cmsWorkflowDao;
    }


    @Required
    public void setCmsWorkflowDao(CMSWorkflowDao cmsWorkflowDao)
    {
        this.cmsWorkflowDao = cmsWorkflowDao;
    }


    protected RelatedItemsService getRelatedItemsService()
    {
        return this.relatedItemsService;
    }


    @Required
    public void setRelatedItemsService(RelatedItemsService relatedItemsService)
    {
        this.relatedItemsService = relatedItemsService;
    }


    protected CMSWorkflowParticipantService getCmsWorkflowParticipantService()
    {
        return this.cmsWorkflowParticipantService;
    }


    @Required
    public void setCmsWorkflowParticipantService(CMSWorkflowParticipantService cmsWorkflowParticipantService)
    {
        this.cmsWorkflowParticipantService = cmsWorkflowParticipantService;
    }
}
