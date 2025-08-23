package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
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
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface CMSPageService
{
    Collection<PageTemplateModel> getAllActivePageTemplates();


    @Deprecated(since = "1905", forRemoval = true)
    Collection<ContentPageModel> getAllContentPages();


    Collection<ContentSlotForPageModel> getOwnContentSlotsForPage(AbstractPageModel paramAbstractPageModel);


    @Deprecated(since = "2105", forRemoval = true)
    Collection<ContentSlotForPageModel> getContentSlotModelsForPage(AbstractPageModel paramAbstractPageModel, PagePreviewCriteriaData paramPagePreviewCriteriaData);


    ContentSlotData getContentSlotForPage(AbstractPageModel paramAbstractPageModel, String paramString) throws CMSItemNotFoundException;


    @Deprecated(since = "2105", forRemoval = true)
    ContentSlotData getContentSlotForPage(AbstractPageModel paramAbstractPageModel, String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel paramAbstractPageModel);


    Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel paramAbstractPageModel, PagePreviewCriteriaData paramPagePreviewCriteriaData);


    Collection<ContentSlotForTemplateModel> getContentSlotsForPageTemplate(PageTemplateModel paramPageTemplateModel);


    CatalogPageModel getDefaultCatalogPage() throws CMSItemNotFoundException;


    CategoryPageModel getDefaultCategoryPage() throws CMSItemNotFoundException;


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getDefaultPageForLabel(String paramString, CatalogVersionModel paramCatalogVersionModel) throws CMSItemNotFoundException;


    ProductPageModel getDefaultProductPage() throws CMSItemNotFoundException;


    String getFrontendTemplateName(PageTemplateModel paramPageTemplateModel);


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getHomepage();


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getHomepage(PagePreviewCriteriaData paramPagePreviewCriteriaData);


    @Deprecated(since = "1905", forRemoval = true)
    String getLabelOrId(ContentPageModel paramContentPageModel);


    CatalogPageModel getPageForCatalog(CatalogModel paramCatalogModel) throws CMSItemNotFoundException;


    CatalogPageModel getPageForCatalogId(String paramString) throws CMSItemNotFoundException;


    CategoryPageModel getPageForCategory(CategoryModel paramCategoryModel) throws CMSItemNotFoundException;


    CategoryPageModel getPageForCategory(CategoryModel paramCategoryModel, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    CategoryPageModel getPageForCategoryCode(String paramString) throws CMSItemNotFoundException;


    CategoryPageModel getPageForCategoryCode(String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    AbstractPageModel getPageForId(String paramString) throws CMSItemNotFoundException;


    @Deprecated(since = "1905", forRemoval = true)
    AbstractPageModel getPageForIdWithRestrictions(String paramString) throws CMSItemNotFoundException;


    AbstractPageModel getPageForId(String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getPageForLabel(String paramString) throws CMSItemNotFoundException;


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getPageForLabel(String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getPageForLabelAndPageStatuses(String paramString, List<CmsPageStatus> paramList) throws CMSItemNotFoundException;


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getPageForLabelOrId(String paramString) throws CMSItemNotFoundException;


    @Deprecated(since = "1905", forRemoval = true)
    ContentPageModel getPageForLabelOrId(String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    ProductPageModel getPageForProduct(ProductModel paramProductModel) throws CMSItemNotFoundException;


    ProductPageModel getPageForProduct(ProductModel paramProductModel, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    ProductPageModel getPageForProductCode(String paramString) throws CMSItemNotFoundException;


    ProductPageModel getPageForProductCode(String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    Collection<AbstractPageModel> getPagesForComponent(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    Collection<AbstractPageModel> getPagesForContentSlots(Collection<ContentSlotModel> paramCollection);


    Collection<AbstractPageModel> getPagesForPageTemplateComponent(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    Collection<AbstractPageModel> getPagesForPageTemplateContentSlots(Collection<ContentSlotModel> paramCollection);


    @Deprecated(since = "2105", forRemoval = true)
    List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> paramList, List<CatalogVersionModel> paramList1);


    List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> paramList, List<CatalogVersionModel> paramList1, AbstractPageModel paramAbstractPageModel);


    SearchPageData<AbstractPageModel> findAllPages(String paramString, SearchPageData paramSearchPageData);


    default List<ContentSlotForTemplateModel> getChildSlotForTemplateAtSamePosition(Collection<ContentSlotForTemplateModel> slotForTemplateModels)
    {
        return new ArrayList<>();
    }
}
