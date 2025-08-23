package de.hybris.platform.europe1.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class TaxRowModel extends PDTRowModel
{
    public static final String _TYPECODE = "TaxRow";
    public static final String _PRODUCT2OWNEUROPE1TAXES = "Product2OwnEurope1Taxes";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CURRENCY = "currency";
    public static final String ABSOLUTE = "absolute";
    public static final String TAX = "tax";
    public static final String VALUE = "value";


    public TaxRowModel()
    {
    }


    public TaxRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TaxRowModel(TaxModel _tax)
    {
        setTax(_tax);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TaxRowModel(ItemModel _owner, ProductTaxGroup _pg, ProductModel _product, String _productId, TaxModel _tax)
    {
        setOwner(_owner);
        setPg((HybrisEnumValue)_pg);
        setProduct(_product);
        setProductId(_productId);
        setTax(_tax);
    }


    @Accessor(qualifier = "absolute", type = Accessor.Type.GETTER)
    public Boolean getAbsolute()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "absolute");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "tax", type = Accessor.Type.GETTER)
    public TaxModel getTax()
    {
        return (TaxModel)getPersistenceContext().getPropertyValue("tax");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Double getValue()
    {
        return (Double)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "pg", type = Accessor.Type.SETTER)
    public void setPg(HybrisEnumValue value)
    {
        if(value == null || value instanceof ProductTaxGroup)
        {
            super.setPg(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.europe1.enums.ProductTaxGroup");
        }
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        super.setProduct(value);
    }


    @Accessor(qualifier = "tax", type = Accessor.Type.SETTER)
    public void setTax(TaxModel value)
    {
        getPersistenceContext().setPropertyValue("tax", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Double value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
