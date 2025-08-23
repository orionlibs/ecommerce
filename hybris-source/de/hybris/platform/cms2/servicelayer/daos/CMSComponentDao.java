package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.List;

public interface CMSComponentDao extends Dao
{
    List<AbstractCMSComponentModel> findCMSComponentsByIdAndCatalogVersions(String paramString, Collection<CatalogVersionModel> paramCollection);


    <T extends AbstractCMSComponentModel> SearchPageData<T> findCMSComponentsByIdsAndCatalogVersions(Collection<String> paramCollection, Collection<CatalogVersionModel> paramCollection1, SearchPageData paramSearchPageData);


    List<AbstractCMSComponentModel> findCMSComponents(String paramString1, String paramString2, Collection<CatalogVersionModel> paramCollection);


    List<AbstractCMSComponentModel> findCMSComponentsByIdAndCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractCMSComponentModel> findCMSComponentsById(String paramString);


    List<AbstractCMSComponentContainerModel> findCMSComponentContainersByIdAndCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel);


    @Deprecated(since = "1811", forRemoval = true)
    List<SimpleCMSComponentModel> findCMSComponentsOfContainerByIdAndCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractCMSComponentModel> findAllCMSComponentsByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    <T extends AbstractCMSComponentModel> SearchPageData<T> findAllCMSComponentsByCatalogVersions(Collection<CatalogVersionModel> paramCollection, SearchPageData paramSearchPageData);


    SearchResult<AbstractCMSComponentModel> findByCatalogVersionAndMask(CatalogVersionModel paramCatalogVersionModel, String paramString, PageableData paramPageableData);


    long getComponentReferenceCountOutsidePage(AbstractCMSComponentModel paramAbstractCMSComponentModel, AbstractPageModel paramAbstractPageModel);
}
