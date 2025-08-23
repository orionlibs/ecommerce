package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxResults extends GenericItem
{
    public static final String KEY = "key";
    public static final String SESSIONKEY = "sessionKey";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String RESULTS = "results";
    public static final String ADDITIONALDATA = "additionalData";
    public static final String CALCULATIONTIME = "calculationTime";
    public static final String ANONYMOUS = "anonymous";
    public static final String DEFAULT = "default";
    public static final String USER = "user";
    protected static final BidirectionalOneToManyHandler<GeneratedCxResults> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXRESULTS, false, "user", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("key", Item.AttributeMode.INITIAL);
        tmp.put("sessionKey", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("results", Item.AttributeMode.INITIAL);
        tmp.put("additionalData", Item.AttributeMode.INITIAL);
        tmp.put("calculationTime", Item.AttributeMode.INITIAL);
        tmp.put("anonymous", Item.AttributeMode.INITIAL);
        tmp.put("default", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Object getAdditionalData(SessionContext ctx)
    {
        return getProperty(ctx, "additionalData");
    }


    public Object getAdditionalData()
    {
        return getAdditionalData(getSession().getSessionContext());
    }


    public void setAdditionalData(SessionContext ctx, Object value)
    {
        setProperty(ctx, "additionalData", value);
    }


    public void setAdditionalData(Object value)
    {
        setAdditionalData(getSession().getSessionContext(), value);
    }


    public Boolean isAnonymous(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "anonymous");
    }


    public Boolean isAnonymous()
    {
        return isAnonymous(getSession().getSessionContext());
    }


    public boolean isAnonymousAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAnonymous(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAnonymousAsPrimitive()
    {
        return isAnonymousAsPrimitive(getSession().getSessionContext());
    }


    public void setAnonymous(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "anonymous", value);
    }


    public void setAnonymous(Boolean value)
    {
        setAnonymous(getSession().getSessionContext(), value);
    }


    public void setAnonymous(SessionContext ctx, boolean value)
    {
        setAnonymous(ctx, Boolean.valueOf(value));
    }


    public void setAnonymous(boolean value)
    {
        setAnonymous(getSession().getSessionContext(), value);
    }


    public Date getCalculationTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "calculationTime");
    }


    public Date getCalculationTime()
    {
        return getCalculationTime(getSession().getSessionContext());
    }


    public void setCalculationTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "calculationTime", value);
    }


    public void setCalculationTime(Date value)
    {
        setCalculationTime(getSession().getSessionContext(), value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isDefault(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "default");
    }


    public Boolean isDefault()
    {
        return isDefault(getSession().getSessionContext());
    }


    public boolean isDefaultAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDefault(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDefaultAsPrimitive()
    {
        return isDefaultAsPrimitive(getSession().getSessionContext());
    }


    public void setDefault(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "default", value);
    }


    public void setDefault(Boolean value)
    {
        setDefault(getSession().getSessionContext(), value);
    }


    public void setDefault(SessionContext ctx, boolean value)
    {
        setDefault(ctx, Boolean.valueOf(value));
    }


    public void setDefault(boolean value)
    {
        setDefault(getSession().getSessionContext(), value);
    }


    public String getKey(SessionContext ctx)
    {
        return (String)getProperty(ctx, "key");
    }


    public String getKey()
    {
        return getKey(getSession().getSessionContext());
    }


    public void setKey(SessionContext ctx, String value)
    {
        setProperty(ctx, "key", value);
    }


    public void setKey(String value)
    {
        setKey(getSession().getSessionContext(), value);
    }


    public Object getResults(SessionContext ctx)
    {
        return getProperty(ctx, "results");
    }


    public Object getResults()
    {
        return getResults(getSession().getSessionContext());
    }


    public void setResults(SessionContext ctx, Object value)
    {
        setProperty(ctx, "results", value);
    }


    public void setResults(Object value)
    {
        setResults(getSession().getSessionContext(), value);
    }


    public String getSessionKey(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sessionKey");
    }


    public String getSessionKey()
    {
        return getSessionKey(getSession().getSessionContext());
    }


    public void setSessionKey(SessionContext ctx, String value)
    {
        setProperty(ctx, "sessionKey", value);
    }


    public void setSessionKey(String value)
    {
        setSessionKey(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
