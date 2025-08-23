package de.hybris.platform.cms2.cloning.strategy.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.CMSItemCloningService;
import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.cms2.cloning.service.CMSModelCloningContextFactory;
import de.hybris.platform.cms2.cloning.service.impl.CMSModelCloningContext;
import de.hybris.platform.cms2.cloning.strategy.CMSCloningStrategy;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class PageCloningStrategy implements CMSCloningStrategy<AbstractPageModel>
{
    private CMSAdminContentSlotService cmsAdminContentSlotService;
    private ModelService modelService;
    private PlatformTransactionManager transactionManager;
    private SearchRestrictionService searchRestrictionService;
    private SessionService sessionService;
    private CMSItemDeepCloningService cmsItemDeepCloningService;
    private CMSAdminPageService cmsAdminPageService;
    private SessionSearchRestrictionsDisabler cmsSessionSearchRestrictionsDisabler;
    private CMSModelCloningContextFactory cmsModelCloningContextFactory;
    private CMSItemCloningService cmsItemCloningService;


    public AbstractPageModel clone(AbstractPageModel sourcePageModel, Optional<AbstractPageModel> template, Optional<Map<String, Object>> context) throws CMSItemNotFoundException, IllegalArgumentException
    {
        Preconditions.checkArgument((template != null), "Template is required to perform this operation, null given");
        Preconditions.checkArgument(template.isPresent(), "Template is required to perform this operation, empty given");
        Preconditions.checkArgument((context != null), "Context is required to perform this operation, null given");
        return (AbstractPageModel)(new TransactionTemplate(getTransactionManager())).execute(status -> (AbstractPageModel)getCmsSessionSearchRestrictionsDisabler().execute(clonePage(sourcePageModel, template.get(), context.orElse(null))));
    }


    protected Supplier<AbstractPageModel> clonePage(AbstractPageModel sourcePageModel, AbstractPageModel clonedPageModel, Map<String, Object> context)
    {
        return () -> {
            clonedPageModel.setOriginalPage(sourcePageModel);
            clonedPageModel.setLocalizedPages(Collections.emptyList());
            clonedPageModel.setApprovalStatus(CmsApprovalStatus.CHECK);
            AbstractPageModel identicalPrimaryPage = getCmsAdminPageService().getIdenticalPrimaryPageModel(clonedPageModel);
            getCmsAdminContentSlotService().getContentSlotsForPage(sourcePageModel, false).forEach(());
            Optional<MediaModel> previewImage = clonePreviewImage(sourcePageModel, clonedPageModel.getCatalogVersion());
            Objects.requireNonNull(clonedPageModel);
            previewImage.ifPresent(clonedPageModel::setPreviewImage);
            if(identicalPrimaryPage != null && clonedPageModel.getDefaultPage().booleanValue())
            {
                identicalPrimaryPage.setPageStatus(CmsPageStatus.DELETED);
            }
            getModelService().saveAll();
            return clonedPageModel;
        };
    }


    protected Optional<MediaModel> clonePreviewImage(AbstractPageModel sourcePageModel, CatalogVersionModel targetCatalogVersion)
    {
        return Optional.<MediaModel>ofNullable(sourcePageModel.getPreviewImage()).map(sourceMediaModel -> {
            CMSModelCloningContext cloningContext = getCmsModelCloningContextFactory().createCloningContextWithCatalogVersionPredicates(targetCatalogVersion);
            return (MediaModel)getCmsItemDeepCloningService().deepCloneComponent((ItemModel)sourceMediaModel, (ModelCloningContext)cloningContext);
        });
    }


    protected ContentSlotModel cloneAndAddContentSlot(AbstractPageModel pageModel, ContentSlotModel contentSlotModel, boolean shouldCloneComponents)
    {
        ContentSlotModel clonedContentSlot = getCmsAdminContentSlotService().createContentSlot(pageModel, null, contentSlotModel
                        .getName(), contentSlotModel.getCurrentPosition(), contentSlotModel.getActive().booleanValue(), contentSlotModel
                        .getActiveFrom(), contentSlotModel.getActiveUntil());
        if(shouldCloneComponents)
        {
            getCmsItemCloningService().cloneContentSlotComponents(contentSlotModel, clonedContentSlot, clonedContentSlot
                            .getCatalogVersion());
        }
        else
        {
            clonedContentSlot.setCmsComponents(new ArrayList(contentSlotModel.getCmsComponents()));
        }
        return clonedContentSlot;
    }


    @Required
    public void setCmsAdminContentSlotService(CMSAdminContentSlotService cmsAdminContentSlotService)
    {
        this.cmsAdminContentSlotService = cmsAdminContentSlotService;
    }


    protected CMSAdminContentSlotService getCmsAdminContentSlotService()
    {
        return this.cmsAdminContentSlotService;
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


    protected PlatformTransactionManager getTransactionManager()
    {
        return this.transactionManager;
    }


    @Required
    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }


    protected SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected CMSItemDeepCloningService getCmsItemDeepCloningService()
    {
        return this.cmsItemDeepCloningService;
    }


    @Required
    public void setCmsItemDeepCloningService(CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }


    protected CMSAdminPageService getCmsAdminPageService()
    {
        return this.cmsAdminPageService;
    }


    @Required
    public void setCmsAdminPageService(CMSAdminPageService cmsAdminPageService)
    {
        this.cmsAdminPageService = cmsAdminPageService;
    }


    protected SessionSearchRestrictionsDisabler getCmsSessionSearchRestrictionsDisabler()
    {
        return this.cmsSessionSearchRestrictionsDisabler;
    }


    @Required
    public void setCmsSessionSearchRestrictionsDisabler(SessionSearchRestrictionsDisabler cmsSessionSearchRestrictionsDisabler)
    {
        this.cmsSessionSearchRestrictionsDisabler = cmsSessionSearchRestrictionsDisabler;
    }


    protected CMSModelCloningContextFactory getCmsModelCloningContextFactory()
    {
        return this.cmsModelCloningContextFactory;
    }


    @Required
    public void setCmsModelCloningContextFactory(CMSModelCloningContextFactory cmsModelCloningContextFactory)
    {
        this.cmsModelCloningContextFactory = cmsModelCloningContextFactory;
    }


    protected CMSItemCloningService getCmsItemCloningService()
    {
        return this.cmsItemCloningService;
    }


    @Required
    public void setCmsItemCloningService(CMSItemCloningService cmsItemCloningService)
    {
        this.cmsItemCloningService = cmsItemCloningService;
    }
}
