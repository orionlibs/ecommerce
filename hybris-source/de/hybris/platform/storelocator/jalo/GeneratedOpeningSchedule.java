package de.hybris.platform.storelocator.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOpeningSchedule extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String OPENINGDAYS = "openingDays";
    protected static final OneToManyHandler<OpeningDay> OPENINGDAYSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.OPENINGDAY, false, "openingSchedule", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
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


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedOpeningSchedule.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedOpeningSchedule.setName requires a session language", 0);
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


    public Collection<OpeningDay> getOpeningDays(SessionContext ctx)
    {
        return OPENINGDAYSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<OpeningDay> getOpeningDays()
    {
        return getOpeningDays(getSession().getSessionContext());
    }


    public void setOpeningDays(SessionContext ctx, Collection<OpeningDay> value)
    {
        OPENINGDAYSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setOpeningDays(Collection<OpeningDay> value)
    {
        setOpeningDays(getSession().getSessionContext(), value);
    }


    public void addToOpeningDays(SessionContext ctx, OpeningDay value)
    {
        OPENINGDAYSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToOpeningDays(OpeningDay value)
    {
        addToOpeningDays(getSession().getSessionContext(), value);
    }


    public void removeFromOpeningDays(SessionContext ctx, OpeningDay value)
    {
        OPENINGDAYSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromOpeningDays(OpeningDay value)
    {
        removeFromOpeningDays(getSession().getSessionContext(), value);
    }
}
