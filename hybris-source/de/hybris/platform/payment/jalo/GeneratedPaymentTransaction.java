package de.hybris.platform.payment.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.payment.constants.GeneratedPaymentConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedPaymentTransaction extends GenericItem
{
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
    protected static final OneToManyHandler<PaymentTransactionEntry> ENTRIESHANDLER = new OneToManyHandler(GeneratedPaymentConstants.TC.PAYMENTTRANSACTIONENTRY, true, "paymentTransaction", null, false, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedPaymentTransaction> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedPaymentConstants.TC.PAYMENTTRANSACTION, false, "order", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("requestId", Item.AttributeMode.INITIAL);
        tmp.put("requestToken", Item.AttributeMode.INITIAL);
        tmp.put("paymentProvider", Item.AttributeMode.INITIAL);
        tmp.put("plannedAmount", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("info", Item.AttributeMode.INITIAL);
        tmp.put("versionID", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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
        ORDERHANDLER.newInstance(ctx, allAttributes);
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


    public List<PaymentTransactionEntry> getEntries(SessionContext ctx)
    {
        return (List<PaymentTransactionEntry>)ENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<PaymentTransactionEntry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    public void setEntries(SessionContext ctx, List<PaymentTransactionEntry> value)
    {
        ENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEntries(List<PaymentTransactionEntry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    public void addToEntries(SessionContext ctx, PaymentTransactionEntry value)
    {
        ENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEntries(PaymentTransactionEntry value)
    {
        addToEntries(getSession().getSessionContext(), value);
    }


    public void removeFromEntries(SessionContext ctx, PaymentTransactionEntry value)
    {
        ENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEntries(PaymentTransactionEntry value)
    {
        removeFromEntries(getSession().getSessionContext(), value);
    }


    public PaymentInfo getInfo(SessionContext ctx)
    {
        return (PaymentInfo)getProperty(ctx, "info");
    }


    public PaymentInfo getInfo()
    {
        return getInfo(getSession().getSessionContext());
    }


    public void setInfo(SessionContext ctx, PaymentInfo value)
    {
        setProperty(ctx, "info", value);
    }


    public void setInfo(PaymentInfo value)
    {
        setInfo(getSession().getSessionContext(), value);
    }


    public AbstractOrder getOrder(SessionContext ctx)
    {
        return (AbstractOrder)getProperty(ctx, "order");
    }


    public AbstractOrder getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, AbstractOrder value)
    {
        ORDERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOrder(AbstractOrder value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public String getPaymentProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "paymentProvider");
    }


    public String getPaymentProvider()
    {
        return getPaymentProvider(getSession().getSessionContext());
    }


    public void setPaymentProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "paymentProvider", value);
    }


    public void setPaymentProvider(String value)
    {
        setPaymentProvider(getSession().getSessionContext(), value);
    }


    public BigDecimal getPlannedAmount(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "plannedAmount");
    }


    public BigDecimal getPlannedAmount()
    {
        return getPlannedAmount(getSession().getSessionContext());
    }


    public void setPlannedAmount(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "plannedAmount", value);
    }


    public void setPlannedAmount(BigDecimal value)
    {
        setPlannedAmount(getSession().getSessionContext(), value);
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
