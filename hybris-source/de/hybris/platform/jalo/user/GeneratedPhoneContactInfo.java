package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPhoneContactInfo extends AbstractContactInfo
{
    public static final String PHONENUMBER = "phoneNumber";
    public static final String TYPE = "type";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractContactInfo.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("phoneNumber", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getPhoneNumber(SessionContext ctx)
    {
        return (String)getProperty(ctx, "phoneNumber");
    }


    public String getPhoneNumber()
    {
        return getPhoneNumber(getSession().getSessionContext());
    }


    public void setPhoneNumber(SessionContext ctx, String value)
    {
        setProperty(ctx, "phoneNumber", value);
    }


    public void setPhoneNumber(String value)
    {
        setPhoneNumber(getSession().getSessionContext(), value);
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
}
