package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.ExtensibleItem;
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

public abstract class GeneratedAsFacetRange extends AbstractAsItemConfiguration
{
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String QUALIFIER = "qualifier";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String UNIQUEIDX = "uniqueIdx";
    public static final String FACETCONFIGURATIONPOS = "facetConfigurationPOS";
    public static final String FACETCONFIGURATION = "facetConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedAsFacetRange> FACETCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASFACETRANGE, false, "facetConfiguration", "facetConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsItemConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        tmp.put("from", Item.AttributeMode.INITIAL);
        tmp.put("to", Item.AttributeMode.INITIAL);
        tmp.put("uniqueIdx", Item.AttributeMode.INITIAL);
        tmp.put("facetConfigurationPOS", Item.AttributeMode.INITIAL);
        tmp.put("facetConfiguration", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        FACETCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public AbstractAsFacetConfiguration getFacetConfiguration(SessionContext ctx)
    {
        return (AbstractAsFacetConfiguration)getProperty(ctx, "facetConfiguration");
    }


    public AbstractAsFacetConfiguration getFacetConfiguration()
    {
        return getFacetConfiguration(getSession().getSessionContext());
    }


    protected void setFacetConfiguration(SessionContext ctx, AbstractAsFacetConfiguration value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'facetConfiguration' is not changeable", 0);
        }
        FACETCONFIGURATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setFacetConfiguration(AbstractAsFacetConfiguration value)
    {
        setFacetConfiguration(getSession().getSessionContext(), value);
    }


    Integer getFacetConfigurationPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "facetConfigurationPOS");
    }


    Integer getFacetConfigurationPOS()
    {
        return getFacetConfigurationPOS(getSession().getSessionContext());
    }


    int getFacetConfigurationPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getFacetConfigurationPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getFacetConfigurationPOSAsPrimitive()
    {
        return getFacetConfigurationPOSAsPrimitive(getSession().getSessionContext());
    }


    void setFacetConfigurationPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "facetConfigurationPOS", value);
    }


    void setFacetConfigurationPOS(Integer value)
    {
        setFacetConfigurationPOS(getSession().getSessionContext(), value);
    }


    void setFacetConfigurationPOS(SessionContext ctx, int value)
    {
        setFacetConfigurationPOS(ctx, Integer.valueOf(value));
    }


    void setFacetConfigurationPOS(int value)
    {
        setFacetConfigurationPOS(getSession().getSessionContext(), value);
    }


    public String getFrom(SessionContext ctx)
    {
        return (String)getProperty(ctx, "from");
    }


    public String getFrom()
    {
        return getFrom(getSession().getSessionContext());
    }


    public void setFrom(SessionContext ctx, String value)
    {
        setProperty(ctx, "from", value);
    }


    public void setFrom(String value)
    {
        setFrom(getSession().getSessionContext(), value);
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    public void setId(SessionContext ctx, String value)
    {
        setProperty(ctx, "id", value);
    }


    public void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAsFacetRange.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedAsFacetRange.setName requires a session language", 0);
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


    public String getTo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "to");
    }


    public String getTo()
    {
        return getTo(getSession().getSessionContext());
    }


    public void setTo(SessionContext ctx, String value)
    {
        setProperty(ctx, "to", value);
    }


    public void setTo(String value)
    {
        setTo(getSession().getSessionContext(), value);
    }


    public String getUniqueIdx(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uniqueIdx");
    }


    public String getUniqueIdx()
    {
        return getUniqueIdx(getSession().getSessionContext());
    }


    public void setUniqueIdx(SessionContext ctx, String value)
    {
        setProperty(ctx, "uniqueIdx", value);
    }


    public void setUniqueIdx(String value)
    {
        setUniqueIdx(getSession().getSessionContext(), value);
    }
}
