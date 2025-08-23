package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedClassificationKeyword extends Keyword
{
    public static final String EXTERNALID = "externalID";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Keyword.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("externalID", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getExternalID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "externalID");
    }


    public String getExternalID()
    {
        return getExternalID(getSession().getSessionContext());
    }


    public void setExternalID(SessionContext ctx, String value)
    {
        setProperty(ctx, "externalID", value);
    }


    public void setExternalID(String value)
    {
        setExternalID(getSession().getSessionContext(), value);
    }
}
