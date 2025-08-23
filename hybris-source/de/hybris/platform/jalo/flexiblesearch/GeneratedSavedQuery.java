package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSavedQuery extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PARAMTYPES = "paramtypes";
    public static final String PARAMS = "params";
    public static final String QUERY = "query";
    public static final String RESULTTYPE = "resultType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("paramtypes", Item.AttributeMode.INITIAL);
        tmp.put("params", Item.AttributeMode.INITIAL);
        tmp.put("query", Item.AttributeMode.INITIAL);
        tmp.put("resultType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSavedQuery.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSavedQuery.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSavedQuery.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedSavedQuery.setName requires a session language", 0);
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


    public Map<String, Type> getAllParams(SessionContext ctx)
    {
        Map<String, Type> map = (Map<String, Type>)getProperty(ctx, "params");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, Type> getAllParams()
    {
        return getAllParams(getSession().getSessionContext());
    }


    public void setAllParams(SessionContext ctx, Map<String, Type> value)
    {
        setProperty(ctx, "params", value);
    }


    public void setAllParams(Map<String, Type> value)
    {
        setAllParams(getSession().getSessionContext(), value);
    }


    Collection<Type> getParamtypes(SessionContext ctx)
    {
        Collection<Type> coll = (Collection<Type>)getProperty(ctx, "paramtypes");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    Collection<Type> getParamtypes()
    {
        return getParamtypes(getSession().getSessionContext());
    }


    void setParamtypes(SessionContext ctx, Collection<Type> value)
    {
        setProperty(ctx, "paramtypes", (value == null || !value.isEmpty()) ? value : null);
    }


    void setParamtypes(Collection<Type> value)
    {
        setParamtypes(getSession().getSessionContext(), value);
    }


    public String getQuery(SessionContext ctx)
    {
        return (String)getProperty(ctx, "query");
    }


    public String getQuery()
    {
        return getQuery(getSession().getSessionContext());
    }


    public void setQuery(SessionContext ctx, String value)
    {
        setProperty(ctx, "query", value);
    }


    public void setQuery(String value)
    {
        setQuery(getSession().getSessionContext(), value);
    }


    public ComposedType getResultType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "resultType");
    }


    public ComposedType getResultType()
    {
        return getResultType(getSession().getSessionContext());
    }


    public void setResultType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "resultType", value);
    }


    public void setResultType(ComposedType value)
    {
        setResultType(getSession().getSessionContext(), value);
    }
}
