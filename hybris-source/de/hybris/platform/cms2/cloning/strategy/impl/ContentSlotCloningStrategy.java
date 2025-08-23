package de.hybris.platform.cms2.cloning.strategy.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.CMSItemCloningService;
import de.hybris.platform.cms2.cloning.strategy.CMSCloningStrategy;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.enums.CloneAction;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class ContentSlotCloningStrategy implements CMSCloningStrategy<ContentSlotModel>
{
    private SessionSearchRestrictionsDisabler cmsSessionSearchRestrictionsDisabler;
    private CatalogVersionService catalogVersionService;
    private CMSItemCloningService cmsItemCloningService;
    private CMSAdminContentSlotService cmsAdminContentSlotService;
    private ModelService modelService;
    private CMSContentSlotService cmsContentSlotService;


    public ContentSlotModel clone(ContentSlotModel sourceContentSlotModel, Optional<ContentSlotModel> template, Optional<Map<String, Object>> context) throws CMSItemNotFoundException
    {
        Preconditions.checkArgument(template.isPresent(), "Template is required to perform this operation");
        ContentSlotModel clonedContentSlotModel = template.get();
        Preconditions.checkArgument(context.isPresent(), "Context is required to perform this operation");
        Map<String, Object> contextMap = context.get();
        Preconditions.checkArgument(contextMap.containsKey("page"), "Page is required in the context to perform this operation");
        AbstractPageModel sourcePage = (AbstractPageModel)contextMap.get("page");
        Boolean isSlotCustom = (Boolean)contextMap.get("isSlotCustom");
        CloneAction cloneAction = (CloneAction)contextMap.get("cloneAction");
        return (ContentSlotModel)getCmsSessionSearchRestrictionsDisabler().execute(() -> {
            CatalogVersionModel targetCatalogVersionModel = (CatalogVersionModel)getCatalogVersionService().getSessionCatalogVersions().stream().findFirst().orElseThrow(());
            String position = getCmsAdminContentSlotService().getContentSlotPosition(sourcePage, sourceContentSlotModel);
            Preconditions.checkArgument((position != null), "ContentSlot relation was not found for the page or template");
            if(isSlotCustom.booleanValue())
            {
                Preconditions.checkArgument(customPositionIsAvailable(sourcePage, position), "Current position is already taken by another custom slot");
                createContentSlotForPageRelation(targetCatalogVersionModel, position, sourcePage, clonedContentSlotModel);
            }
            else
            {
                createContentSlotForTemplateRelation(targetCatalogVersionModel, position, sourcePage.getMasterTemplate(), clonedContentSlotModel);
            }
            clonedContentSlotModel.setActive(sourceContentSlotModel.getActive());
            clonedContentSlotModel.setActiveFrom(sourceContentSlotModel.getActiveFrom());
            clonedContentSlotModel.setActiveUntil(sourceContentSlotModel.getActiveUntil());
            clonedContentSlotModel.setCatalogVersion(targetCatalogVersionModel);
            if(cloneAction.equals(CloneAction.CLONE))
            {
                getCmsItemCloningService().cloneContentSlotComponents(sourceContentSlotModel, clonedContentSlotModel, targetCatalogVersionModel);
            }
            else if(cloneAction.equals(CloneAction.REMOVE))
            {
                clonedContentSlotModel.setCmsComponents(Collections.emptyList());
            }
            else if(cloneAction.equals(CloneAction.REFERENCE))
            {
                clonedContentSlotModel.setCmsComponents(sourceContentSlotModel.getCmsComponents());
            }
            return clonedContentSlotModel;
        });
    }


    protected boolean customPositionIsAvailable(AbstractPageModel sourcePage, String targetPosition)
    {
        List<String> definedContentSlotPositions = getCmsContentSlotService().getDefinedContentSlotPositions(sourcePage);
        return definedContentSlotPositions.stream().noneMatch(definedPosition -> definedPosition.equals(targetPosition));
    }


    protected void createContentSlotForPageRelation(CatalogVersionModel catalogVersionModel, String position, AbstractPageModel pageModel, ContentSlotModel contentSlotModel)
    {
        ContentSlotForPageModel relation = (ContentSlotForPageModel)getModelService().create(ContentSlotForPageModel.class);
        relation.setCatalogVersion(catalogVersionModel);
        relation.setPosition(position);
        relation.setPage(pageModel);
        relation.setContentSlot(contentSlotModel);
    }


    protected void createContentSlotForTemplateRelation(CatalogVersionModel catalogVersion, String position, PageTemplateModel pageTemplate, ContentSlotModel contentSlotModel)
    {
        ContentSlotForTemplateModel relation = (ContentSlotForTemplateModel)getModelService().create(ContentSlotForTemplateModel.class);
        relation.setCatalogVersion(catalogVersion);
        relation.setPosition(position);
        relation.setPageTemplate(pageTemplate);
        relation.setContentSlot(contentSlotModel);
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


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CMSAdminContentSlotService getCmsAdminContentSlotService()
    {
        return this.cmsAdminContentSlotService;
    }


    @Required
    public void setCmsAdminContentSlotService(CMSAdminContentSlotService cmsAdminContentSlotService)
    {
        this.cmsAdminContentSlotService = cmsAdminContentSlotService;
    }


    public CMSContentSlotService getCmsContentSlotService()
    {
        return this.cmsContentSlotService;
    }


    @Required
    public void setCmsContentSlotService(CMSContentSlotService cmsContentSlotService)
    {
        this.cmsContentSlotService = cmsContentSlotService;
    }
}
