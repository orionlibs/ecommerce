package de.hybris.platform.cms2.model;

import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.SyncItemInfo;
import de.hybris.platform.catalog.synchronization.SynchronizationStatusService;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsItemDisplayStatus;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class AbstractPageDynamicDisplayStatusAttributeHandler implements DynamicAttributeHandler<CmsItemDisplayStatus, AbstractPageModel>
{
    private CMSWorkflowService cmsWorkflowService;
    private SynchronizationStatusService synchronizationStatusService;


    public CmsItemDisplayStatus get(AbstractPageModel page)
    {
        CmsApprovalStatus pageApprovalStatus = page.getApprovalStatus();
        CmsItemDisplayStatus pageDisplayStatus = null;
        if(CmsApprovalStatus.CHECK == pageApprovalStatus)
        {
            boolean isPageInWorfklow = (Objects.nonNull(page.getPk()) && this.cmsWorkflowService.isAnyItemInWorkflow(Collections.singletonList(page)));
            pageDisplayStatus = isPageInWorfklow ? CmsItemDisplayStatus.IN_PROGRESS : CmsItemDisplayStatus.DRAFT;
        }
        else if(CmsApprovalStatus.APPROVED == pageApprovalStatus)
        {
            boolean isPageSynced = (Objects.nonNull(page.getPk()) && SyncItemStatus.IN_SYNC == getSynchronizationItemStatus((CMSItemModel)page));
            pageDisplayStatus = isPageSynced ? CmsItemDisplayStatus.SYNCED : CmsItemDisplayStatus.READY_TO_SYNC;
        }
        return pageDisplayStatus;
    }


    protected SyncItemStatus getSynchronizationItemStatus(CMSItemModel item)
    {
        CatalogVersionModel sourceVersion = item.getCatalogVersion();
        CatalogVersionModel targetVersion = sourceVersion.getCatalog().getActiveCatalogVersion();
        List<SyncItemJobModel> syncJobs = getSynchronizationStatusService().getOutboundSynchronizations((ItemModel)item);
        SyncItemJobModel syncItemJobModel = syncJobs.stream().filter(job -> job.getSourceVersion().getVersion().equals(sourceVersion.getVersion())).filter(job -> job.getTargetVersion().getVersion().equals(targetVersion.getVersion())).findFirst().orElse(null);
        if(Objects.nonNull(syncItemJobModel))
        {
            SyncItemInfo syncInfo = getSynchronizationStatusService().getSyncInfo((ItemModel)item, syncItemJobModel);
            return syncInfo.getSyncStatus();
        }
        return SyncItemStatus.NOT_APPLICABLE;
    }


    public void set(AbstractPageModel page, CmsItemDisplayStatus displayStatus)
    {
        throw new UnsupportedOperationException();
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


    protected SynchronizationStatusService getSynchronizationStatusService()
    {
        return this.synchronizationStatusService;
    }


    @Required
    public void setSynchronizationStatusService(SynchronizationStatusService synchronizationStatusService)
    {
        this.synchronizationStatusService = synchronizationStatusService;
    }
}
