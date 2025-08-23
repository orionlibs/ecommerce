package de.hybris.platform.cms2.servicelayer.services.admin;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemCreateException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CMSAdminPageService
{
    CategoryPageModel createCategoryPage(String paramString1, String paramString2, String paramString3, boolean paramBoolean) throws CMSItemCreateException;


    ContentPageModel createContentPage(String paramString1, String paramString2, String paramString3, String paramString4) throws CMSItemCreateException;


    AbstractPageModel createPage(String paramString1, String paramString2, String paramString3, String paramString4) throws CMSItemCreateException;


    ProductPageModel createProductPage(String paramString1, String paramString2, String paramString3, boolean paramBoolean) throws CMSItemCreateException;


    void deletePage(String paramString) throws CMSItemNotFoundException;


    CatalogVersionModel getActiveCatalogVersion();


    Collection<PageTemplateModel> getAllActivePageTemplates();


    Collection<ContentPageModel> getAllContentPages(Collection<CatalogVersionModel> paramCollection);


    Collection<ContentPageModel> getAllContentPagesForPageStatuses(Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    Collection<CMSPageTypeModel> getAllPageTypes();


    Optional<CMSPageTypeModel> getPageTypeByCode(String paramString);


    Collection<AbstractPageModel> getAllPages();


    Collection<AbstractPageModel> getAllPages(CatalogVersionModel paramCatalogVersionModel);


    Collection<AbstractPageModel> getAllPagesForCatalogVersionAndPageStatuses(CatalogVersionModel paramCatalogVersionModel, List<CmsPageStatus> paramList);


    Collection<AbstractPageModel> getAllPagesByType(String paramString);


    Collection<AbstractPageModel> getAllPagesByType(String paramString, CatalogVersionModel paramCatalogVersionModel);


    Map<String, Collection<AbstractPageModel>> getAllPagesMap();


    Collection<PageTemplateModel> getAllPageTemplates(boolean paramBoolean);


    Collection<PageTemplateModel> getAllPageTemplates(Collection<CatalogVersionModel> paramCollection);


    Collection<PageTemplateModel> getAllRestrictedPageTemplates(boolean paramBoolean, CMSPageTypeModel paramCMSPageTypeModel);


    Collection<ContentPageModel> getContentPages(Collection<CatalogVersionModel> paramCollection, String paramString);


    AbstractPageModel getPageForIdFromActiveCatalogVersion(String paramString) throws UnknownIdentifierException;


    AbstractPageModel getPageForId(String paramString, Collection<CatalogVersionModel> paramCollection);


    AbstractPageModel getPageForIdFromActiveCatalogVersionByPageStatuses(String paramString, List<CmsPageStatus> paramList) throws UnknownIdentifierException;


    PageTemplateModel getPageTemplateForIdFromActiveCatalogVersion(String paramString) throws AmbiguousIdentifierException, UnknownIdentifierException;


    boolean pageExists(String paramString);


    void updatePage(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2) throws CMSItemNotFoundException;


    ContentPageModel getHomepage(CMSSiteModel paramCMSSiteModel);


    ContentPageModel getHomepage(List<CatalogVersionModel> paramList);


    ContentPageModel getHomepage(CatalogVersionModel paramCatalogVersionModel);


    Collection<AbstractPageModel> findPagesByType(ComposedTypeModel paramComposedTypeModel, boolean paramBoolean);


    Collection<AbstractPageModel> findPagesByTypeAndPageStatuses(ComposedTypeModel paramComposedTypeModel, boolean paramBoolean, List<CmsPageStatus> paramList);


    AbstractPageModel getIdenticalPrimaryPageModel(AbstractPageModel paramAbstractPageModel);


    void trashPage(String paramString, CatalogVersionModel paramCatalogVersionModel) throws CMSItemNotFoundException;
}
