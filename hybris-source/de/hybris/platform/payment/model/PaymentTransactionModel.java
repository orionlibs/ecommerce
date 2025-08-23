package de.hybris.platform.payment.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.List;

public class PaymentTransactionModel extends ItemModel
{
    public static final String _TYPECODE = "PaymentTransaction";
    public static final String _ORDER2PAYMENTTRANSACTION = "Order2PaymentTransaction";
    public static final String CODE = "code";
    public static final String REQUESTID = "requestId";
    public static final String REQUESTTOKEN = "requestToken";
    public static final String PAYMENTPROVIDER = "paymentProvider";
    public static final String PLANNEDAMOUNT = "plannedAmount";
    public static final String CURRENCY = "currency";
    public static final String INFO = "info";
    public static final String VERSIONID = "versionID";
    public static final String ENTRIES = "entries";
    public static final String ORDER = "order";


    public PaymentTransactionModel()
    {
    }


    public PaymentTransactionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentTransactionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentTransactionModel(String _code, ItemModel _owner, String _versionID)
    {
        setCode(_code);
        setOwner(_owner);
        setVersionID(_versionID);
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


    @Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
    public List<PaymentTransactionEntryModel> getEntries()
    {
        return (List<PaymentTransactionEntryModel>)getPersistenceContext().getPropertyValue("entries");
    }


    @Accessor(qualifier = "info", type = Accessor.Type.GETTER)
    public PaymentInfoModel getInfo()
    {
        return (PaymentInfoModel)getPersistenceContext().getPropertyValue("info");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public AbstractOrderModel getOrder()
    {
        return (AbstractOrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "paymentProvider", type = Accessor.Type.GETTER)
    public String getPaymentProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("paymentProvider");
    }


    @Accessor(qualifier = "plannedAmount", type = Accessor.Type.GETTER)
    public BigDecimal getPlannedAmount()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("plannedAmount");
    }


    @Accessor(qualifier = "requestId", type = Accessor.Type.GETTER)
    public String getRequestId()
    {
        return (String)getPersistenceContext().getPropertyValue("requestId");
    }


    @Accessor(qualifier = "requestToken", type = Accessor.Type.GETTER)
    public String getRequestToken()
    {
        return (String)getPersistenceContext().getPropertyValue("requestToken");
    }


    @Accessor(qualifier = "versionID", type = Accessor.Type.GETTER)
    public String getVersionID()
    {
        return (String)getPersistenceContext().getPropertyValue("versionID");
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


    @Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
    public void setEntries(List<PaymentTransactionEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("entries", value);
    }


    @Accessor(qualifier = "info", type = Accessor.Type.SETTER)
    public void setInfo(PaymentInfoModel value)
    {
        getPersistenceContext().setPropertyValue("info", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "paymentProvider", type = Accessor.Type.SETTER)
    public void setPaymentProvider(String value)
    {
        getPersistenceContext().setPropertyValue("paymentProvider", value);
    }


    @Accessor(qualifier = "plannedAmount", type = Accessor.Type.SETTER)
    public void setPlannedAmount(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("plannedAmount", value);
    }


    @Accessor(qualifier = "requestId", type = Accessor.Type.SETTER)
    public void setRequestId(String value)
    {
        getPersistenceContext().setPropertyValue("requestId", value);
    }


    @Accessor(qualifier = "requestToken", type = Accessor.Type.SETTER)
    public void setRequestToken(String value)
    {
        getPersistenceContext().setPropertyValue("requestToken", value);
    }


    @Accessor(qualifier = "versionID", type = Accessor.Type.SETTER)
    public void setVersionID(String value)
    {
        getPersistenceContext().setPropertyValue("versionID", value);
    }
}
