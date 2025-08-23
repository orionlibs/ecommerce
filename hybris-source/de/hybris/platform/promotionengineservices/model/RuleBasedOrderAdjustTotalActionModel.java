package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;

public class RuleBasedOrderAdjustTotalActionModel extends AbstractRuleBasedPromotionActionModel
{
    public static final String _TYPECODE = "RuleBasedOrderAdjustTotalAction";
    public static final String AMOUNT = "amount";


    public RuleBasedOrderAdjustTotalActionModel()
    {
    }


    public RuleBasedOrderAdjustTotalActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedOrderAdjustTotalActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public BigDecimal getAmount()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
    public void setAmount(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("amount", value);
    }
}
