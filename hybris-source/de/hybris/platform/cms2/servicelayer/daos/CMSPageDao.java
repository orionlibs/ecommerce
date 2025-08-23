package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.List;

public interface CMSPageDao extends Dao
{
    Collection<AbstractPageModel> findAllPagesByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    Collection<AbstractPageModel> findAllPagesByCatalogVersionAndPageStatuses(CatalogVersionModel paramCatalogVersionModel, List<CmsPageStatus> paramList);


    List<AbstractPageModel> findPagesByIdAndCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractPageModel> findPagesByIdAndCatalogVersionAndPageStatuses(String paramString, CatalogVersionModel paramCatalogVersionModel, List<CmsPageStatus> paramList);


    <T extends AbstractPageModel> List<T> findPagesByIdAndPageStatuses(String paramString, Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    List<AbstractPageModel> findPagesById(String paramString, Collection<CatalogVersionModel> paramCollection);


    Collection<ContentPageModel> findPagesByLabel(String paramString, Collection<CatalogVersionModel> paramCollection);


    <T extends AbstractPageModel> Collection<T> findPagesByLabelAndPageStatuses(String paramString, Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    <T extends AbstractPageModel> Collection<T> findPagesByLabelsAndPageStatuses(Collection<String> paramCollection, Collection<CatalogVersionModel> paramCollection1, List<CmsPageStatus> paramList);


    List<AbstractPageModel> findDefaultPagesByType(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection);


    List<AbstractPageModel> findPagesByType(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection, boolean paramBoolean);


    List<AbstractPageModel> findPagesByTypeAndPageStatuses(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection, boolean paramBoolean, List<CmsPageStatus> paramList);


    Collection<ContentPageModel> findAllContentPagesByCatalogVersions(Collection<CatalogVersionModel> paramCollection);


    Collection<ContentPageModel> findAllContentPagesByCatalogVersionsAndPageStatuses(Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    Collection<AbstractPageModel> findAllPagesByTypeAndCatalogVersions(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection);


    <T extends AbstractPageModel> SearchPageData<T> findAllPagesByTypeAndCatalogVersions(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection, SearchPageData paramSearchPageData);


    Collection<AbstractPageModel> findAllPagesByTypeAndCatalogVersionsAndPageStatuses(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    Collection<AbstractPageModel> findDefaultPageByTypeAndCatalogVersions(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection);


    Collection<AbstractPageModel> findDefaultPageByTypeAndCatalogVersionsAndPageStatuses(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    Collection<ContentPageModel> findDefaultContentPageByLabelAndCatalogVersions(String paramString, Collection<CatalogVersionModel> paramCollection);


    Collection<ContentPageModel> findDefaultContentPageByLabelAndCatalogVersionsAndPageStatuses(String paramString, Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    Collection<AbstractPageModel> findPagesByContentSlots(Collection<ContentSlotModel> paramCollection, Collection<CatalogVersionModel> paramCollection1);


    Collection<AbstractPageModel> findPagesByContentSlotsAndPageStatuses(Collection<ContentSlotModel> paramCollection, Collection<CatalogVersionModel> paramCollection1, List<CmsPageStatus> paramList);


    Collection<AbstractPageModel> findPagesByPageTemplateContentSlots(Collection<ContentSlotModel> paramCollection, Collection<CatalogVersionModel> paramCollection1);


    Collection<AbstractPageModel> findPagesByPageTemplateContentSlotsAndPageStatuses(Collection<ContentSlotModel> paramCollection, Collection<CatalogVersionModel> paramCollection1, List<CmsPageStatus> paramList);


    ContentPageModel findHomepage(Collection<CatalogVersionModel> paramCollection);


    ContentPageModel findHomepageByPageStatuses(Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);


    Collection<ContentPageModel> findHomepagesByPageStatuses(Collection<CatalogVersionModel> paramCollection, List<CmsPageStatus> paramList);
}
