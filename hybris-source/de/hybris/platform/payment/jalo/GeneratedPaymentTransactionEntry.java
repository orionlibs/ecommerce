package de.hybris.platform.payment.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.payment.constants.GeneratedPaymentConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPaymentTransactionEntry extends GenericItem
{
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
    protected static final BidirectionalOneToManyHandler<GeneratedPaymentTransactionEntry> PAYMENTTRANSACTIONHANDLER = new BidirectionalOneToManyHandler(GeneratedPaymentConstants.TC.PAYMENTTRANSACTIONENTRY, false, "paymentTransaction", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("amount", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("time", Item.AttributeMode.INITIAL);
        tmp.put("transactionStatus", Item.AttributeMode.INITIAL);
        tmp.put("transactionStatusDetails", Item.AttributeMode.INITIAL);
        tmp.put("requestToken", Item.AttributeMode.INITIAL);
        tmp.put("requestId", Item.AttributeMode.INITIAL);
        tmp.put("subscriptionID", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("versionID", Item.AttributeMode.INITIAL);
        tmp.put("paymentTransaction", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BigDecimal getAmount(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "amount");
    }


    public BigDecimal getAmount()
    {
        return getAmount(getSession().getSessionContext());
    }


    public void setAmount(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "amount", value);
    }


    public void setAmount(BigDecimal value)
    {
        setAmount(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PAYMENTTRANSACTIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public PaymentTransaction getPaymentTransaction(SessionContext ctx)
    {
        return (PaymentTransaction)getProperty(ctx, "paymentTransaction");
    }


    public PaymentTransaction getPaymentTransaction()
    {
        return getPaymentTransaction(getSession().getSessionContext());
    }


    public void setPaymentTransaction(SessionContext ctx, PaymentTransaction value)
    {
        PAYMENTTRANSACTIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPaymentTransaction(PaymentTransaction value)
    {
        setPaymentTransaction(getSession().getSessionContext(), value);
    }


    public String getRequestId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "requestId");
    }


    public String getRequestId()
    {
        return getRequestId(getSession().getSessionContext());
    }


    public void setRequestId(SessionContext ctx, String value)
    {
        setProperty(ctx, "requestId", value);
    }


    public void setRequestId(String value)
    {
        setRequestId(getSession().getSessionContext(), value);
    }


    public String getRequestToken(SessionContext ctx)
    {
        return (String)getProperty(ctx, "requestToken");
    }


    public String getRequestToken()
    {
        return getRequestToken(getSession().getSessionContext());
    }


    public void setRequestToken(SessionContext ctx, String value)
    {
        setProperty(ctx, "requestToken", value);
    }


    public void setRequestToken(String value)
    {
        setRequestToken(getSession().getSessionContext(), value);
    }


    public String getSubscriptionID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "subscriptionID");
    }


    public String getSubscriptionID()
    {
        return getSubscriptionID(getSession().getSessionContext());
    }


    public void setSubscriptionID(SessionContext ctx, String value)
    {
        setProperty(ctx, "subscriptionID", value);
    }


    public void setSubscriptionID(String value)
    {
        setSubscriptionID(getSession().getSessionContext(), value);
    }


    public Date getTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "time");
    }


    public Date getTime()
    {
        return getTime(getSession().getSessionContext());
    }


    public void setTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "time", value);
    }


    public void setTime(Date value)
    {
        setTime(getSession().getSessionContext(), value);
    }


    public String getTransactionStatus(SessionContext ctx)
    {
        return (String)getProperty(ctx, "transactionStatus");
    }


    public String getTransactionStatus()
    {
        return getTransactionStatus(getSession().getSessionContext());
    }


    public void setTransactionStatus(SessionContext ctx, String value)
    {
        setProperty(ctx, "transactionStatus", value);
    }


    public void setTransactionStatus(String value)
    {
        setTransactionStatus(getSession().getSessionContext(), value);
    }


    public String getTransactionStatusDetails(SessionContext ctx)
    {
        return (String)getProperty(ctx, "transactionStatusDetails");
    }


    public String getTransactionStatusDetails()
    {
        return getTransactionStatusDetails(getSession().getSessionContext());
    }


    public void setTransactionStatusDetails(SessionContext ctx, String value)
    {
        setProperty(ctx, "transactionStatusDetails", value);
    }


    public void setTransactionStatusDetails(String value)
    {
        setTransactionStatusDetails(getSession().getSessionContext(), value);
    }


    public EnumerationValue getType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "type");
    }


    public EnumerationValue getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(EnumerationValue value)
    {
        setType(getSession().getSessionContext(), value);
    }


    public String getVersionID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "versionID");
    }


    public String getVersionID()
    {
        return getVersionID(getSession().getSessionContext());
    }


    protected void setVersionID(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'versionID' is not changeable", 0);
        }
        setProperty(ctx, "versionID", value);
    }


    protected void setVersionID(String value)
    {
        setVersionID(getSession().getSessionContext(), value);
    }
}
