package de.hybris.platform.personalizationsearch.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.personalizationservices.jalo.CxAbstractAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxSearchProfileAction extends CxAbstractAction
{
    public static final String SEARCHPROFILECODE = "searchProfileCode";
    public static final String SEARCHPROFILECATALOG = "searchProfileCatalog";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CxAbstractAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("searchProfileCode", Item.AttributeMode.INITIAL);
        tmp.put("searchProfileCatalog", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getSearchProfileCatalog(SessionContext ctx)
    {
        return (String)getProperty(ctx, "searchProfileCatalog");
    }


    public String getSearchProfileCatalog()
    {
        return getSearchProfileCatalog(getSession().getSessionContext());
    }


    public void setSearchProfileCatalog(SessionContext ctx, String value)
    {
        setProperty(ctx, "searchProfileCatalog", value);
    }


    public void setSearchProfileCatalog(String value)
    {
        setSearchProfileCatalog(getSession().getSessionContext(), value);
    }


    public String getSearchProfileCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "searchProfileCode");
    }


    public String getSearchProfileCode()
    {
        return getSearchProfileCode(getSession().getSessionContext());
    }


    public void setSearchProfileCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "searchProfileCode", value);
    }


    public void setSearchProfileCode(String value)
    {
        setSearchProfileCode(getSession().getSessionContext(), value);
    }
}
