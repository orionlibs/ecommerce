package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionOrderEntryAdjustActionModel extends AbstractPromotionActionModel
{
    public static final String _TYPECODE = "PromotionOrderEntryAdjustAction";
    public static final String AMOUNT = "amount";
    public static final String ORDERENTRYPRODUCT = "orderEntryProduct";
    public static final String ORDERENTRYQUANTITY = "orderEntryQuantity";
    public static final String ORDERENTRYNUMBER = "orderEntryNumber";


    public PromotionOrderEntryAdjustActionModel()
    {
    }


    public PromotionOrderEntryAdjustActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderEntryAdjustActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public Double getAmount()
    {
        return (Double)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.GETTER)
    public Integer getOrderEntryNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("orderEntryNumber");
    }


    @Accessor(qualifier = "orderEntryProduct", type = Accessor.Type.GETTER)
    public ProductModel getOrderEntryProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("orderEntryProduct");
    }


    @Accessor(qualifier = "orderEntryQuantity", type = Accessor.Type.GETTER)
    public Long getOrderEntryQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("orderEntryQuantity");
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
    public void setAmount(Double value)
    {
        getPersistenceContext().setPropertyValue("amount", value);
    }


    @Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.SETTER)
    public void setOrderEntryNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("orderEntryNumber", value);
    }


    @Accessor(qualifier = "orderEntryProduct", type = Accessor.Type.SETTER)
    public void setOrderEntryProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntryProduct", value);
    }


    @Accessor(qualifier = "orderEntryQuantity", type = Accessor.Type.SETTER)
    public void setOrderEntryQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("orderEntryQuantity", value);
    }
}
