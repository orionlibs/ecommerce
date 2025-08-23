package de.hybris.platform.jalo.media;

import de.hybris.platform.constants.GeneratedCoreConstants;
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

public abstract class GeneratedMediaContainer extends GenericItem
{
    public static final String QUALIFIER = "qualifier";
    public static final String NAME = "name";
    public static final String MEDIAS = "medias";
    protected static final OneToManyHandler<Media> MEDIASHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.MEDIA, false, "mediaContainer", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Media> getMedias(SessionContext ctx)
    {
        return MEDIASHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Media> getMedias()
    {
        return getMedias(getSession().getSessionContext());
    }


    public void setMedias(SessionContext ctx, Collection<Media> value)
    {
        MEDIASHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setMedias(Collection<Media> value)
    {
        setMedias(getSession().getSessionContext(), value);
    }


    public void addToMedias(SessionContext ctx, Media value)
    {
        MEDIASHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToMedias(Media value)
    {
        addToMedias(getSession().getSessionContext(), value);
    }


    public void removeFromMedias(SessionContext ctx, Media value)
    {
        MEDIASHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromMedias(Media value)
    {
        removeFromMedias(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedMediaContainer.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedMediaContainer.setName requires a session language", 0);
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


    public String getQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "qualifier");
    }


    public String getQualifier()
    {
        return getQualifier(getSession().getSessionContext());
    }


    public void setQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "qualifier", value);
    }


    public void setQualifier(String value)
    {
        setQualifier(getSession().getSessionContext(), value);
    }
}
