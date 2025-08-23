package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemCreateException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageTemplateDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSAdminPageService extends AbstractCMSAdminService implements CMSAdminPageService
{
    private CatalogVersionService catalogVersionService;
    private CMSPageDao cmsPageDao;
    private CMSPageTemplateDao cmsPageTemplateDao;
    private TypeService typeService;
    private Comparator<CMSItemModel> cmsItemCatalogLevelComparator;
    private CMSCatalogVersionService cmsCatalogVersionService;
    private PermissionCRUDService permissionCRUDService;
    private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;


    public CategoryPageModel createCategoryPage(String id, String name, String masterTemplateId, boolean isDefault) throws CMSItemCreateException
    {
        CategoryPageModel page = (CategoryPageModel)createPage(GeneratedCms2Constants.TC.CATEGORYPAGE, id, name, masterTemplateId);
        page.setDefaultPage(Boolean.valueOf(isDefault));
        getModelService().save(page);
        return page;
    }


    public ContentPageModel createContentPage(String id, String name, String masterTemplateId, String label) throws CMSItemCreateException
    {
        ContentPageModel page = (ContentPageModel)createPage(GeneratedCms2Constants.TC.CONTENTPAGE, id, name, masterTemplateId);
        page.setLabel(label);
        getModelService().save(page);
        return page;
    }


    public AbstractPageModel createPage(String composedType, String id, String name, String masterTemplateId) throws CMSItemCreateException
    {
        AbstractPageModel page = createPageFromType(composedType);
        if(StringUtils.isEmpty(id) || StringUtils.isEmpty(name))
        {
            throw new CMSItemCreateException("id [" + id + "] and/or name [" + name + "] must not be empty");
        }
        try
        {
            if(pageExists(id))
            {
                throw new CMSItemCreateException("A page with id [" + id + "] already exists in active catalog version");
            }
            page.setUid(id);
            page.setName(name);
            page.setMasterTemplate(getPageTemplateForIdFromActiveCatalogVersion(masterTemplateId));
            page.setCatalogVersion(getActiveCatalogVersion());
            getModelService().save(page);
            return page;
        }
        catch(Exception e)
        {
            throw new CMSItemCreateException(e);
        }
    }


    public ProductPageModel createProductPage(String id, String name, String masterTemplateId, boolean isDefault) throws CMSItemCreateException
    {
        ProductPageModel page = (ProductPageModel)createPage(GeneratedCms2Constants.TC.PRODUCTPAGE, id, name, masterTemplateId);
        page.setDefaultPage(Boolean.valueOf(isDefault));
        getModelService().save(page);
        return page;
    }


    public void deletePage(String id) throws CMSItemNotFoundException
    {
        AbstractPageModel page = getPageForIdFromActiveCatalogVersion(id);
        getModelService().remove(page);
    }


    public Collection<PageTemplateModel> getAllActivePageTemplates()
    {
        return getAllPageTemplates(true);
    }


    public Collection<ContentPageModel> getAllContentPages(Collection<CatalogVersionModel> catalogVersions)
    {
        return getAllContentPagesForPageStatuses(catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<ContentPageModel> getAllContentPagesForPageStatuses(Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        return CollectionUtils.isEmpty(catalogVersions) ? Collections.<ContentPageModel>emptyList() :
                        (Collection<ContentPageModel>)((Collection)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findAllContentPagesByCatalogVersionsAndPageStatuses(catalogVersions, pageStatuses))).stream()
                                        .sorted((page1, page2) -> getCmsItemCatalogLevelComparator().reversed().compare(page1, page2)).collect(Collectors.toList());
    }


    public Collection<CMSPageTypeModel> getAllPageTypes()
    {
        return (Collection<CMSPageTypeModel>)getTypeService().getComposedTypeForClass(AbstractPageModel.class).getAllSubTypes().stream()
                        .filter(composedType -> getPermissionCRUDService().canReadType(composedType))
                        .map(composedType -> (CMSPageTypeModel)composedType)
                        .collect(Collectors.toList());
    }


    public Optional<CMSPageTypeModel> getPageTypeByCode(String typeCode)
    {
        return getAllPageTypes().stream().filter(model -> model.getCode().equals(typeCode)).findFirst();
    }


    public Collection<AbstractPageModel> getAllPages()
    {
        return getAllPages(getActiveCatalogVersion());
    }


    public Collection<AbstractPageModel> getAllPages(CatalogVersionModel catalogVersion)
    {
        return getAllPagesForCatalogVersionAndPageStatuses(catalogVersion, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<AbstractPageModel> getAllPagesForCatalogVersionAndPageStatuses(CatalogVersionModel catalogVersion, List<CmsPageStatus> pageStatuses)
    {
        return (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findAllPagesByCatalogVersionAndPageStatuses(catalogVersion, pageStatuses));
    }


    public Collection<AbstractPageModel> getAllPagesByType(String type)
    {
        List<AbstractPageModel> result = new ArrayList<>();
        for(AbstractPageModel page : getAllPages())
        {
            if(page.getItemtype().equals(type))
            {
                result.add(page);
            }
        }
        return result;
    }


    public Collection<AbstractPageModel> getAllPagesByType(String type, CatalogVersionModel catalogVersion)
    {
        List<AbstractPageModel> result = new ArrayList<>();
        for(AbstractPageModel page : getAllPages(catalogVersion))
        {
            if(page.getItemtype().equals(type))
            {
                result.add(page);
            }
        }
        return result;
    }


    public Map<String, Collection<AbstractPageModel>> getAllPagesMap()
    {
        Map<String, Collection<AbstractPageModel>> result = new HashMap<>();
        for(AbstractPageModel page : getAllPages())
        {
            String type = page.getItemtype();
            if(result.containsKey(type))
            {
                ((Collection<AbstractPageModel>)result.get(type)).add(page);
                continue;
            }
            List<AbstractPageModel> tmp = new ArrayList<>();
            tmp.add(page);
            result.put(type, tmp);
        }
        return result;
    }


    public Collection<PageTemplateModel> getAllPageTemplates(boolean active)
    {
        return getCmsPageTemplateDao().findAllPageTemplatesByCatalogVersion(getActiveCatalogVersion(), active);
    }


    public Collection<PageTemplateModel> getAllPageTemplates(Collection<CatalogVersionModel> catalogVersions)
    {
        return getCmsPageTemplateDao().findAllPageTemplatesByCatalogVersions(catalogVersions, true);
    }


    public Collection<PageTemplateModel> getAllRestrictedPageTemplates(boolean active, CMSPageTypeModel type)
    {
        List<PageTemplateModel> templates = new ArrayList<>();
        if(type != null)
        {
            Collection<CatalogVersionModel> catalogVersions = getCatalogVersionService().getSessionCatalogVersions();
            templates.addAll(getCmsPageTemplateDao().findAllRestrictedPageTemplatesByCatalogVersion(catalogVersions, active, type));
        }
        if(templates.isEmpty())
        {
            templates.addAll(getAllPageTemplates(active));
        }
        return (Collection<PageTemplateModel>)templates.stream()
                        .filter(template -> getPermissionCRUDService().canReadType(template.getItemtype()))
                        .collect(Collectors.toList());
    }


    public Collection<ContentPageModel> getContentPages(Collection<CatalogVersionModel> catalogVersions, String label)
    {
        return (Collection<ContentPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByLabel(label, catalogVersions));
    }


    public AbstractPageModel getPageForIdFromActiveCatalogVersion(String id)
    {
        return getPageForIdFromActiveCatalogVersionByPageStatuses(id, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public AbstractPageModel getPageForIdFromActiveCatalogVersionByPageStatuses(String id, List<CmsPageStatus> pageStatuses)
    {
        return getPageForId(id, Arrays.asList(new CatalogVersionModel[] {getActiveCatalogVersion()}, ), pageStatuses);
    }


    public AbstractPageModel getPageForId(String id, Collection<CatalogVersionModel> catalogVersions)
    {
        return getPageForId(id, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    protected AbstractPageModel getPageForId(String uid, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        return (AbstractPageModel)getSessionSearchRestrictionsDisabler().execute(() -> {
            List<AbstractPageModel> pages = (List<AbstractPageModel>)getCmsPageDao().findPagesByIdAndPageStatuses(uid, catalogVersions, pageStatuses).stream().sorted(()).collect(Collectors.toList());
            if(pages.isEmpty())
            {
                throw new UnknownIdentifierException("No page with id [" + uid + "] found in active catalog version");
            }
            return pages.get(0);
        });
    }


    public PageTemplateModel getPageTemplateForIdFromActiveCatalogVersion(String id)
    {
        List<PageTemplateModel> templates = getCmsPageTemplateDao().findPageTemplatesByIdAndCatalogVersion(id,
                        getActiveCatalogVersion());
        if(templates.isEmpty())
        {
            throw new UnknownIdentifierException("No template with id [" + id + "] found in active catalog version");
        }
        if(templates.size() > 1)
        {
            throw new AmbiguousIdentifierException("Page template id '" + id + "' is not unique, " + templates
                            .size() + " pages found!");
        }
        return templates.get(0);
    }


    public boolean pageExists(String id)
    {
        try
        {
            return (getPageForIdFromActiveCatalogVersion(id) != null);
        }
        catch(UnknownIdentifierException e)
        {
            return false;
        }
    }


    public void updatePage(AbstractPageModel page, String name, String masterTemplateId) throws CMSItemNotFoundException
    {
        page.setName(name);
        page.setMasterTemplate(getPageTemplateForIdFromActiveCatalogVersion(masterTemplateId));
        getModelService().save(page);
    }


    protected AbstractPageModel createPageFromType(String composedTypeCode) throws CMSItemCreateException
    {
        try
        {
            return (AbstractPageModel)getModelService().create(composedTypeCode);
        }
        catch(Exception e)
        {
            throw new CMSItemCreateException("Could not find composed type with code [" + composedTypeCode + "]", e);
        }
    }


    public ContentPageModel getHomepage(CMSSiteModel siteModel)
    {
        List<CatalogVersionModel> activeContentCatalogVersions = (List<CatalogVersionModel>)siteModel.getContentCatalogs().stream().map(CatalogModel::getActiveCatalogVersion).collect(Collectors.toList());
        return getHomepage(activeContentCatalogVersions);
    }


    public ContentPageModel getHomepage(CatalogVersionModel catalogVersion)
    {
        return getHomepage(Arrays.asList(new CatalogVersionModel[] {catalogVersion}));
    }


    public ContentPageModel getHomepage(List<CatalogVersionModel> catalogVersions)
    {
        return getAllContentPages(catalogVersions).stream()
                        .filter(ContentPageModel::isHomepage).findFirst()
                        .orElse(null);
    }


    public Collection<AbstractPageModel> findPagesByType(ComposedTypeModel composedType, boolean isDefault)
    {
        return findPagesByTypeAndPageStatuses(composedType, isDefault, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<AbstractPageModel> findPagesByTypeAndPageStatuses(ComposedTypeModel composedType, boolean isDefault, List<CmsPageStatus> pageStatuses)
    {
        return (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByTypeAndPageStatuses(composedType, Arrays.asList(new CatalogVersionModel[] {getActiveCatalogVersion()}, ), isDefault, pageStatuses));
    }


    public AbstractPageModel getIdenticalPrimaryPageModel(AbstractPageModel pageModel)
    {
        if(pageModel != null)
        {
            Collection<?> defaultPages;
            if("ContentPage".equals(pageModel.getItemtype()))
            {
                defaultPages = getCmsPageDao().findDefaultContentPageByLabelAndCatalogVersions(((ContentPageModel)pageModel)
                                .getLabel(), Arrays.asList(new CatalogVersionModel[] {pageModel.getCatalogVersion()}));
            }
            else
            {
                ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(pageModel.getItemtype());
                defaultPages = getCmsPageDao().findDefaultPagesByType(composedType, Arrays.asList(new CatalogVersionModel[] {pageModel.getCatalogVersion()}));
            }
            if(!defaultPages.isEmpty())
            {
                return defaultPages.iterator().next();
            }
        }
        return null;
    }


    public void trashPage(String pageUid, CatalogVersionModel catalogVersion) throws CMSItemNotFoundException
    {
        List<AbstractPageModel> pages = getCmsPageDao().findPagesByIdAndPageStatuses(pageUid, Arrays.asList(new CatalogVersionModel[] {catalogVersion}, ), Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE, CmsPageStatus.DELETED}));
        AbstractPageModel page = pages.isEmpty() ? null : pages.iterator().next();
        if(page == null)
        {
            throw new CMSItemNotFoundException("No page with id [" + pageUid + "] found.");
        }
        page.setPageStatus(CmsPageStatus.DELETED);
        getModelService().save(page);
    }


    protected CMSPageDao getCmsPageDao()
    {
        return this.cmsPageDao;
    }


    @Required
    public void setCmsPageDao(CMSPageDao cmsPageDao)
    {
        this.cmsPageDao = cmsPageDao;
    }


    protected CMSPageTemplateDao getCmsPageTemplateDao()
    {
        return this.cmsPageTemplateDao;
    }


    @Required
    public void setCmsPageTemplateDao(CMSPageTemplateDao cmsPageTemplateDao)
    {
        this.cmsPageTemplateDao = cmsPageTemplateDao;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
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


    protected Comparator<CMSItemModel> getCmsItemCatalogLevelComparator()
    {
        return this.cmsItemCatalogLevelComparator;
    }


    @Required
    public void setCmsItemCatalogLevelComparator(Comparator<CMSItemModel> cmsItemCatalogLevelComparator)
    {
        this.cmsItemCatalogLevelComparator = cmsItemCatalogLevelComparator;
    }


    protected CMSCatalogVersionService getCmsCatalogVersionService()
    {
        return this.cmsCatalogVersionService;
    }


    @Required
    public void setCmsCatalogVersionService(CMSCatalogVersionService cmsCatalogVersionService)
    {
        this.cmsCatalogVersionService = cmsCatalogVersionService;
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


    protected SessionSearchRestrictionsDisabler getSessionSearchRestrictionsDisabler()
    {
        return this.sessionSearchRestrictionsDisabler;
    }


    @Required
    public void setSessionSearchRestrictionsDisabler(SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler)
    {
        this.sessionSearchRestrictionsDisabler = sessionSearchRestrictionsDisabler;
    }
}
