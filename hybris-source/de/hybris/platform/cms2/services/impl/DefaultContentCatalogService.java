package de.hybris.platform.cms2.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentCatalogDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.cms2.servicelayer.services.impl.AbstractCMSService;
import de.hybris.platform.cms2.services.ContentCatalogService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;

public class DefaultContentCatalogService extends AbstractCMSService implements ContentCatalogService
{
    private CMSPageDao cmsPageDao;
    private CMSContentCatalogDao cmsContentCatalogDao;
    private TypeService typeService;
    private CatalogVersionService catalogVersionService;


    public Collection<ContentCatalogModel> getContentCatalogs()
    {
        return this.cmsContentCatalogDao.findAllContentCatalogs();
    }


    public Collection<ContentCatalogModel> getContentCatalogs(String orderField)
    {
        return this.cmsContentCatalogDao.findAllContentCatalogsOrderedBy(orderField);
    }


    public boolean hasCatalogPages()
    {
        ComposedTypeModel typeModel = this.typeService.getComposedTypeForCode(GeneratedCms2Constants.TC.CATALOGPAGE);
        return hasPagesOfType(typeModel);
    }


    public boolean hasCategoryPages()
    {
        ComposedTypeModel typeModel = this.typeService.getComposedTypeForCode(GeneratedCms2Constants.TC.CATEGORYPAGE);
        return hasPagesOfType(typeModel);
    }


    public boolean hasCMSItems(CatalogVersionModel versionModel)
    {
        return this.cmsContentCatalogDao.hasCMSItems(versionModel);
    }


    public boolean hasCMSRelations(CatalogVersionModel versionModel)
    {
        return this.cmsContentCatalogDao.hasCMSRelations(versionModel);
    }


    public boolean hasContentPages()
    {
        ComposedTypeModel typeModel = this.typeService.getComposedTypeForCode(GeneratedCms2Constants.TC.CONTENTPAGE);
        return hasPagesOfType(typeModel);
    }


    public boolean hasDefaultCatalogPage()
    {
        return hasDefaultPage(GeneratedCms2Constants.TC.CATALOGPAGE);
    }


    public boolean hasDefaultCategoryPage()
    {
        return hasDefaultPage(GeneratedCms2Constants.TC.CATEGORYPAGE);
    }


    public boolean hasDefaultProductPage()
    {
        return hasDefaultPage(GeneratedCms2Constants.TC.PRODUCTPAGE);
    }


    public boolean hasProductPages()
    {
        ComposedTypeModel typeModel = this.typeService.getComposedTypeForCode(GeneratedCms2Constants.TC.PRODUCTPAGE);
        return hasPagesOfType(typeModel);
    }


    public boolean isContentCatalog(CatalogModel catalog)
    {
        return catalog instanceof ContentCatalogModel;
    }


    public boolean isContentCatalog(CatalogVersionModel catalogVersion)
    {
        return isContentCatalog(catalogVersion.getCatalog());
    }


    public boolean isProductCatalog(CatalogModel catalog)
    {
        if(isContentCatalog(catalog))
        {
            return false;
        }
        return !(catalog instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemModel);
    }


    public boolean isProductCatalog(CatalogVersionModel catalogVersion)
    {
        return isProductCatalog(catalogVersion.getCatalog());
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public void setCmsContentCatalogDao(CMSContentCatalogDao cmsContentCatalogDao)
    {
        this.cmsContentCatalogDao = cmsContentCatalogDao;
    }


    public void setCmsPageDao(CMSPageDao cmsPageDao)
    {
        this.cmsPageDao = cmsPageDao;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected boolean hasDefaultPage(String composedTypeCode)
    {
        return (this.cmsPageDao.findDefaultPagesByType(this.typeService.getComposedTypeForCode(composedTypeCode), this.catalogVersionService
                        .getSessionCatalogVersions()) != null);
    }


    protected boolean hasPagesOfType(ComposedTypeModel composedType)
    {
        Collection<CatalogVersionModel> sessionCatalogVersions = this.catalogVersionService.getSessionCatalogVersions();
        Collection<AbstractPageModel> pages = this.cmsPageDao.findAllPagesByTypeAndCatalogVersions(composedType, sessionCatalogVersions);
        return (pages != null && !pages.isEmpty());
    }
}
