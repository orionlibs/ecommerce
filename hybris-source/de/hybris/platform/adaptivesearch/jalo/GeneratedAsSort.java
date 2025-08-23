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

public abstract class GeneratedAsSort extends AbstractAsSortConfiguration
{
    public static final String SEARCHCONFIGURATIONPOS = "searchConfigurationPOS";
    public static final String SEARCHCONFIGURATION = "searchConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedAsSort> SEARCHCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASSORT, false, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsSortConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("searchConfigurationPOS", Item.AttributeMode.INITIAL);
        tmp.put("searchConfiguration", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SEARCHCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public AbstractAsConfigurableSearchConfiguration getSearchConfiguration(SessionContext ctx)
    {
        return (AbstractAsConfigurableSearchConfiguration)getProperty(ctx, "searchConfiguration");
    }


    public AbstractAsConfigurableSearchConfiguration getSearchConfiguration()
    {
        return getSearchConfiguration(getSession().getSessionContext());
    }


    protected void setSearchConfiguration(SessionContext ctx, AbstractAsConfigurableSearchConfiguration value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'searchConfiguration' is not changeable", 0);
        }
        SEARCHCONFIGURATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setSearchConfiguration(AbstractAsConfigurableSearchConfiguration value)
    {
        setSearchConfiguration(getSession().getSessionContext(), value);
    }


    Integer getSearchConfigurationPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "searchConfigurationPOS");
    }


    Integer getSearchConfigurationPOS()
    {
        return getSearchConfigurationPOS(getSession().getSessionContext());
    }


    int getSearchConfigurationPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSearchConfigurationPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSearchConfigurationPOSAsPrimitive()
    {
        return getSearchConfigurationPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSearchConfigurationPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "searchConfigurationPOS", value);
    }


    void setSearchConfigurationPOS(Integer value)
    {
        setSearchConfigurationPOS(getSession().getSessionContext(), value);
    }


    void setSearchConfigurationPOS(SessionContext ctx, int value)
    {
        setSearchConfigurationPOS(ctx, Integer.valueOf(value));
    }


    void setSearchConfigurationPOS(int value)
    {
        setSearchConfigurationPOS(getSession().getSessionContext(), value);
    }
}
