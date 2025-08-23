package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSTypeRestrictionDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminTypeRestrictionsService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSAdminTypeRestrictionsService extends AbstractCMSAdminService implements CMSAdminTypeRestrictionsService
{
    private CMSContentSlotDao cmsContentSlotDao;
    private CMSAdminSiteService cmsAdminSiteService;
    private CMSTypeRestrictionDao cmsTypeRestrictionDao;


    public Set<CMSComponentTypeModel> getTypeRestrictionsContentSlotForTemplate(PageTemplateModel pageTemplate, ContentSlotModel contentSlot) throws CMSItemNotFoundException
    {
        Set<CMSComponentTypeModel> result = Sets.newHashSet();
        Optional<ContentSlotForTemplateModel> contentSlotForTemplate = getCmsContentSlotDao().findContentSlotRelationsByPageTemplateAndContentSlot(pageTemplate, contentSlot, contentSlot.getCatalogVersion()).stream().findFirst();
        if(contentSlotForTemplate.isPresent())
        {
            result = getMergedTypeRestrictionForContentSlot(pageTemplate.getAvailableContentSlots(), ((ContentSlotForTemplateModel)contentSlotForTemplate
                            .get()).getPosition());
        }
        return result;
    }


    public Set<CMSComponentTypeModel> getTypeRestrictionsContentSlotForPage(AbstractPageModel page, ContentSlotModel contentSlot) throws CMSItemNotFoundException
    {
        Set<CMSComponentTypeModel> result = Sets.newHashSet();
        Optional<ContentSlotForPageModel> contentSlotsForPage = getCmsContentSlotDao().findContentSlotRelationsByPageAndContentSlot(page, contentSlot, contentSlot.getCatalogVersion()).stream().findFirst();
        if(contentSlotsForPage.isPresent())
        {
            result = getMergedTypeRestrictionForContentSlot(page.getMasterTemplate().getAvailableContentSlots(), ((ContentSlotForPageModel)contentSlotsForPage
                            .get()).getPosition());
        }
        return result;
    }


    public Set<CMSComponentTypeModel> getTypeRestrictionsForContentSlot(AbstractPageModel page, ContentSlotModel contentSlot) throws CMSItemNotFoundException
    {
        Set<CMSComponentTypeModel> typeRestrictionsContentSlotForTemplate = getTypeRestrictionsContentSlotForTemplate(page
                        .getMasterTemplate(), contentSlot);
        Set<CMSComponentTypeModel> typeRestrictionsContentSlotForPage = getTypeRestrictionsContentSlotForPage(page, contentSlot);
        return (Set<CMSComponentTypeModel>)Sets.union(typeRestrictionsContentSlotForTemplate, typeRestrictionsContentSlotForPage);
    }


    public Set<CMSComponentTypeModel> getTypeRestrictionsForPage(AbstractPageModel page)
    {
        List<CMSComponentTypeModel> typesAllowedInPage = getCmsTypeRestrictionDao().getTypeRestrictionsForPageTemplate(page.getMasterTemplate());
        return new HashSet<>(typesAllowedInPage);
    }


    protected Set<CMSComponentTypeModel> getMergedTypeRestrictionForContentSlot(List<ContentSlotNameModel> availableContentSlots, String contentSlotForNamePosition) throws CMSItemNotFoundException
    {
        ContentSlotNameModel contentSlotName = (ContentSlotNameModel)availableContentSlots.stream().filter(contentSlotNameModel -> contentSlotNameModel.getName().equals(contentSlotForNamePosition)).findFirst()
                        .orElseThrow(() -> new CMSItemNotFoundException("Content slot not found for name \"" + contentSlotForNamePosition + "\""));
        return (Set<CMSComponentTypeModel>)Sets.union(getCmsComponentTypesFromContentSlotNameComponentTypeGroup(contentSlotName),
                        getCmsComponentTypesFromContentSlotNameValidComponentTypes(contentSlotName));
    }


    protected Set<CMSComponentTypeModel> getCmsComponentTypesFromContentSlotNameComponentTypeGroup(ContentSlotNameModel contentSlotName)
    {
        Set<CMSComponentTypeModel> componentTypes = new HashSet<>();
        if(contentSlotName.getCompTypeGroup() != null &&
                        CollectionUtils.isNotEmpty(contentSlotName.getCompTypeGroup().getCmsComponentTypes()))
        {
            componentTypes = contentSlotName.getCompTypeGroup().getCmsComponentTypes();
        }
        return componentTypes;
    }


    protected Set<CMSComponentTypeModel> getCmsComponentTypesFromContentSlotNameValidComponentTypes(ContentSlotNameModel contentSlotName)
    {
        Set<CMSComponentTypeModel> componentTypes = new HashSet<>();
        if(CollectionUtils.isNotEmpty(contentSlotName.getValidComponentTypes()))
        {
            componentTypes = contentSlotName.getValidComponentTypes();
        }
        return componentTypes;
    }


    protected CMSContentSlotDao getCmsContentSlotDao()
    {
        return this.cmsContentSlotDao;
    }


    @Required
    public void setCmsContentSlotDao(CMSContentSlotDao contentSlotDao)
    {
        this.cmsContentSlotDao = contentSlotDao;
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


    public CMSTypeRestrictionDao getCmsTypeRestrictionDao()
    {
        return this.cmsTypeRestrictionDao;
    }


    @Required
    public void setCmsTypeRestrictionDao(CMSTypeRestrictionDao cmsTypeRestrictionDao)
    {
        this.cmsTypeRestrictionDao = cmsTypeRestrictionDao;
    }
}
