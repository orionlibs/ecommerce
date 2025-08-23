package de.hybris.platform.ordercancel.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.ordermodify.jalo.OrderModificationRecordEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderCancelRecordEntry extends OrderModificationRecordEntry
{
    public static final String REFUSEDMESSAGE = "refusedMessage";
    public static final String CANCELRESULT = "cancelResult";
    public static final String CANCELREASON = "cancelReason";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OrderModificationRecordEntry.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("refusedMessage", Item.AttributeMode.INITIAL);
        tmp.put("cancelResult", Item.AttributeMode.INITIAL);
        tmp.put("cancelReason", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getCancelReason(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "cancelReason");
    }


    public EnumerationValue getCancelReason()
    {
        return getCancelReason(getSession().getSessionContext());
    }


    public void setCancelReason(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "cancelReason", value);
    }


    public void setCancelReason(EnumerationValue value)
    {
        setCancelReason(getSession().getSessionContext(), value);
    }


    public EnumerationValue getCancelResult(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "cancelResult");
    }


    public EnumerationValue getCancelResult()
    {
        return getCancelResult(getSession().getSessionContext());
    }


    public void setCancelResult(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "cancelResult", value);
    }


    public void setCancelResult(EnumerationValue value)
    {
        setCancelResult(getSession().getSessionContext(), value);
    }


    public String getRefusedMessage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "refusedMessage");
    }


    public String getRefusedMessage()
    {
        return getRefusedMessage(getSession().getSessionContext());
    }


    public void setRefusedMessage(SessionContext ctx, String value)
    {
        setProperty(ctx, "refusedMessage", value);
    }


    public void setRefusedMessage(String value)
    {
        setRefusedMessage(getSession().getSessionContext(), value);
    }
}
