package de.hybris.platform.cms2.catalogversion.service;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CMSCatalogVersionService
{
    Map<CatalogModel, Set<CatalogVersionModel>> getContentCatalogsAndVersions(boolean paramBoolean1, boolean paramBoolean2, CMSSiteModel paramCMSSiteModel);


    Map<CatalogModel, Set<CatalogVersionModel>> getProductCatalogsAndVersions(boolean paramBoolean1, boolean paramBoolean2, BaseSiteModel paramBaseSiteModel);


    List<CatalogVersionModel> getWritableChildContentCatalogVersions(PrincipalModel paramPrincipalModel, CMSSiteModel paramCMSSiteModel, CatalogModel paramCatalogModel);


    List<CatalogVersionModel> getWritableContentCatalogVersions(PrincipalModel paramPrincipalModel, CatalogModel paramCatalogModel);


    List<CatalogVersionModel> getSubCatalogsCatalogVersions(ContentCatalogModel paramContentCatalogModel);


    List<CatalogVersionModel> getSuperCatalogsCatalogVersions(ContentCatalogModel paramContentCatalogModel, CMSSiteModel paramCMSSiteModel);


    List<CatalogVersionModel> getSuperCatalogsActiveCatalogVersions(ContentCatalogModel paramContentCatalogModel, CMSSiteModel paramCMSSiteModel);


    List<CatalogVersionModel> getFullHierarchyForCatalogVersion(CatalogVersionModel paramCatalogVersionModel, CMSSiteModel paramCMSSiteModel);


    default boolean isContentCatalogModelAncestor(ContentCatalogModel catalogModel, ContentCatalogModel ancestor)
    {
        return false;
    }


    default boolean areCatalogVersionsRelatives(Collection<CatalogVersionModel> catalogVersionModels)
    {
        return false;
    }


    default Collection<CatalogVersionModel> getIntersectionOfCatalogVersions(Collection<CatalogVersionModel> catalogVersionModelsA, Collection<CatalogVersionModel> catalogVersionModelsB)
    {
        return new ArrayList<>();
    }


    default List<CatalogVersionModel> getSubCatalogsCatalogVersionsInSite(CMSSiteModel site, ContentCatalogModel catalog)
    {
        return new ArrayList<>();
    }
}
