package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAsExcludedFacetValue extends AbstractAsFacetValueConfiguration
{
    public static final String FACETCONFIGURATIONPOS = "facetConfigurationPOS";
    public static final String FACETCONFIGURATION = "facetConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedAsExcludedFacetValue> FACETCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASEXCLUDEDFACETVALUE, false, "facetConfiguration", "facetConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsFacetValueConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
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
}
