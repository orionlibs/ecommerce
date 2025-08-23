package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedUserPasswordChangeAudit extends AbstractUserAudit
{
    public static final String ENCODEDPASSWORD = "encodedPassword";
    public static final String PASSWORDENCODING = "passwordEncoding";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractUserAudit.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("encodedPassword", Item.AttributeMode.INITIAL);
        tmp.put("passwordEncoding", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getEncodedPassword(SessionContext ctx)
    {
        return (String)getProperty(ctx, "encodedPassword");
    }


    public String getEncodedPassword()
    {
        return getEncodedPassword(getSession().getSessionContext());
    }


    protected void setEncodedPassword(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'encodedPassword' is not changeable", 0);
        }
        setProperty(ctx, "encodedPassword", value);
    }


    protected void setEncodedPassword(String value)
    {
        setEncodedPassword(getSession().getSessionContext(), value);
    }


    public String getPasswordEncoding(SessionContext ctx)
    {
        return (String)getProperty(ctx, "passwordEncoding");
    }


    public String getPasswordEncoding()
    {
        return getPasswordEncoding(getSession().getSessionContext());
    }


    public void setPasswordEncoding(SessionContext ctx, String value)
    {
        setProperty(ctx, "passwordEncoding", value);
    }


    public void setPasswordEncoding(String value)
    {
        setPasswordEncoding(getSession().getSessionContext(), value);
    }
}
