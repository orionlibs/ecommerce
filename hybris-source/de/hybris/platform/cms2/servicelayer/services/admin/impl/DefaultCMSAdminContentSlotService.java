package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.common.exceptions.PermissionExceptionUtils;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSAdminContentSlotService extends AbstractCMSAdminService implements CMSAdminContentSlotService
{
    private static final Logger log = Logger.getLogger(DefaultCMSAdminContentSlotService.class.getName());
    public static final String DEFAULT_CONTENTSLOT_FOR_PAGE_UID_PREFIX = "contentSlotForPage";
    public static final String DEFAULT_CONTENTSLOT_UID_SUFFIX = "Slot";
    private static final String NAME_CHARACTER_EXCLUSION_REGEXP = "[^a-zA-Z0-9-_]";
    private String contentSlotForPageUidPrefix = "contentSlotForPage";
    private String contentSlotUidSuffix = "Slot";
    private CMSAdminSiteService adminSiteService;
    private ConfigurationService configurationService;
    private CatalogLevelService cmsCatalogLevelService;
    private CMSAdminComponentService cmsAdminComponentService;
    private CMSContentSlotDao cmsContentSlotDao;
    private CMSDataFactory cmsDataFactory;
    private KeyGenerator keyGenerator;
    private PermissionCRUDService permissionCRUDService;
    private CMSCatalogVersionService cmsCatalogVersionService;
    private CMSContentSlotService cmsContentSlotService;
    private CMSPageService cmsPageService;


    public ContentSlotModel createContentSlot(AbstractPageModel page, String id, String name, String position)
    {
        return createContentSlot(page, id, name, position, true);
    }


    public ContentSlotModel createContentSlot(AbstractPageModel page, String id, String name, String position, boolean active)
    {
        return createContentSlot(page, id, name, position, true, null, null);
    }


    public ContentSlotModel createContentSlot(AbstractPageModel page, String id, String name, String position, boolean active, Date activeFrom, Date activeUntil)
    {
        ContentSlotModel contentSlot = (ContentSlotModel)getModelService().create(ContentSlotModel.class);
        contentSlot.setUid(StringUtils.isNotBlank(id) ? id : generateContentSlotUid(page, position));
        contentSlot.setName(name);
        contentSlot.setActive(Boolean.valueOf(active));
        contentSlot.setActiveFrom(activeFrom);
        contentSlot.setActiveUntil(activeUntil);
        contentSlot.setCatalogVersion(page.getCatalogVersion());
        ContentSlotForPageModel relation = (ContentSlotForPageModel)getModelService().create(ContentSlotForPageModel.class);
        relation.setUid(generateContentSlotForPageUID());
        relation.setCatalogVersion(page.getCatalogVersion());
        relation.setPosition(position);
        relation.setPage(page);
        relation.setContentSlot(contentSlot);
        return contentSlot;
    }


    protected String generateContentSlotUid(AbstractPageModel page, String position)
    {
        return String.format("%s%s-%s", new Object[] {position, getContentSlotUidSuffix(), page
                        .getUid().replaceAll("[^a-zA-Z0-9-_]", "-")});
    }


    protected String generateContentSlotForPageUID()
    {
        return StringUtils.isNotBlank(getContentSlotForPageUidPrefix()) ?
                        String.format("%s-%s", new Object[] {getContentSlotForPageUidPrefix(), getKeyGenerator().generate().toString()}) : getKeyGenerator().generate().toString();
    }


    public void deleteContentSlot(String contentSlotId) throws CMSItemNotFoundException
    {
        ContentSlotModel contentSlot = getContentSlotForId(contentSlotId);
        for(ContentSlotForPageModel csfp : getCmsContentSlotDao().findAllContentSlotRelationsByContentSlot(contentSlot,
                        getActiveCatalogVersion()))
        {
            getModelService().remove(csfp);
        }
        getModelService().remove(contentSlot);
    }


    public void deleteRelation(AbstractPageModel page, ContentSlotModel contentSlot)
    {
        List<ContentSlotForPageModel> csfp = getCmsContentSlotDao().findContentSlotRelationsByPageAndContentSlot(page, contentSlot,
                        getActiveCatalogVersion());
        if(csfp.isEmpty())
        {
            log.warn("Could not find relation between contentslot [" + contentSlot
                            .getName() + "] and page [" + page.getName() + "]");
        }
        else
        {
            getModelService().remove(csfp.get(0));
        }
    }


    public void deleteRelationByPosition(AbstractPageModel page, String position)
    {
        List<ContentSlotForPageModel> csfp = getCmsContentSlotDao().findContentSlotRelationsByPageAndPosition(page, position,
                        getActiveCatalogVersion());
        if(csfp.isEmpty())
        {
            log.warn("Could not find relation at position [" + position + "] for page [" + page.getName() + "]");
        }
        else
        {
            getModelService().remove(csfp.get(0));
        }
    }


    public Collection<CMSRelationModel> getAllRelationsForSlot(ContentSlotModel contentSlot)
    {
        return getCmsContentSlotDao().findAllContentSlotRelationsByContentSlot(contentSlot);
    }


    public Collection<CMSRelationModel> getOnlyContentSlotRelationsForSlot(ContentSlotModel contentSlot)
    {
        return getCmsContentSlotDao().findOnlyContentSlotRelationsByContentSlot(contentSlot);
    }


    public ContentSlotModel getContentSlotForId(String contentSlotId) throws UnknownIdentifierException, AmbiguousIdentifierException
    {
        List<ContentSlotModel> contentSlots = getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(contentSlotId,
                        Collections.singletonList(getActiveCatalogVersion()));
        if(contentSlots.isEmpty())
        {
            throw new UnknownIdentifierException("Could not find content slot with id [" + contentSlotId + "]");
        }
        if(contentSlots.size() > 1)
        {
            throw new AmbiguousIdentifierException("Content slot id '" + contentSlotId + "' is not unique, " + contentSlots
                            .size() + " content slots found!");
        }
        return contentSlots.get(0);
    }


    public List<ContentSlotModel> getContentSlots(List<String> requestedContentSlotIds) throws UnknownIdentifierException, AmbiguousIdentifierException
    {
        List<ContentSlotModel> contentSlots = getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(requestedContentSlotIds,
                        Collections.singletonList(getActiveCatalogVersion()));
        List<String> slotIdsFound = (List<String>)contentSlots.stream().map(CMSItemModel::getUid).distinct().collect(Collectors.toList());
        requestedContentSlotIds.stream()
                        .filter(id -> !slotIdsFound.contains(id))
                        .findAny()
                        .ifPresent(csId -> {
                            throw new UnknownIdentifierException("Could not find all requested content slots.");
                        });
        if(contentSlots.size() != requestedContentSlotIds.size())
        {
            throw new AmbiguousIdentifierException("One or more of the given content slot IDs is not unique.");
        }
        return contentSlots;
    }


    public ContentSlotModel getContentSlotForIdAndCatalogVersions(String contentSlotId, Collection<CatalogVersionModel> catalogVersions) throws UnknownIdentifierException, AmbiguousIdentifierException
    {
        List<ContentSlotModel> contentSlots = getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(contentSlotId, catalogVersions);
        if(contentSlots.isEmpty())
        {
            throw new UnknownIdentifierException("Could not find content slot with id [" + contentSlotId + "]");
        }
        if(contentSlots.size() > 1)
        {
            throw new AmbiguousIdentifierException("Content slot id '" + contentSlotId + "' is not unique, " + contentSlots
                            .size() + " content slots found!");
        }
        return contentSlots.get(0);
    }


    public Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel page)
    {
        return getContentSlotsForPage(page, true);
    }


    public Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel page, boolean includeMasterTemplateSlots)
    {
        List<ContentSlotData> result = new ArrayList<>();
        PageTemplateModel template = page.getMasterTemplate();
        Set<String> positions = new HashSet<>();
        getCmsContentSlotDao().findAllContentSlotRelationsByPage(page).forEach(pageSlot -> {
            positions.add(pageSlot.getPosition());
            ContentSlotData info = getCmsDataFactory().createContentSlotData(pageSlot);
            info.setAvailableCMSComponentContainers(getCmsAdminComponentService().getAllowedCMSComponents());
            info.setAvailableCMSComponentContainers(getCmsAdminComponentService().getAllowedCMSComponentContainers());
            result.add(info);
        });
        if(includeMasterTemplateSlots)
        {
            List<CatalogVersionModel> catalogVersions = getCmsCatalogVersionService().getFullHierarchyForCatalogVersion(page.getCatalogVersion(), this.adminSiteService.getActiveSite());
            List<ContentSlotModel> addedTemplateSlots = appendTemplateSlots(template, positions, result, page, catalogVersions);
            List<ContentSlotModel> contentSlots = getSortedMultiCountryContentSlots(addedTemplateSlots, catalogVersions, page);
            for(int i = 0; i < result.size(); i++)
            {
                ContentSlotData data = result.get(i);
                ContentSlotModel overrideSlot = getOverrideSlot(contentSlots, data.getContentSlot());
                if(overrideSlot != null)
                {
                    result.set(i, getCmsDataFactory().createContentSlotData(data.getPageId(), overrideSlot, data.getPosition(), data
                                    .isFromMaster(), data.isAllowOverwrite()));
                }
            }
        }
        return result;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public List<ContentSlotForTemplateModel> findAllContentSlotRelationsByPageTemplate(PageTemplateModel template)
    {
        return getCmsContentSlotDao().findAllContentSlotRelationsByPageTemplate(template);
    }


    public List<ContentSlotForPageModel> findAllContentSlotRelationsByPage(AbstractPageModel page)
    {
        return getCmsContentSlotDao().findAllContentSlotRelationsByPage(page);
    }


    protected ContentSlotModel getOverrideSlot(List<ContentSlotModel> contentSlots, ContentSlotModel contentSlot)
    {
        return contentSlots.stream().filter(slot -> contentSlot.equals(slot.getOriginalSlot())).findFirst().orElse(null);
    }


    protected List<ContentSlotModel> appendTemplateSlots(PageTemplateModel pageTemplate, Set<String> positions, List<ContentSlotData> contentSlots, AbstractPageModel page, List<CatalogVersionModel> catalogVersions)
    {
        List<ContentSlotModel> addedSlots = new ArrayList<>();
        List<ContentSlotForTemplateModel> slotForTemplateModels = getCmsPageService().getChildSlotForTemplateAtSamePosition(
                        getCmsContentSlotDao().findContentSlotRelationsByPageTemplateAndCatalogVersions(pageTemplate, catalogVersions));
        slotForTemplateModels.stream()
                        .forEach(templateSlot -> {
                            if(!positions.contains(templateSlot.getPosition()))
                            {
                                positions.add(templateSlot.getPosition());
                                ContentSlotData info = getCmsDataFactory().createContentSlotData(page, templateSlot);
                                info.setAvailableCMSComponents(getCmsAdminComponentService().getAllowedCMSComponents());
                                info.setAvailableCMSComponentContainers(getCmsAdminComponentService().getAllowedCMSComponentContainers());
                                contentSlots.add(info);
                                addedSlots.add(info.getContentSlot());
                            }
                            else
                            {
                                contentSlots.stream().filter(()).findFirst().ifPresent(());
                            }
                        });
        return addedSlots;
    }


    public void updatePositionCMSComponentInContentSlot(AbstractCMSComponentModel component, ContentSlotModel slot, Integer index)
    {
        Preconditions.checkArgument((index != null), "Index cannot be null.");
        Preconditions.checkArgument((index.intValue() >= 0), "Index must be greater than or equal to zero.");
        if(!getPermissionCRUDService().canChangeType(slot.getItemtype()))
        {
            throwTypePermissionException("change", slot.getItemtype());
        }
        List<AbstractCMSComponentModel> currentComponentList = new ArrayList<>();
        currentComponentList.addAll(slot.getCmsComponents());
        int currentIndex = currentComponentList.indexOf(component);
        currentComponentList.remove(currentIndex);
        addComponentToComponentListSaveAsSlot(component, slot, index, currentComponentList);
    }


    public void addCMSComponentToContentSlot(AbstractCMSComponentModel component, ContentSlotModel slot, Integer index)
    {
        Preconditions.checkArgument((index != null), "Index cannot be null.");
        Preconditions.checkArgument((index.intValue() >= 0), "Index must be greater than or equal to zero.");
        if(!getPermissionCRUDService().canChangeType(slot.getItemtype()))
        {
            throwTypePermissionException("change", slot.getItemtype());
        }
        List<AbstractCMSComponentModel> currentComponentList = new ArrayList<>();
        currentComponentList.addAll(slot.getCmsComponents());
        addComponentToComponentListSaveAsSlot(component, slot, index, currentComponentList);
    }


    protected void addComponentToComponentListSaveAsSlot(AbstractCMSComponentModel component, ContentSlotModel slot, Integer index, List<AbstractCMSComponentModel> currentComponentList)
    {
        int position = (index.intValue() >= currentComponentList.size()) ? currentComponentList.size() : index.intValue();
        currentComponentList.add(position, component);
        slot.setCmsComponents(currentComponentList);
        getModelService().save(slot);
        if(getCmsContentSlotService().isSharedSlot(slot) && component.isSynchronizationBlocked())
        {
            component.setSynchronizationBlocked(false);
            getModelService().save(component);
        }
    }


    public boolean hasOtherRelations(AbstractPageModel page, ContentSlotModel contentSlot)
    {
        Collection<ContentSlotForPageModel> relations = getCmsContentSlotDao().findAllContentSlotRelationsByContentSlot(contentSlot, getActiveCatalogVersion());
        for(ContentSlotForPageModel relation : relations)
        {
            if(!page.getUid().equals(relation.getPage().getUid()))
            {
                return true;
            }
        }
        return false;
    }


    public boolean hasRelations(ContentSlotModel contentSlot)
    {
        Collection<ContentSlotForPageModel> relations = getCmsContentSlotDao().findAllContentSlotRelationsByContentSlot(contentSlot, getActiveCatalogVersion());
        return (relations != null && !relations.isEmpty());
    }


    public Collection<ContentSlotModel> getContentSlotsForCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return getCmsContentSlotDao().findContentSlotsForCatalogVersion(catalogVersion);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> contentSlots, List<CatalogVersionModel> catalogVersions)
    {
        List<ContentSlotModel> list = null;
        if(CollectionUtils.isNotEmpty(contentSlots))
        {
            list = getCmsContentSlotDao().findAllMultiCountryContentSlotsByOriginalSlots(contentSlots, catalogVersions);
        }
        return CollectionUtils.isNotEmpty(list) ? getCmsCatalogLevelService().getSortedMultiCountryContentSlots(list) :
                        Collections.<ContentSlotModel>emptyList();
    }


    public List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> contentSlots, List<CatalogVersionModel> catalogVersions, AbstractPageModel page)
    {
        List<ContentSlotModel> list = null;
        if(CollectionUtils.isNotEmpty(contentSlots))
        {
            list = getCmsContentSlotDao().findAllMultiCountryContentSlotsByOriginalSlots(contentSlots, catalogVersions, page);
        }
        return CollectionUtils.isNotEmpty(list) ? getCmsCatalogLevelService().getSortedMultiCountryContentSlots(list) :
                        Collections.<ContentSlotModel>emptyList();
    }


    public ContentSlotModel getContentSlotOverride(AbstractPageModel page, ContentSlotModel contentSlot)
    {
        List<CatalogVersionModel> catalogVersions = Collections.singletonList(page.getCatalogVersion());
        List<ContentSlotModel> contentSlots = Collections.singletonList(contentSlot);
        List<ContentSlotModel> overrideSlots = getCmsContentSlotDao().findAllMultiCountryContentSlotsByOriginalSlots(contentSlots, catalogVersions);
        if(CollectionUtils.isNotEmpty(overrideSlots))
        {
            return overrideSlots.get(0);
        }
        return null;
    }


    public List<ContentSlotForPageModel> getContentSlotRelationsByPageId(String pageId, CatalogVersionModel catalogVersion)
    {
        return getCmsContentSlotDao().findAllContentSlotRelationsByPageUid(pageId, catalogVersion);
    }


    public String getContentSlotPosition(AbstractPageModel page, ContentSlotModel contentSlot)
    {
        List<CatalogVersionModel> catalogVersions = getCmsCatalogVersionService().getFullHierarchyForCatalogVersion(page.getCatalogVersion(), this.adminSiteService.getActiveSite());
        return getCmsContentSlotDao()
                        .findContentSlotRelationsByPageAndContentSlot(page, contentSlot, catalogVersions)
                        .stream()
                        .map(ContentSlotForPageModel::getPosition)
                        .findFirst()
                        .orElseGet(() -> (String)getCmsContentSlotDao().findContentSlotRelationsByPageTemplateAndCatalogVersionsAndContentSlot(page.getMasterTemplate(), contentSlot, catalogVersions).stream().map(ContentSlotForTemplateModel::getPosition).findFirst().orElse(null));
    }


    public List<CMSRelationModel> getAllDeletedRelationsForPage(CatalogVersionModel targetCatalogVersion, AbstractPageModel sourcePage)
    {
        return getCmsContentSlotDao().getAllDeletedRelationsForPage(targetCatalogVersion, sourcePage);
    }


    protected void throwTypePermissionException(String permissionName, String typeCode)
    {
        throw PermissionExceptionUtils.createTypePermissionException(permissionName, typeCode);
    }


    @Required
    public void setCmsAdminComponentService(CMSAdminComponentService cmsAdminComponentService)
    {
        this.cmsAdminComponentService = cmsAdminComponentService;
    }


    @Required
    public void setCmsContentSlotDao(CMSContentSlotDao cmsContentSlotDao)
    {
        this.cmsContentSlotDao = cmsContentSlotDao;
    }


    @Required
    public void setCmsDataFactory(CMSDataFactory cmsDataFactory)
    {
        this.cmsDataFactory = cmsDataFactory;
    }


    protected CMSAdminComponentService getCmsAdminComponentService()
    {
        return this.cmsAdminComponentService;
    }


    protected CMSContentSlotDao getCmsContentSlotDao()
    {
        return this.cmsContentSlotDao;
    }


    protected CMSDataFactory getCmsDataFactory()
    {
        return this.cmsDataFactory;
    }


    public KeyGenerator getKeyGenerator()
    {
        return this.keyGenerator;
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    protected CMSAdminSiteService getAdminSiteService()
    {
        return this.adminSiteService;
    }


    @Required
    public void setAdminSiteService(CMSAdminSiteService adminSiteService)
    {
        this.adminSiteService = adminSiteService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected CatalogLevelService getCmsCatalogLevelService()
    {
        return this.cmsCatalogLevelService;
    }


    @Required
    public void setCmsCatalogLevelService(CatalogLevelService cmsCatalogLevelService)
    {
        this.cmsCatalogLevelService = cmsCatalogLevelService;
    }


    public String getContentSlotForPageUidPrefix()
    {
        return this.contentSlotForPageUidPrefix;
    }


    public void setContentSlotForPageUidPrefix(String contentSlotForPageUidPrefix)
    {
        this.contentSlotForPageUidPrefix = contentSlotForPageUidPrefix;
    }


    public String getContentSlotUidSuffix()
    {
        return this.contentSlotUidSuffix;
    }


    public void setContentSlotUidSuffix(String contentSlotUidSuffix)
    {
        this.contentSlotUidSuffix = contentSlotUidSuffix;
    }


    protected PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    public CMSCatalogVersionService getCmsCatalogVersionService()
    {
        return this.cmsCatalogVersionService;
    }


    @Required
    public void setCmsCatalogVersionService(CMSCatalogVersionService cmsCatalogVersionService)
    {
        this.cmsCatalogVersionService = cmsCatalogVersionService;
    }


    protected CMSContentSlotService getCmsContentSlotService()
    {
        return this.cmsContentSlotService;
    }


    @Required
    public void setCmsContentSlotService(CMSContentSlotService cmsContentSlotService)
    {
        this.cmsContentSlotService = cmsContentSlotService;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    @Required
    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }
}
