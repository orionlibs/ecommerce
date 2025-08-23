package de.hybris.platform.cms2.servicelayer.services.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.common.functions.ThrowableSupplier;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CatalogPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageTemplateDao;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSPageService extends AbstractCMSPageService implements CMSPageService
{
    protected static final Logger LOG = Logger.getLogger(DefaultCMSPageService.class.getName());
    private CatalogService catalogService;
    private CategoryService categoryService;
    private CatalogLevelService cmsCatalogLevelService;
    private CMSContentPageService cmsContentPageService;
    private CMSContentSlotDao cmsContentSlotDao;
    private CMSDataFactory cmsDataFactory;
    private CMSPageTemplateDao cmsPageTemplateDao;
    private ConfigurationService configurationService;
    private ProductService productService;
    private TypeService typeService;
    private CMSAdminSiteService adminSiteService;
    private Comparator<CatalogVersionModel> cmsCatalogVersionLevelComparator;
    private CMSCatalogVersionService cmsCatalogVersionService;
    private static final String FOUND = "] found.";
    private static final String AS_DEFAULT = " Considering this as default.";
    private static final String RETURNING_DEFAULT = "]. Returning default.";


    public Collection<PageTemplateModel> getAllActivePageTemplates()
    {
        return getCmsPageTemplateDao().findAllPageTemplatesByCatalogVersions(getCatalogVersionService().getSessionCatalogVersions(), true);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public Collection<ContentPageModel> getAllContentPages()
    {
        return (Collection<ContentPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findAllContentPagesByCatalogVersions(getCatalogVersionService().getSessionCatalogVersions()));
    }


    public Collection<ContentSlotForPageModel> getOwnContentSlotsForPage(AbstractPageModel page)
    {
        return getCmsContentSlotDao().findAllContentSlotRelationsByPage(page);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public Collection<ContentSlotForPageModel> getContentSlotModelsForPage(AbstractPageModel page, PagePreviewCriteriaData pagePreviewCriteria)
    {
        ThrowableSupplier<Collection<ContentSlotForPageModel>> defaultSupplier = () -> getOwnContentSlotsForPage(page);
        ThrowableSupplier<Collection<ContentSlotForPageModel>> versionSupplier = () -> getCmsVersionSessionContextProvider().getAllCachedContentSlotsForPage();
        return (Collection<ContentSlotForPageModel>)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public ContentSlotData getContentSlotForPage(AbstractPageModel page, String position) throws CMSItemNotFoundException
    {
        Collection<CatalogVersionModel> catalogVersions = getCatalogVersionService().getSessionCatalogVersions();
        List<ContentSlotForPageModel> csForPage = getCmsContentSlotDao().findContentSlotRelationsByPageAndPosition(page, position, catalogVersions);
        ContentSlotData result = null;
        if(!csForPage.isEmpty())
        {
            result = getCmsDataFactory().createContentSlotData(csForPage.get(0));
        }
        else
        {
            PageTemplateModel template = page.getMasterTemplate();
            List<ContentSlotForTemplateModel> csForTemplate = getCmsContentSlotDao().findContentSlotRelationsByPageTemplateAndPosition(template, position, catalogVersions);
            if(!csForTemplate.isEmpty())
            {
                result = getCmsDataFactory().createContentSlotData(page, csForTemplate.get(0));
            }
        }
        if(result == null)
        {
            throw new CMSItemNotFoundException("Could not find a content slot with position [" + position + "] on page [" + page
                            .getUid() + "] " + page.getName());
        }
        return result;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public ContentSlotData getContentSlotForPage(AbstractPageModel page, String position, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        ThrowableSupplier<ContentSlotData> defaultSupplier = () -> getContentSlotForPage(page, position);
        ThrowableSupplier<ContentSlotData> versionSupplier = () -> getPageContentSlotFromVersionSessionContext(position);
        return (ContentSlotData)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel page)
    {
        Collection<ContentSlotForPageModel> pageSlots = getCmsContentSlotDao().findAllContentSlotRelationsByPage(page);
        List<CatalogVersionModel> catalogVersions = getCmsCatalogVersionService().getFullHierarchyForCatalogVersion(page.getCatalogVersion(), this.adminSiteService.getActiveSite());
        List<ContentSlotForTemplateModel> templateSlots = getCmsContentSlotDao().findContentSlotRelationsByPageTemplateAndCatalogVersions(page.getMasterTemplate(), catalogVersions);
        return getAllContentSlotsForPageAndSlots(page, pageSlots, templateSlots);
    }


    protected Collection<ContentSlotData> getAllContentSlotsForPageAndSlots(AbstractPageModel page, Collection<ContentSlotForPageModel> pageSlots, Collection<ContentSlotForTemplateModel> templateSlots)
    {
        List<String> positions = (List<String>)pageSlots.stream().filter(Objects::nonNull).map(ContentSlotForPageModel::getPosition).collect(Collectors.toList());
        List<ContentSlotData> results = (List<ContentSlotData>)pageSlots.stream().filter(Objects::nonNull).map(pageSlotModel -> getCmsDataFactory().createContentSlotData(pageSlotModel)).collect(Collectors.toList());
        List<ContentSlotForTemplateModel> processedtemplateSlots = getChildSlotForTemplateAtSamePosition(templateSlots);
        appendContentSlots(results, positions, processedtemplateSlots, page);
        return results;
    }


    public List<ContentSlotForTemplateModel> getChildSlotForTemplateAtSamePosition(Collection<ContentSlotForTemplateModel> slotForTemplateModels)
    {
        Map<String, ContentSlotForTemplateModel> positionSlotForTemplates = new HashMap<>();
        slotForTemplateModels.forEach(templateSlot -> {
            ContentSlotForTemplateModel existedSlotTemplate = (ContentSlotForTemplateModel)positionSlotForTemplates.get(templateSlot.getPosition());
            if(existedSlotTemplate != null)
            {
                if(getCmsCatalogVersionLevelComparator().compare(templateSlot.getCatalogVersion(), existedSlotTemplate.getCatalogVersion()) > 0)
                {
                    positionSlotForTemplates.replace(templateSlot.getPosition(), templateSlot);
                }
            }
            else
            {
                positionSlotForTemplates.put(templateSlot.getPosition(), templateSlot);
            }
        });
        return new ArrayList<>(positionSlotForTemplates.values());
    }


    protected Collection<ContentSlotForTemplateModel> getPageTemplateSlots(AbstractPageModel page)
    {
        PageTemplateModel template = page.getMasterTemplate();
        return getCmsContentSlotDao().findAllContentSlotRelationsByPageTemplate(template);
    }


    public Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel page, PagePreviewCriteriaData pagePreviewCriteria)
    {
        ThrowableSupplier<Collection<ContentSlotData>> defaultSupplier = () -> getContentSlotsForPage(page);
        ThrowableSupplier<Collection<ContentSlotData>> versionSupplier = () -> getContentSlotsForPageVersionFromSessionContext(page);
        return (Collection<ContentSlotData>)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    protected Collection<ContentSlotData> getContentSlotsForPageVersionFromSessionContext(AbstractPageModel page) throws CMSItemNotFoundException
    {
        Collection<ContentSlotForPageModel> pageSlots = getCmsVersionSessionContextProvider().getAllCachedContentSlotsForPage();
        List<CatalogVersionModel> catalogVersions = getCmsCatalogVersionService().getFullHierarchyForCatalogVersion(page.getCatalogVersion(), this.adminSiteService.getActiveSite());
        List<ContentSlotForTemplateModel> templateSlots = getCmsContentSlotDao().findContentSlotRelationsByPageTemplateAndCatalogVersions(page.getMasterTemplate(), catalogVersions);
        return getAllContentSlotsForPageAndSlots(page, pageSlots, templateSlots);
    }


    protected Optional<ContentSlotModel> getOverrideSlot(List<ContentSlotModel> contentSlots, ContentSlotModel contentSlot)
    {
        return contentSlots.stream().filter(slot -> contentSlot.equals(slot.getOriginalSlot())).findFirst();
    }


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


    protected List<ContentSlotModel> appendContentSlots(List<ContentSlotData> contentSlots, List<String> positions, Collection<ContentSlotForTemplateModel> templateSlots, AbstractPageModel page)
    {
        List<ContentSlotModel> addedSlots = new ArrayList<>();
        Iterator<ContentSlotForTemplateModel> templateSlotsIterator = templateSlots.iterator();
        while(templateSlotsIterator.hasNext())
        {
            ContentSlotForTemplateModel templateSlot = templateSlotsIterator.next();
            if(templateSlot.getContentSlot() == null || positions.contains(templateSlot.getPosition()))
            {
                continue;
            }
            positions.add(templateSlot.getPosition());
            ContentSlotData info = getCmsDataFactory().createContentSlotData(page, templateSlot);
            contentSlots.add(info);
            addedSlots.add(info.getContentSlot());
        }
        return addedSlots;
    }


    public Collection<ContentSlotForTemplateModel> getContentSlotsForPageTemplate(PageTemplateModel pageTemplate)
    {
        return getCmsContentSlotDao().findAllContentSlotRelationsByPageTemplate(pageTemplate);
    }


    public CatalogPageModel getDefaultCatalogPage() throws CMSItemNotFoundException
    {
        return (CatalogPageModel)getDefaultPage(GeneratedCms2Constants.TC.CATALOGPAGE);
    }


    public CategoryPageModel getDefaultCategoryPage() throws CMSItemNotFoundException
    {
        return (CategoryPageModel)getDefaultPage(GeneratedCms2Constants.TC.CATEGORYPAGE);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getDefaultPageForLabel(String label, CatalogVersionModel version) throws CMSItemNotFoundException
    {
        return getCmsContentPageService().getDefaultPageForLabel(label, version);
    }


    public ProductPageModel getDefaultProductPage() throws CMSItemNotFoundException
    {
        return (ProductPageModel)getDefaultPage(GeneratedCms2Constants.TC.PRODUCTPAGE);
    }


    public String getFrontendTemplateName(PageTemplateModel template)
    {
        if(template == null)
        {
            return null;
        }
        return StringUtils.isNotBlank(template.getFrontendTemplateName()) ? template.getFrontendTemplateName() : template.getUid();
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getHomepage()
    {
        return getCmsContentPageService().getHomepage();
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getHomepage(PagePreviewCriteriaData pagePreviewCriteria)
    {
        return getCmsContentPageService().getHomepage(pagePreviewCriteria);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public String getLabelOrId(ContentPageModel contentPage)
    {
        return (String)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, contentPage));
    }


    public CatalogPageModel getPageForCatalog(CatalogModel catalog) throws CMSItemNotFoundException
    {
        CatalogPageModel page = (CatalogPageModel)getSinglePage("CatalogPage");
        if(page != null)
        {
            LOG.debug("Only one CatalogPage for catalog [" + catalog.getName() + "] found. Considering this as default.");
            return page;
        }
        ComposedTypeModel type = getTypeService().getComposedTypeForCode("CatalogPage");
        Collection<CatalogVersionModel> versions = getCatalogVersionService().getSessionCatalogVersions();
        RestrictionData data = getCmsDataFactory().createRestrictionData(catalog);
        Collection<AbstractPageModel> pages = getCmsPageDao().findAllPagesByTypeAndCatalogVersions(type, versions);
        Collection<AbstractPageModel> result = getCmsRestrictionService().evaluatePages(pages, data);
        if(result.isEmpty())
        {
            throw new CMSItemNotFoundException("No page for catalog [" + catalog.getName() + "] found.");
        }
        if(result.size() > 1)
        {
            LOG.warn("More than one page found for catalog [" + catalog.getName() + "]. Returning default.");
        }
        return (CatalogPageModel)Iterables.getLast(result);
    }


    public CatalogPageModel getPageForCatalogId(String catalogId) throws CMSItemNotFoundException
    {
        CatalogModel catalog;
        try
        {
            catalog = getCatalogService().getCatalogForId(catalogId);
        }
        catch(Exception e)
        {
            throw new CMSItemNotFoundException("Could not find catalog with id [" + catalogId + "]", e);
        }
        return getPageForCatalog(catalog);
    }


    public CategoryPageModel getPageForCategory(CategoryModel category) throws CMSItemNotFoundException
    {
        String categoryCode = Objects.nonNull(category) ? category.getCode() : "";
        CategoryPageModel page = (CategoryPageModel)getSinglePage("CategoryPage");
        if(page != null)
        {
            LOG.debug("Only one CategoryPage for category [" + categoryCode + "] found. Considering this as default.");
            return page;
        }
        ComposedTypeModel type = getTypeService().getComposedTypeForCode("CategoryPage");
        Collection<CatalogVersionModel> versions = getCatalogVersionService().getSessionCatalogVersions();
        Collection<AbstractPageModel> pages = (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findAllPagesByTypeAndCatalogVersionsAndPageStatuses(type, versions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE})));
        RestrictionData data = getCmsDataFactory().createRestrictionData(category);
        Collection<AbstractPageModel> result = getCmsRestrictionService().evaluatePages(pages, data);
        if(result.isEmpty())
        {
            throw new CMSItemNotFoundException("No page for category [" + categoryCode + "] found.");
        }
        if(result.size() > 1)
        {
            LOG.warn("More than one page found for category [" + categoryCode + "]. Returning default.");
        }
        return (CategoryPageModel)Iterables.getLast(result);
    }


    public CategoryPageModel getPageForCategory(CategoryModel category, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = () -> getPageForCategory(category);
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (CategoryPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public CategoryPageModel getPageForCategoryCode(String categoryCode) throws CMSItemNotFoundException
    {
        CategoryModel category;
        try
        {
            category = getCategoryService().getCategoryForCode(categoryCode);
        }
        catch(Exception e)
        {
            throw new CMSItemNotFoundException("Could not find category with code [" + categoryCode + "]", e);
        }
        return getPageForCategory(category);
    }


    public CategoryPageModel getPageForCategoryCode(String categoryCode, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = () -> getPageForCategoryCode(categoryCode);
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (CategoryPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public AbstractPageModel getPageForId(String id) throws CMSItemNotFoundException
    {
        return super.getPageForId(id);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public AbstractPageModel getPageForIdWithRestrictions(String id) throws CMSItemNotFoundException
    {
        ContentPageModel contentPageModel;
        ProductPageModel productPageModel;
        AbstractPageModel page = null;
        page = getPageForId(id);
        if(page instanceof ContentPageModel)
        {
            String label = ((ContentPageModel)page).getLabel();
            contentPageModel = getCmsContentPageService().getPageForLabelAndStatuses(label, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
        }
        else
        {
            CategoryPageModel categoryPageModel;
            if(contentPageModel instanceof CategoryPageModel)
            {
                categoryPageModel = getPageForCategory(null);
            }
            else if(categoryPageModel instanceof ProductPageModel)
            {
                productPageModel = getPageForProduct(null);
            }
        }
        return (AbstractPageModel)productPageModel;
    }


    public AbstractPageModel getPageForId(String id, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = () -> getPageForId(id);
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (AbstractPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getPageForLabel(String label) throws CMSItemNotFoundException
    {
        return getCmsContentPageService().getPageForLabelAndStatuses(label, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getPageForLabel(String label, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        return getCmsContentPageService().getPageForLabelAndPreview(label, pagePreviewCriteria);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getPageForLabelAndPageStatuses(String label, List<CmsPageStatus> pageStatuses) throws CMSItemNotFoundException
    {
        return getCmsContentPageService().getPageForLabelAndStatuses(label, pageStatuses);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getPageForLabelOrId(String labelOrId) throws CMSItemNotFoundException
    {
        return getCmsContentPageService().getPageForLabelOrIdAndMatchType(labelOrId, true);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public ContentPageModel getPageForLabelOrId(String labelOrId, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        return getCmsContentPageService().getPageForLabelOrIdAndMatchType(labelOrId, pagePreviewCriteria, true);
    }


    public ProductPageModel getPageForProduct(ProductModel product) throws CMSItemNotFoundException
    {
        String productCode = Objects.nonNull(product) ? product.getCode() : "";
        ProductPageModel page = (ProductPageModel)getSinglePage("ProductPage");
        if(page != null)
        {
            LOG.debug("Only one ProductPage for product [" + productCode + "] found. Considering this as default.");
            return page;
        }
        ComposedTypeModel type = getTypeService().getComposedTypeForCode("ProductPage");
        Collection<CatalogVersionModel> versions = getCatalogVersionService().getSessionCatalogVersions();
        RestrictionData data = getCmsDataFactory().createRestrictionData(product);
        Collection<AbstractPageModel> pages = (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findAllPagesByTypeAndCatalogVersionsAndPageStatuses(type, versions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE})));
        Collection<AbstractPageModel> result = getCmsRestrictionService().evaluatePages(pages, data);
        if(result.isEmpty())
        {
            throw new CMSItemNotFoundException("No page for product [" + productCode + "] found.");
        }
        if(result.size() > 1)
        {
            LOG.warn("More than one page found for product [" + productCode + "]. Returning default.");
        }
        return (ProductPageModel)Iterables.getLast(result);
    }


    public ProductPageModel getPageForProduct(ProductModel product, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = () -> getPageForProduct(product);
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (ProductPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public ProductPageModel getPageForProductCode(String productCode) throws CMSItemNotFoundException
    {
        ProductModel product;
        try
        {
            product = getProductService().getProductForCode(productCode);
        }
        catch(Exception e)
        {
            throw new CMSItemNotFoundException("Could not find product with code [" + productCode + "]", e);
        }
        return getPageForProduct(product);
    }


    public ProductPageModel getPageForProductCode(String productCode, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = () -> getPageForProductCode(productCode);
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (ProductPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public Collection<AbstractPageModel> getPagesForComponent(AbstractCMSComponentModel component)
    {
        return (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByContentSlots(component.getSlots(), getCatalogVersionService().getSessionCatalogVersions()));
    }


    public Collection<AbstractPageModel> getPagesForContentSlots(Collection<ContentSlotModel> contentSlots)
    {
        return (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByContentSlots(contentSlots, getCatalogVersionService().getSessionCatalogVersions()));
    }


    public Collection<AbstractPageModel> getPagesForPageTemplateComponent(AbstractCMSComponentModel component)
    {
        return (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByPageTemplateContentSlots(component.getSlots(), getCatalogVersionService().getSessionCatalogVersions()));
    }


    public Collection<AbstractPageModel> getPagesForPageTemplateContentSlots(Collection<ContentSlotModel> contentSlots)
    {
        return (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByPageTemplateContentSlots(contentSlots, getCatalogVersionService().getSessionCatalogVersions()));
    }


    protected AbstractPageModel getDefaultPage(String composedTypeCode) throws CMSItemNotFoundException
    {
        ComposedTypeModel type = getTypeService().getComposedTypeForCode(composedTypeCode);
        Collection<CatalogVersionModel> versions = getCatalogVersionService().getSessionCatalogVersions();
        Collection<AbstractPageModel> pages = (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findDefaultPageByTypeAndCatalogVersions(type, versions));
        if(pages.isEmpty())
        {
            throw new CMSItemNotFoundException("No default page of composed type <" + type.getCode() + "> found.");
        }
        if(pages.size() > 1)
        {
            LOG.warn("More than one default page with composed type <" + type.getCode() + "> found. Returning first.");
        }
        return pages.iterator().next();
    }


    protected AbstractPageModel getSinglePage(String composedTypeCode)
    {
        return (AbstractPageModel)getSessionSearchRestrictionsDisabler().execute(() -> {
            ComposedTypeModel type = getTypeService().getComposedTypeForCode(composedTypeCode);
            Collection<CatalogVersionModel> versions = getCatalogVersionService().getSessionCatalogVersions();
            Collection<AbstractPageModel> pages = getCmsPageDao().findAllPagesByTypeAndCatalogVersionsAndPageStatuses(type, versions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
            if(pages.size() == 1)
            {
                AbstractPageModel page = pages.iterator().next();
                if(page.getRestrictions().isEmpty())
                {
                    return page;
                }
            }
            return null;
        });
    }


    protected ContentSlotData getPageContentSlotFromVersionSessionContext(String position) throws CMSItemNotFoundException
    {
        List<ContentSlotForPageModel> contentSlotsForPage = getCmsVersionSessionContextProvider().getAllCachedContentSlotsForPage();
        Optional<ContentSlotForPageModel> result = contentSlotsForPage.stream().filter(contentSlotForPageModel -> contentSlotForPageModel.getPosition().equals(position)).findFirst();
        if(result.isPresent())
        {
            return getCmsDataFactory().createContentSlotData(result.get());
        }
        throw new CMSItemNotFoundException("Could not find a content slot with position " + position);
    }


    public SearchPageData<AbstractPageModel> findAllPages(String typeCode, SearchPageData searchPageData)
    {
        Preconditions.checkArgument(Objects.nonNull(typeCode), "TypeCode cannot be null");
        ComposedTypeModel composedTypeModel = getTypeService().getComposedTypeForCode(typeCode);
        Collection<CatalogVersionModel> catalogVersions = getCatalogVersionService().getSessionCatalogVersions();
        return getCmsPageDao().findAllPagesByTypeAndCatalogVersions(composedTypeModel, catalogVersions, searchPageData);
    }


    protected CatalogService getCatalogService()
    {
        return this.catalogService;
    }


    @Required
    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }


    protected CategoryService getCategoryService()
    {
        return this.categoryService;
    }


    @Required
    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    @Required
    public void setCmsContentSlotDao(CMSContentSlotDao cmsContentSlotDao)
    {
        this.cmsContentSlotDao = cmsContentSlotDao;
    }


    protected CMSContentSlotDao getCmsContentSlotDao()
    {
        return this.cmsContentSlotDao;
    }


    @Required
    public void setCmsDataFactory(CMSDataFactory cmsDataFactory)
    {
        this.cmsDataFactory = cmsDataFactory;
    }


    protected CMSDataFactory getCmsDataFactory()
    {
        return this.cmsDataFactory;
    }


    protected CMSContentPageService getCmsContentPageService()
    {
        return this.cmsContentPageService;
    }


    @Required
    public void setCmsContentPageService(CMSContentPageService cmsContentPageService)
    {
        this.cmsContentPageService = cmsContentPageService;
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


    protected ProductService getProductService()
    {
        return this.productService;
    }


    @Required
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
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


    protected CMSAdminSiteService getAdminSiteService()
    {
        return this.adminSiteService;
    }


    @Required
    public void setAdminSiteService(CMSAdminSiteService adminSiteService)
    {
        this.adminSiteService = adminSiteService;
    }


    protected Comparator<CatalogVersionModel> getCmsCatalogVersionLevelComparator()
    {
        return this.cmsCatalogVersionLevelComparator;
    }


    @Required
    public void setCmsCatalogVersionLevelComparator(Comparator<CatalogVersionModel> cmsCatalogVersionLevelComparator)
    {
        this.cmsCatalogVersionLevelComparator = cmsCatalogVersionLevelComparator;
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
}
