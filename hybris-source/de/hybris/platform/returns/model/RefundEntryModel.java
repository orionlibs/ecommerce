package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Date;

public class RefundEntryModel extends ReturnEntryModel
{
    public static final String _TYPECODE = "RefundEntry";
    public static final String REASON = "reason";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String REFUNDEDDATE = "refundedDate";


    public RefundEntryModel()
    {
    }


    public RefundEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RefundEntryModel(ReturnAction _action, AbstractOrderEntryModel _orderEntry, RefundReason _reason, ReturnStatus _status)
    {
        setAction(_action);
        setOrderEntry(_orderEntry);
        setReason(_reason);
        setStatus(_status);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RefundEntryModel(ReturnAction _action, AbstractOrderEntryModel _orderEntry, ItemModel _owner, RefundReason _reason, ReturnStatus _status)
    {
        setAction(_action);
        setOrderEntry(_orderEntry);
        setOwner(_owner);
        setReason(_reason);
        setStatus(_status);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public BigDecimal getAmount()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "reason", type = Accessor.Type.GETTER)
    public RefundReason getReason()
    {
        return (RefundReason)getPersistenceContext().getPropertyValue("reason");
    }


    @Accessor(qualifier = "refundedDate", type = Accessor.Type.GETTER)
    public Date getRefundedDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("refundedDate");
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
    public void setAmount(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("amount", value);
    }


    @Accessor(qualifier = "reason", type = Accessor.Type.SETTER)
    public void setReason(RefundReason value)
    {
        getPersistenceContext().setPropertyValue("reason", value);
    }


    @Accessor(qualifier = "refundedDate", type = Accessor.Type.SETTER)
    public void setRefundedDate(Date value)
    {
        getPersistenceContext().setPropertyValue("refundedDate", value);
    }
}
