package de.hybris.platform.hmc.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedUserProfile extends GenericItem
{
    public static final String READABLELANGUAGES = "readableLanguages";
    public static final String WRITABLELANGUAGES = "writableLanguages";
    public static final String ALLREADABLELANGUAGES = "allReadableLanguages";
    public static final String ALLWRITABLELANGUAGES = "allWritableLanguages";
    public static final String EXPANDINITIAL = "expandInitial";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("readableLanguages", Item.AttributeMode.INITIAL);
        tmp.put("writableLanguages", Item.AttributeMode.INITIAL);
        tmp.put("expandInitial", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Language> getAllReadableLanguages()
    {
        return getAllReadableLanguages(getSession().getSessionContext());
    }


    public Collection<Language> getAllWritableLanguages()
    {
        return getAllWritableLanguages(getSession().getSessionContext());
    }


    public Boolean isExpandInitial(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "expandInitial");
    }


    public Boolean isExpandInitial()
    {
        return isExpandInitial(getSession().getSessionContext());
    }


    public boolean isExpandInitialAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExpandInitial(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExpandInitialAsPrimitive()
    {
        return isExpandInitialAsPrimitive(getSession().getSessionContext());
    }


    public void setExpandInitial(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "expandInitial", value);
    }


    public void setExpandInitial(Boolean value)
    {
        setExpandInitial(getSession().getSessionContext(), value);
    }


    public void setExpandInitial(SessionContext ctx, boolean value)
    {
        setExpandInitial(ctx, Boolean.valueOf(value));
    }


    public void setExpandInitial(boolean value)
    {
        setExpandInitial(getSession().getSessionContext(), value);
    }


    public List<Language> getReadableLanguages(SessionContext ctx)
    {
        List<Language> coll = (List<Language>)getProperty(ctx, "readableLanguages");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<Language> getReadableLanguages()
    {
        return getReadableLanguages(getSession().getSessionContext());
    }


    public void setReadableLanguages(SessionContext ctx, List<Language> value)
    {
        setProperty(ctx, "readableLanguages", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setReadableLanguages(List<Language> value)
    {
        setReadableLanguages(getSession().getSessionContext(), value);
    }


    public List<Language> getWritableLanguages(SessionContext ctx)
    {
        List<Language> coll = (List<Language>)getProperty(ctx, "writableLanguages");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<Language> getWritableLanguages()
    {
        return getWritableLanguages(getSession().getSessionContext());
    }


    public void setWritableLanguages(SessionContext ctx, List<Language> value)
    {
        setProperty(ctx, "writableLanguages", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setWritableLanguages(List<Language> value)
    {
        setWritableLanguages(getSession().getSessionContext(), value);
    }


    public abstract Collection<Language> getAllReadableLanguages(SessionContext paramSessionContext);


    public abstract Collection<Language> getAllWritableLanguages(SessionContext paramSessionContext);
}
