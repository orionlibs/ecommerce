package de.hybris.platform.cms2.services;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import java.util.Collection;

public interface ContentCatalogService
{
    Collection<ContentCatalogModel> getContentCatalogs();


    Collection<ContentCatalogModel> getContentCatalogs(String paramString);


    boolean isContentCatalog(CatalogModel paramCatalogModel);


    boolean isContentCatalog(CatalogVersionModel paramCatalogVersionModel);


    boolean isProductCatalog(CatalogModel paramCatalogModel);


    boolean isProductCatalog(CatalogVersionModel paramCatalogVersionModel);


    boolean hasContentPages();


    boolean hasDefaultCatalogPage();


    boolean hasCatalogPages();


    boolean hasDefaultCategoryPage();


    boolean hasCategoryPages();


    boolean hasDefaultProductPage();


    boolean hasProductPages();


    boolean hasCMSItems(CatalogVersionModel paramCatalogVersionModel);


    boolean hasCMSRelations(CatalogVersionModel paramCatalogVersionModel);
}
