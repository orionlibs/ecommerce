package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;

public class RuleBasedOrderEntryAdjustActionModel extends AbstractRuleBasedPromotionActionModel
{
    public static final String _TYPECODE = "RuleBasedOrderEntryAdjustAction";
    public static final String AMOUNT = "amount";
    public static final String ORDERENTRYPRODUCT = "orderEntryProduct";
    public static final String ORDERENTRYQUANTITY = "orderEntryQuantity";
    public static final String ORDERENTRYNUMBER = "orderEntryNumber";


    public RuleBasedOrderEntryAdjustActionModel()
    {
    }


    public RuleBasedOrderEntryAdjustActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedOrderEntryAdjustActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public BigDecimal getAmount()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("amount");
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
    public void setAmount(BigDecimal value)
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
