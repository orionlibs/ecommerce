package de.hybris.platform.paymentstandard.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.paymentstandard.constants.GeneratedStandardPaymentModeConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedStandardPaymentMode extends PaymentMode
{
    public static final String NET = "net";
    public static final String PAYMENTMODEVALUES = "paymentModeValues";
    protected static final OneToManyHandler<StandardPaymentModeValue> PAYMENTMODEVALUESHANDLER = new OneToManyHandler(GeneratedStandardPaymentModeConstants.TC.STANDARDPAYMENTMODEVALUE, true, "paymentMode", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(PaymentMode.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("net", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isNet(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "net");
    }


    public Boolean isNet()
    {
        return isNet(getSession().getSessionContext());
    }


    public boolean isNetAsPrimitive(SessionContext ctx)
    {
        Boolean value = isNet(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isNetAsPrimitive()
    {
        return isNetAsPrimitive(getSession().getSessionContext());
    }


    public void setNet(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "net", value);
    }


    public void setNet(Boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public void setNet(SessionContext ctx, boolean value)
    {
        setNet(ctx, Boolean.valueOf(value));
    }


    public void setNet(boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public Collection<StandardPaymentModeValue> getPaymentModeValues(SessionContext ctx)
    {
        return PAYMENTMODEVALUESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<StandardPaymentModeValue> getPaymentModeValues()
    {
        return getPaymentModeValues(getSession().getSessionContext());
    }


    public void setPaymentModeValues(SessionContext ctx, Collection<StandardPaymentModeValue> value)
    {
        PAYMENTMODEVALUESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPaymentModeValues(Collection<StandardPaymentModeValue> value)
    {
        setPaymentModeValues(getSession().getSessionContext(), value);
    }


    public void addToPaymentModeValues(SessionContext ctx, StandardPaymentModeValue value)
    {
        PAYMENTMODEVALUESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPaymentModeValues(StandardPaymentModeValue value)
    {
        addToPaymentModeValues(getSession().getSessionContext(), value);
    }


    public void removeFromPaymentModeValues(SessionContext ctx, StandardPaymentModeValue value)
    {
        PAYMENTMODEVALUESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPaymentModeValues(StandardPaymentModeValue value)
    {
        removeFromPaymentModeValues(getSession().getSessionContext(), value);
    }
}
