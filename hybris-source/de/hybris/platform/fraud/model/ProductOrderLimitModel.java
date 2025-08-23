package de.hybris.platform.fraud.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.IntervalResolution;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class ProductOrderLimitModel extends ItemModel
{
    public static final String _TYPECODE = "ProductOrderLimit";
    public static final String _PRODUCTPRODUCTORDERLIMITRELATION = "ProductProductOrderLimitRelation";
    public static final String CODE = "code";
    public static final String INTERVALRESOLUTION = "intervalResolution";
    public static final String INTERVALVALUE = "intervalValue";
    public static final String INTERVALMAXORDERSNUMBER = "intervalMaxOrdersNumber";
    public static final String MAXNUMBERPERORDER = "maxNumberPerOrder";
    public static final String PRODUCTS = "products";


    public ProductOrderLimitModel()
    {
    }


    public ProductOrderLimitModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductOrderLimitModel(String _code, Integer _intervalMaxOrdersNumber, IntervalResolution _intervalResolution, Integer _intervalValue, Integer _maxNumberPerOrder)
    {
        setCode(_code);
        setIntervalMaxOrdersNumber(_intervalMaxOrdersNumber);
        setIntervalResolution(_intervalResolution);
        setIntervalValue(_intervalValue);
        setMaxNumberPerOrder(_maxNumberPerOrder);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductOrderLimitModel(String _code, Integer _intervalMaxOrdersNumber, IntervalResolution _intervalResolution, Integer _intervalValue, Integer _maxNumberPerOrder, ItemModel _owner)
    {
        setCode(_code);
        setIntervalMaxOrdersNumber(_intervalMaxOrdersNumber);
        setIntervalResolution(_intervalResolution);
        setIntervalValue(_intervalValue);
        setMaxNumberPerOrder(_maxNumberPerOrder);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "intervalMaxOrdersNumber", type = Accessor.Type.GETTER)
    public Integer getIntervalMaxOrdersNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("intervalMaxOrdersNumber");
    }


    @Accessor(qualifier = "intervalResolution", type = Accessor.Type.GETTER)
    public IntervalResolution getIntervalResolution()
    {
        return (IntervalResolution)getPersistenceContext().getPropertyValue("intervalResolution");
    }


    @Accessor(qualifier = "intervalValue", type = Accessor.Type.GETTER)
    public Integer getIntervalValue()
    {
        return (Integer)getPersistenceContext().getPropertyValue("intervalValue");
    }


    @Accessor(qualifier = "maxNumberPerOrder", type = Accessor.Type.GETTER)
    public Integer getMaxNumberPerOrder()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxNumberPerOrder");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Set<ProductModel> getProducts()
    {
        return (Set<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "intervalMaxOrdersNumber", type = Accessor.Type.SETTER)
    public void setIntervalMaxOrdersNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("intervalMaxOrdersNumber", value);
    }


    @Accessor(qualifier = "intervalResolution", type = Accessor.Type.SETTER)
    public void setIntervalResolution(IntervalResolution value)
    {
        getPersistenceContext().setPropertyValue("intervalResolution", value);
    }


    @Accessor(qualifier = "intervalValue", type = Accessor.Type.SETTER)
    public void setIntervalValue(Integer value)
    {
        getPersistenceContext().setPropertyValue("intervalValue", value);
    }


    @Accessor(qualifier = "maxNumberPerOrder", type = Accessor.Type.SETTER)
    public void setMaxNumberPerOrder(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxNumberPerOrder", value);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Set<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }
}
