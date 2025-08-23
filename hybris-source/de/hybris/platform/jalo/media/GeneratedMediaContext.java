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

public abstract class GeneratedMediaContext extends GenericItem
{
    public static final String QUALIFIER = "qualifier";
    public static final String NAME = "name";
    public static final String MAPPINGS = "mappings";
    protected static final OneToManyHandler<MediaFormatMapping> MAPPINGSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.MEDIAFORMATMAPPING, true, "mediaContext", null, false, true, 0);
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


    public Collection<MediaFormatMapping> getMappings(SessionContext ctx)
    {
        return MAPPINGSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<MediaFormatMapping> getMappings()
    {
        return getMappings(getSession().getSessionContext());
    }


    public void setMappings(SessionContext ctx, Collection<MediaFormatMapping> value)
    {
        MAPPINGSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setMappings(Collection<MediaFormatMapping> value)
    {
        setMappings(getSession().getSessionContext(), value);
    }


    public void addToMappings(SessionContext ctx, MediaFormatMapping value)
    {
        MAPPINGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToMappings(MediaFormatMapping value)
    {
        addToMappings(getSession().getSessionContext(), value);
    }


    public void removeFromMappings(SessionContext ctx, MediaFormatMapping value)
    {
        MAPPINGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromMappings(MediaFormatMapping value)
    {
        removeFromMappings(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedMediaContext.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedMediaContext.setName requires a session language", 0);
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
