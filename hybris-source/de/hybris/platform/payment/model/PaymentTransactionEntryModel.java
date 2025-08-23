package de.hybris.platform.payment.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Date;

public class PaymentTransactionEntryModel extends ItemModel
{
    public static final String _TYPECODE = "PaymentTransactionEntry";
    public static final String _PAYMENTTRANSACTION2PAYMENTTRANSACTIONENTRY = "PaymentTransaction2PaymentTransactionEntry";
    public static final String TYPE = "type";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String TIME = "time";
    public static final String TRANSACTIONSTATUS = "transactionStatus";
    public static final String TRANSACTIONSTATUSDETAILS = "transactionStatusDetails";
    public static final String REQUESTTOKEN = "requestToken";
    public static final String REQUESTID = "requestId";
    public static final String SUBSCRIPTIONID = "subscriptionID";
    public static final String CODE = "code";
    public static final String VERSIONID = "versionID";
    public static final String PAYMENTTRANSACTION = "paymentTransaction";


    public PaymentTransactionEntryModel()
    {
    }


    public PaymentTransactionEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentTransactionEntryModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentTransactionEntryModel(String _code, ItemModel _owner, String _versionID)
    {
        setCode(_code);
        setOwner(_owner);
        setVersionID(_versionID);
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


    @Accessor(qualifier = "paymentTransaction", type = Accessor.Type.GETTER)
    public PaymentTransactionModel getPaymentTransaction()
    {
        return (PaymentTransactionModel)getPersistenceContext().getPropertyValue("paymentTransaction");
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


    @Accessor(qualifier = "subscriptionID", type = Accessor.Type.GETTER)
    public String getSubscriptionID()
    {
        return (String)getPersistenceContext().getPropertyValue("subscriptionID");
    }


    @Accessor(qualifier = "time", type = Accessor.Type.GETTER)
    public Date getTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("time");
    }


    @Accessor(qualifier = "transactionStatus", type = Accessor.Type.GETTER)
    public String getTransactionStatus()
    {
        return (String)getPersistenceContext().getPropertyValue("transactionStatus");
    }


    @Accessor(qualifier = "transactionStatusDetails", type = Accessor.Type.GETTER)
    public String getTransactionStatusDetails()
    {
        return (String)getPersistenceContext().getPropertyValue("transactionStatusDetails");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public PaymentTransactionType getType()
    {
        return (PaymentTransactionType)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "versionID", type = Accessor.Type.GETTER)
    public String getVersionID()
    {
        return (String)getPersistenceContext().getPropertyValue("versionID");
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


    @Accessor(qualifier = "paymentTransaction", type = Accessor.Type.SETTER)
    public void setPaymentTransaction(PaymentTransactionModel value)
    {
        getPersistenceContext().setPropertyValue("paymentTransaction", value);
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


    @Accessor(qualifier = "subscriptionID", type = Accessor.Type.SETTER)
    public void setSubscriptionID(String value)
    {
        getPersistenceContext().setPropertyValue("subscriptionID", value);
    }


    @Accessor(qualifier = "time", type = Accessor.Type.SETTER)
    public void setTime(Date value)
    {
        getPersistenceContext().setPropertyValue("time", value);
    }


    @Accessor(qualifier = "transactionStatus", type = Accessor.Type.SETTER)
    public void setTransactionStatus(String value)
    {
        getPersistenceContext().setPropertyValue("transactionStatus", value);
    }


    @Accessor(qualifier = "transactionStatusDetails", type = Accessor.Type.SETTER)
    public void setTransactionStatusDetails(String value)
    {
        getPersistenceContext().setPropertyValue("transactionStatusDetails", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(PaymentTransactionType value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "versionID", type = Accessor.Type.SETTER)
    public void setVersionID(String value)
    {
        getPersistenceContext().setPropertyValue("versionID", value);
    }
}
