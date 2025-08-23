package de.hybris.platform.payment.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.payment.constants.GeneratedPaymentConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedPaymentManager extends Extension
{
    protected static final OneToManyHandler<PaymentTransaction> ORDER2PAYMENTTRANSACTIONPAYMENTTRANSACTIONSHANDLER = new OneToManyHandler(GeneratedPaymentConstants.TC.PAYMENTTRANSACTION, true, "order", null, false, true, 2);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("billingAddress", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.payment.PaymentInfo", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("subscriptionId", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.payment.PaymentInfo", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Address getBillingAddress(SessionContext ctx, PaymentInfo item)
    {
        return (Address)item.getProperty(ctx, GeneratedPaymentConstants.Attributes.PaymentInfo.BILLINGADDRESS);
    }


    public Address getBillingAddress(PaymentInfo item)
    {
        return getBillingAddress(getSession().getSessionContext(), item);
    }


    public void setBillingAddress(SessionContext ctx, PaymentInfo item, Address value)
    {
        (new Object(this, item))
                        .setValue(ctx, value);
    }


    public void setBillingAddress(PaymentInfo item, Address value)
    {
        setBillingAddress(getSession().getSessionContext(), item, value);
    }


    public PaymentTransaction createPaymentTransaction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPaymentConstants.TC.PAYMENTTRANSACTION);
            return (PaymentTransaction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PaymentTransaction : " + e.getMessage(), 0);
        }
    }


    public PaymentTransaction createPaymentTransaction(Map attributeValues)
    {
        return createPaymentTransaction(getSession().getSessionContext(), attributeValues);
    }


    public PaymentTransactionEntry createPaymentTransactionEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPaymentConstants.TC.PAYMENTTRANSACTIONENTRY);
            return (PaymentTransactionEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PaymentTransactionEntry : " + e.getMessage(), 0);
        }
    }


    public PaymentTransactionEntry createPaymentTransactionEntry(Map attributeValues)
    {
        return createPaymentTransactionEntry(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "payment";
    }


    public List<PaymentTransaction> getPaymentTransactions(SessionContext ctx, AbstractOrder item)
    {
        return (List<PaymentTransaction>)ORDER2PAYMENTTRANSACTIONPAYMENTTRANSACTIONSHANDLER.getValues(ctx, (Item)item);
    }


    public List<PaymentTransaction> getPaymentTransactions(AbstractOrder item)
    {
        return getPaymentTransactions(getSession().getSessionContext(), item);
    }


    public void setPaymentTransactions(SessionContext ctx, AbstractOrder item, List<PaymentTransaction> value)
    {
        ORDER2PAYMENTTRANSACTIONPAYMENTTRANSACTIONSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setPaymentTransactions(AbstractOrder item, List<PaymentTransaction> value)
    {
        setPaymentTransactions(getSession().getSessionContext(), item, value);
    }


    public void addToPaymentTransactions(SessionContext ctx, AbstractOrder item, PaymentTransaction value)
    {
        ORDER2PAYMENTTRANSACTIONPAYMENTTRANSACTIONSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToPaymentTransactions(AbstractOrder item, PaymentTransaction value)
    {
        addToPaymentTransactions(getSession().getSessionContext(), item, value);
    }


    public void removeFromPaymentTransactions(SessionContext ctx, AbstractOrder item, PaymentTransaction value)
    {
        ORDER2PAYMENTTRANSACTIONPAYMENTTRANSACTIONSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromPaymentTransactions(AbstractOrder item, PaymentTransaction value)
    {
        removeFromPaymentTransactions(getSession().getSessionContext(), item, value);
    }


    public String getSubscriptionId(SessionContext ctx, PaymentInfo item)
    {
        return (String)item.getProperty(ctx, GeneratedPaymentConstants.Attributes.CreditCardPaymentInfo.SUBSCRIPTIONID);
    }


    public String getSubscriptionId(PaymentInfo item)
    {
        return getSubscriptionId(getSession().getSessionContext(), item);
    }


    public void setSubscriptionId(SessionContext ctx, PaymentInfo item, String value)
    {
        item.setProperty(ctx, GeneratedPaymentConstants.Attributes.CreditCardPaymentInfo.SUBSCRIPTIONID, value);
    }


    public void setSubscriptionId(PaymentInfo item, String value)
    {
        setSubscriptionId(getSession().getSessionContext(), item, value);
    }
}
