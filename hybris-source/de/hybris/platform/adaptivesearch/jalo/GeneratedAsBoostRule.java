package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAsBoostRule extends AbstractAsBoostRuleConfiguration
{
    public static final String INDEXPROPERTY = "indexProperty";
    public static final String OPERATOR = "operator";
    public static final String VALUE = "value";
    public static final String BOOSTTYPE = "boostType";
    public static final String BOOST = "boost";
    public static final String SEARCHCONFIGURATIONPOS = "searchConfigurationPOS";
    public static final String SEARCHCONFIGURATION = "searchConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedAsBoostRule> SEARCHCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASBOOSTRULE, false, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsBoostRuleConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("indexProperty", Item.AttributeMode.INITIAL);
        tmp.put("operator", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("boostType", Item.AttributeMode.INITIAL);
        tmp.put("boost", Item.AttributeMode.INITIAL);
        tmp.put("searchConfigurationPOS", Item.AttributeMode.INITIAL);
        tmp.put("searchConfiguration", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Float getBoost(SessionContext ctx)
    {
        return (Float)getProperty(ctx, "boost");
    }


    public Float getBoost()
    {
        return getBoost(getSession().getSessionContext());
    }


    public float getBoostAsPrimitive(SessionContext ctx)
    {
        Float value = getBoost(ctx);
        return (value != null) ? value.floatValue() : 0.0F;
    }


    public float getBoostAsPrimitive()
    {
        return getBoostAsPrimitive(getSession().getSessionContext());
    }


    public void setBoost(SessionContext ctx, Float value)
    {
        setProperty(ctx, "boost", value);
    }


    public void setBoost(Float value)
    {
        setBoost(getSession().getSessionContext(), value);
    }


    public void setBoost(SessionContext ctx, float value)
    {
        setBoost(ctx, Float.valueOf(value));
    }


    public void setBoost(float value)
    {
        setBoost(getSession().getSessionContext(), value);
    }


    public EnumerationValue getBoostType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "boostType");
    }


    public EnumerationValue getBoostType()
    {
        return getBoostType(getSession().getSessionContext());
    }


    public void setBoostType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "boostType", value);
    }


    public void setBoostType(EnumerationValue value)
    {
        setBoostType(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SEARCHCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getIndexProperty(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexProperty");
    }


    public String getIndexProperty()
    {
        return getIndexProperty(getSession().getSessionContext());
    }


    protected void setIndexProperty(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'indexProperty' is not changeable", 0);
        }
        setProperty(ctx, "indexProperty", value);
    }


    protected void setIndexProperty(String value)
    {
        setIndexProperty(getSession().getSessionContext(), value);
    }


    public EnumerationValue getOperator(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "operator");
    }


    public EnumerationValue getOperator()
    {
        return getOperator(getSession().getSessionContext());
    }


    public void setOperator(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "operator", value);
    }


    public void setOperator(EnumerationValue value)
    {
        setOperator(getSession().getSessionContext(), value);
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


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
