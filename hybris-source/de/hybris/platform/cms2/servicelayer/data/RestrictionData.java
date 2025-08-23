package de.hybris.platform.cms2.servicelayer.data;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.io.Serializable;

public interface RestrictionData extends Serializable
{
    Object getValue(String paramString);


    void setValue(String paramString, Object paramObject);


    boolean hasValue(String paramString);


    void setProduct(ProductModel paramProductModel);


    ProductModel getProduct();


    boolean hasProduct();


    void setCategory(CategoryModel paramCategoryModel);


    CategoryModel getCategory();


    boolean hasCategory();


    void setCatalog(CatalogModel paramCatalogModel);


    CatalogModel getCatalog();


    boolean hasCatalog();
}
