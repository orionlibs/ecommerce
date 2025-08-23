package de.hybris.platform.ticket.events.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCsTicketResolutionEvent extends CsCustomerEvent
{
    public static final String RESOLUTIONTYPE = "resolutionType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CsCustomerEvent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("resolutionType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getResolutionType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "resolutionType");
    }


    public EnumerationValue getResolutionType()
    {
        return getResolutionType(getSession().getSessionContext());
    }


    protected void setResolutionType(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'resolutionType' is not changeable", 0);
        }
        setProperty(ctx, "resolutionType", value);
    }


    protected void setResolutionType(EnumerationValue value)
    {
        setResolutionType(getSession().getSessionContext(), value);
    }
}
