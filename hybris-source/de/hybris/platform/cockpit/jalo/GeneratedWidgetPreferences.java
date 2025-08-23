package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWidgetPreferences extends GenericItem
{
    public static final String TITLE = "title";
    public static final String OWNERUSER = "ownerUser";
    protected static final BidirectionalOneToManyHandler<GeneratedWidgetPreferences> OWNERUSERHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.WIDGETPREFERENCES, false, "ownerUser", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("title", Item.AttributeMode.INITIAL);
        tmp.put("ownerUser", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        OWNERUSERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public User getOwnerUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "ownerUser");
    }


    public User getOwnerUser()
    {
        return getOwnerUser(getSession().getSessionContext());
    }


    public void setOwnerUser(SessionContext ctx, User value)
    {
        OWNERUSERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOwnerUser(User value)
    {
        setOwnerUser(getSession().getSessionContext(), value);
    }


    public String getTitle(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWidgetPreferences.getTitle requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "title");
    }


    public String getTitle()
    {
        return getTitle(getSession().getSessionContext());
    }


    public Map<Language, String> getAllTitle(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "title", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllTitle()
    {
        return getAllTitle(getSession().getSessionContext());
    }


    public void setTitle(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWidgetPreferences.setTitle requires a session language", 0);
        }
        setLocalizedProperty(ctx, "title", value);
    }


    public void setTitle(String value)
    {
        setTitle(getSession().getSessionContext(), value);
    }


    public void setAllTitle(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "title", value);
    }


    public void setAllTitle(Map<Language, String> value)
    {
        setAllTitle(getSession().getSessionContext(), value);
    }
}
