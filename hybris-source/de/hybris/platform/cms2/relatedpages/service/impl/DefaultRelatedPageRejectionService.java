package de.hybris.platform.cms2.relatedpages.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cms2.relatedpages.service.RelatedPageRejectionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRelatedPageRejectionService implements RelatedPageRejectionService
{
    private RelatedItemsService relatedItemsService;
    private ModelService modelService;
    private Predicate<ItemModel> pageTypePredicate;
    private CMSWorkflowService cmsWorkflowService;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private CMSAdminSiteService cmsAdminSiteService;


    public void rejectAllRelatedPages(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        if(getPageTypePredicate().test(itemModel) && (
                        hasUserChangedApprovalStatus((AbstractPageModel)itemModel) || !shouldRejectPage((AbstractPageModel)itemModel)))
        {
            return;
        }
        List<CMSItemModel> relatedItems = getRelatedItemsService().getRelatedItems(itemModel, interceptorContext);
        Objects.requireNonNull(AbstractPageModel.class);
        Objects.requireNonNull(getModelService());
        relatedItems.stream().filter(item -> Objects.nonNull(item.getPk())).filter(item -> !item.equals(itemModel)).filter(getPageTypePredicate()).map(AbstractPageModel.class::cast).filter(this::shouldRejectPage).map(this::rejectPage).forEach(getModelService()::save);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public void rejectAllRelatedPages(ItemModel itemModel)
    {
        if(getPageTypePredicate().test(itemModel) && (
                        hasUserChangedApprovalStatus((AbstractPageModel)itemModel) || !shouldRejectPage((AbstractPageModel)itemModel)))
        {
            return;
        }
        List<CMSItemModel> relatedItems = getRelatedItemsService().getRelatedItems(itemModel);
        Objects.requireNonNull(AbstractPageModel.class);
        Objects.requireNonNull(getModelService());
        relatedItems.stream().filter(item -> Objects.nonNull(item.getPk())).filter(item -> !item.equals(itemModel)).filter(getPageTypePredicate()).map(AbstractPageModel.class::cast).filter(this::shouldRejectPage).map(this::rejectPage).forEach(getModelService()::save);
    }


    protected boolean shouldRejectPage(AbstractPageModel page)
    {
        return (!isRestoredPageVersion(page) && !isOriginalItem(page) && notInActiveWorkflow(page));
    }


    protected boolean notInActiveWorkflow(AbstractPageModel page)
    {
        return !getCmsWorkflowService().isAnyItemInWorkflow(Collections.singletonList(page));
    }


    protected boolean isRestoredPageVersion(AbstractPageModel page)
    {
        return getCmsVersionSessionContextProvider().getAllGeneratedItemsFromCached().containsValue(page);
    }


    protected boolean isOriginalItem(AbstractPageModel page)
    {
        if(Objects.isNull(getCmsAdminSiteService().getOriginalItemContext()))
        {
            return false;
        }
        String itemUid = (String)getCmsAdminSiteService().getOriginalItemContext().get("uid");
        CatalogVersionModel itemCatalogVersion = (CatalogVersionModel)getCmsAdminSiteService().getOriginalItemContext().get("catalogVersion");
        return (Stream.<Serializable>of(new Serializable[] {itemUid, (Serializable)itemCatalogVersion}).allMatch(Objects::nonNull) && page
                        .getUid().equals(itemUid) && page.getCatalogVersion().equals(itemCatalogVersion));
    }


    public boolean hasUserChangedApprovalStatus(AbstractPageModel page)
    {
        return (page.getApprovalStatus() != getOriginalApprovalStatus(page));
    }


    protected CmsApprovalStatus getOriginalApprovalStatus(AbstractPageModel page)
    {
        ItemModelInternalContext context = (ItemModelInternalContext)page.getItemModelContext();
        return (CmsApprovalStatus)context.loadOriginalValue("approvalStatus");
    }


    public AbstractPageModel rejectPage(AbstractPageModel page)
    {
        page.setApprovalStatus(CmsApprovalStatus.CHECK);
        return page;
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected Predicate<ItemModel> getPageTypePredicate()
    {
        return this.pageTypePredicate;
    }


    @Required
    public void setPageTypePredicate(Predicate<ItemModel> pageTypePredicate)
    {
        this.pageTypePredicate = pageTypePredicate;
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


    protected CMSVersionSessionContextProvider getCmsVersionSessionContextProvider()
    {
        return this.cmsVersionSessionContextProvider;
    }


    @Required
    public void setCmsVersionSessionContextProvider(CMSVersionSessionContextProvider cmsVersionSessionContextProvider)
    {
        this.cmsVersionSessionContextProvider = cmsVersionSessionContextProvider;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }
}
