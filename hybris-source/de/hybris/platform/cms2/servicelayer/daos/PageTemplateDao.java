package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.List;

public interface PageTemplateDao extends Dao
{
    Collection<AbstractPageModel> findAllPagesByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    List<AbstractPageModel> findPagesByIdAndCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractPageModel> findPagesByIdAndCatalogVersions(String paramString, Collection<CatalogVersionModel> paramCollection);


    Collection<ContentPageModel> findPagesByLabel(String paramString, Collection<CatalogVersionModel> paramCollection);


    List<AbstractPageModel> findDefaultPagesByType(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection);


    Collection<AbstractPageModel> findAllPagesByLabel(String paramString, Collection<CatalogVersionModel> paramCollection);


    Collection<ContentPageModel> findAllContentPagesByCatalogVersions(Collection<CatalogVersionModel> paramCollection);


    Collection<AbstractPageModel> findAllPagesByTypeAndCatalogVersions(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection);


    Collection<AbstractPageModel> findDefaultPageByTypeAndCatalogVersions(ComposedTypeModel paramComposedTypeModel, Collection<CatalogVersionModel> paramCollection);


    Collection<ContentPageModel> findDefaultContentPageByLabelAndCatalogVersions(String paramString, Collection<CatalogVersionModel> paramCollection);
}
