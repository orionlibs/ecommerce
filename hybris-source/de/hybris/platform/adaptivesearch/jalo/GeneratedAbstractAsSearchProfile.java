package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractAsSearchProfile extends GenericItem
{
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String INDEXTYPE = "indexType";
    public static final String QUERYCONTEXT = "queryContext";
    public static final String ACTIVATIONSETPOS = "activationSetPOS";
    public static final String ACTIVATIONSET = "activationSet";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractAsSearchProfile> ACTIVATIONSETHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ABSTRACTASSEARCHPROFILE, false, "activationSet", "activationSetPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("indexType", Item.AttributeMode.INITIAL);
        tmp.put("queryContext", Item.AttributeMode.INITIAL);
        tmp.put("activationSetPOS", Item.AttributeMode.INITIAL);
        tmp.put("activationSet", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AsSearchProfileActivationSet getActivationSet(SessionContext ctx)
    {
        return (AsSearchProfileActivationSet)getProperty(ctx, "activationSet");
    }


    public AsSearchProfileActivationSet getActivationSet()
    {
        return getActivationSet(getSession().getSessionContext());
    }


    public void setActivationSet(SessionContext ctx, AsSearchProfileActivationSet value)
    {
        ACTIVATIONSETHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setActivationSet(AsSearchProfileActivationSet value)
    {
        setActivationSet(getSession().getSessionContext(), value);
    }


    Integer getActivationSetPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "activationSetPOS");
    }


    Integer getActivationSetPOS()
    {
        return getActivationSetPOS(getSession().getSessionContext());
    }


    int getActivationSetPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getActivationSetPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getActivationSetPOSAsPrimitive()
    {
        return getActivationSetPOSAsPrimitive(getSession().getSessionContext());
    }


    void setActivationSetPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "activationSetPOS", value);
    }


    void setActivationSetPOS(Integer value)
    {
        setActivationSetPOS(getSession().getSessionContext(), value);
    }


    void setActivationSetPOS(SessionContext ctx, int value)
    {
        setActivationSetPOS(ctx, Integer.valueOf(value));
    }


    void setActivationSetPOS(int value)
    {
        setActivationSetPOS(getSession().getSessionContext(), value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    protected void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'catalogVersion' is not changeable", 0);
        }
        setProperty(ctx, "catalogVersion", value);
    }


    protected void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
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
        ACTIVATIONSETHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getIndexType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexType");
    }


    public String getIndexType()
    {
        return getIndexType(getSession().getSessionContext());
    }


    protected void setIndexType(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'indexType' is not changeable", 0);
        }
        setProperty(ctx, "indexType", value);
    }


    protected void setIndexType(String value)
    {
        setIndexType(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractAsSearchProfile.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractAsSearchProfile.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public String getQueryContext(SessionContext ctx)
    {
        return (String)getProperty(ctx, "queryContext");
    }


    public String getQueryContext()
    {
        return getQueryContext(getSession().getSessionContext());
    }


    public void setQueryContext(SessionContext ctx, String value)
    {
        setProperty(ctx, "queryContext", value);
    }


    public void setQueryContext(String value)
    {
        setQueryContext(getSession().getSessionContext(), value);
    }
}
