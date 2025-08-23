package de.hybris.platform.cms2.servicelayer.data;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;

public interface CMSDataFactory
{
    RestrictionData createRestrictionData();


    RestrictionData createRestrictionData(CategoryModel paramCategoryModel, ProductModel paramProductModel);


    RestrictionData createRestrictionData(String paramString1, String paramString2, String paramString3);


    RestrictionData createRestrictionData(ProductModel paramProductModel);


    RestrictionData createRestrictionData(CategoryModel paramCategoryModel);


    RestrictionData createRestrictionData(CatalogModel paramCatalogModel);


    RestrictionData createRestrictionData(String paramString, Object paramObject);


    ContentSlotData createContentSlotData(ContentSlotForPageModel paramContentSlotForPageModel);


    ContentSlotData createContentSlotData(AbstractPageModel paramAbstractPageModel, ContentSlotForTemplateModel paramContentSlotForTemplateModel);


    ContentSlotData createContentSlotData(String paramString1, ContentSlotModel paramContentSlotModel, String paramString2, boolean paramBoolean1, boolean paramBoolean2);
}
