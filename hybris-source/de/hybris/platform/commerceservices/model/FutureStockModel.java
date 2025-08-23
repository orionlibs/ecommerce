package de.hybris.platform.commerceservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

public class FutureStockModel extends ItemModel
{
    public static final String _TYPECODE = "FutureStock";
    public static final String PRODUCTCODE = "productCode";
    public static final String QUANTITY = "quantity";
    public static final String DATE = "date";
    public static final String PRODUCTS = "products";


    public FutureStockModel()
    {
    }


    public FutureStockModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FutureStockModel(String _productCode)
    {
        setProductCode(_productCode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FutureStockModel(ItemModel _owner, String _productCode)
    {
        setOwner(_owner);
        setProductCode(_productCode);
    }


    @Accessor(qualifier = "date", type = Accessor.Type.GETTER)
    public Date getDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("date");
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
    public String getProductCode()
    {
        return (String)getPersistenceContext().getPropertyValue("productCode");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getProducts()
    {
        return (Collection<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Integer getQuantity()
    {
        return (Integer)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "date", type = Accessor.Type.SETTER)
    public void setDate(Date value)
    {
        getPersistenceContext().setPropertyValue("date", value);
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
    public void setProductCode(String value)
    {
        getPersistenceContext().setPropertyValue("productCode", value);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Collection<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Integer value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }
}
