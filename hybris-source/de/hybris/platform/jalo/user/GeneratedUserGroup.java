package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.PrincipalGroup;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedUserGroup extends PrincipalGroup
{
    public static final String WRITEABLELANGUAGES = "writeableLanguages";
    public static final String READABLELANGUAGES = "readableLanguages";
    public static final String HMCXML = "hmcXML";
    public static final String DENYWRITEPERMISSIONFORALLLANGUAGES = "denyWritePermissionForAllLanguages";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(PrincipalGroup.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("writeableLanguages", Item.AttributeMode.INITIAL);
        tmp.put("readableLanguages", Item.AttributeMode.INITIAL);
        tmp.put("hmcXML", Item.AttributeMode.INITIAL);
        tmp.put("denyWritePermissionForAllLanguages", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isDenyWritePermissionForAllLanguages(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "denyWritePermissionForAllLanguages");
    }


    public Boolean isDenyWritePermissionForAllLanguages()
    {
        return isDenyWritePermissionForAllLanguages(getSession().getSessionContext());
    }


    public boolean isDenyWritePermissionForAllLanguagesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDenyWritePermissionForAllLanguages(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDenyWritePermissionForAllLanguagesAsPrimitive()
    {
        return isDenyWritePermissionForAllLanguagesAsPrimitive(getSession().getSessionContext());
    }


    public void setDenyWritePermissionForAllLanguages(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "denyWritePermissionForAllLanguages", value);
    }


    public void setDenyWritePermissionForAllLanguages(Boolean value)
    {
        setDenyWritePermissionForAllLanguages(getSession().getSessionContext(), value);
    }


    public void setDenyWritePermissionForAllLanguages(SessionContext ctx, boolean value)
    {
        setDenyWritePermissionForAllLanguages(ctx, Boolean.valueOf(value));
    }


    public void setDenyWritePermissionForAllLanguages(boolean value)
    {
        setDenyWritePermissionForAllLanguages(getSession().getSessionContext(), value);
    }


    public String getHmcXML(SessionContext ctx)
    {
        return (String)getProperty(ctx, "hmcXML");
    }


    public String getHmcXML()
    {
        return getHmcXML(getSession().getSessionContext());
    }


    public void setHmcXML(SessionContext ctx, String value)
    {
        setProperty(ctx, "hmcXML", value);
    }


    public void setHmcXML(String value)
    {
        setHmcXML(getSession().getSessionContext(), value);
    }


    public Collection<Language> getReadableLanguages(SessionContext ctx)
    {
        Collection<Language> coll = (Collection<Language>)getProperty(ctx, "readableLanguages");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Language> getReadableLanguages()
    {
        return getReadableLanguages(getSession().getSessionContext());
    }


    public void setReadableLanguages(SessionContext ctx, Collection<Language> value)
    {
        setProperty(ctx, "readableLanguages", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setReadableLanguages(Collection<Language> value)
    {
        setReadableLanguages(getSession().getSessionContext(), value);
    }


    public Collection<Language> getWriteableLanguages(SessionContext ctx)
    {
        Collection<Language> coll = (Collection<Language>)getProperty(ctx, "writeableLanguages");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Language> getWriteableLanguages()
    {
        return getWriteableLanguages(getSession().getSessionContext());
    }


    public void setWriteableLanguages(SessionContext ctx, Collection<Language> value)
    {
        setProperty(ctx, "writeableLanguages", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setWriteableLanguages(Collection<Language> value)
    {
        setWriteableLanguages(getSession().getSessionContext(), value);
    }
}
