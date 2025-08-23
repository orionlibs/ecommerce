package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;

public class B2BCreditCheckResultModel extends B2BMerchantCheckResultModel
{
    public static final String _TYPECODE = "B2BCreditCheckResult";
    public static final String CURRENCY = "currency";
    public static final String CREDITLIMIT = "creditLimit";
    public static final String AMOUNTUTILISED = "amountUtilised";


    public B2BCreditCheckResultModel()
    {
    }


    public B2BCreditCheckResultModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCreditCheckResultModel(CurrencyModel _currency)
    {
        setCurrency(_currency);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCreditCheckResultModel(CurrencyModel _currency, ItemModel _owner)
    {
        setCurrency(_currency);
        setOwner(_owner);
    }


    @Accessor(qualifier = "amountUtilised", type = Accessor.Type.GETTER)
    public BigDecimal getAmountUtilised()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("amountUtilised");
    }


    @Accessor(qualifier = "creditLimit", type = Accessor.Type.GETTER)
    public BigDecimal getCreditLimit()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("creditLimit");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "amountUtilised", type = Accessor.Type.SETTER)
    public void setAmountUtilised(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("amountUtilised", value);
    }


    @Accessor(qualifier = "creditLimit", type = Accessor.Type.SETTER)
    public void setCreditLimit(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("creditLimit", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }
}
