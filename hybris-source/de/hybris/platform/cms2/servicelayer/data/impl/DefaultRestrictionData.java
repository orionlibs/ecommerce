package de.hybris.platform.cms2.servicelayer.data.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.HashMap;
import java.util.Map;

public class DefaultRestrictionData implements RestrictionData
{
    private static final long serialVersionUID = -7366549789914197595L;
    private CatalogModel catalog;
    private CategoryModel category;
    private ProductModel product;
    private Map<String, Object> values;


    public CatalogModel getCatalog()
    {
        return this.catalog;
    }


    public CategoryModel getCategory()
    {
        return this.category;
    }


    public ProductModel getProduct()
    {
        return this.product;
    }


    public Object getValue(String key)
    {
        if(this.values == null)
        {
            return null;
        }
        return this.values.get(key);
    }


    public boolean hasCatalog()
    {
        return (this.catalog != null);
    }


    public boolean hasCategory()
    {
        return (this.category != null);
    }


    public boolean hasProduct()
    {
        return (this.product != null);
    }


    public boolean hasValue(String key)
    {
        return (this.values != null && this.values.containsKey(key));
    }


    public void setCatalog(CatalogModel catalog)
    {
        this.catalog = catalog;
    }


    public void setCategory(CategoryModel category)
    {
        this.category = category;
    }


    public void setProduct(ProductModel product)
    {
        this.product = product;
    }


    public void setValue(String key, Object value)
    {
        if(this.values == null)
        {
            this.values = new HashMap<>();
        }
        this.values.put(key, value);
    }
}
