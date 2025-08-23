package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.List;

public interface CMSPageTemplateDao extends Dao
{
    Collection<PageTemplateModel> findAllPageTemplatesByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    Collection<PageTemplateModel> findAllPageTemplatesByCatalogVersion(CatalogVersionModel paramCatalogVersionModel, boolean paramBoolean);


    Collection<PageTemplateModel> findAllPageTemplatesByCatalogVersions(Collection<CatalogVersionModel> paramCollection, boolean paramBoolean);


    List<PageTemplateModel> findPageTemplatesByIdAndCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel);


    Collection<PageTemplateModel> findAllRestrictedPageTemplatesByCatalogVersion(CatalogVersionModel paramCatalogVersionModel, boolean paramBoolean, CMSPageTypeModel paramCMSPageTypeModel);


    Collection<PageTemplateModel> findAllRestrictedPageTemplatesByCatalogVersion(Collection<CatalogVersionModel> paramCollection, boolean paramBoolean, CMSPageTypeModel paramCMSPageTypeModel);
}
