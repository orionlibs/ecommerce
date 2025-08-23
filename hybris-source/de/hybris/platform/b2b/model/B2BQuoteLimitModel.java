package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Collection;

public class B2BQuoteLimitModel extends ItemModel
{
    public static final String _TYPECODE = "B2BQuoteLimit";
    public static final String _B2BUNIT2B2BQUOTELIMIT = "B2BUnit2B2BQuoteLimit";
    public static final String CODE = "code";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String UNIT = "Unit";


    public B2BQuoteLimitModel()
    {
    }


    public B2BQuoteLimitModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BQuoteLimitModel(BigDecimal _amount, String _code, CurrencyModel _currency)
    {
        setAmount(_amount);
        setCode(_code);
        setCurrency(_currency);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BQuoteLimitModel(BigDecimal _amount, String _code, CurrencyModel _currency, ItemModel _owner)
    {
        setAmount(_amount);
        setCode(_code);
        setCurrency(_currency);
        setOwner(_owner);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public BigDecimal getAmount()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.GETTER)
    public Collection<B2BUnitModel> getUnit()
    {
        return (Collection<B2BUnitModel>)getPersistenceContext().getPropertyValue("Unit");
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
    public void setAmount(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("amount", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.SETTER)
    public void setUnit(Collection<B2BUnitModel> value)
    {
        getPersistenceContext().setPropertyValue("Unit", value);
    }
}
