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

public abstract class GeneratedAsSortExpression extends AbstractAsItemConfiguration
{
    public static final String EXPRESSION = "expression";
    public static final String ORDER = "order";
    public static final String UNIQUEIDX = "uniqueIdx";
    public static final String SORTCONFIGURATIONPOS = "sortConfigurationPOS";
    public static final String SORTCONFIGURATION = "sortConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedAsSortExpression> SORTCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASSORTEXPRESSION, false, "sortConfiguration", "sortConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsItemConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("expression", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        tmp.put("uniqueIdx", Item.AttributeMode.INITIAL);
        tmp.put("sortConfigurationPOS", Item.AttributeMode.INITIAL);
        tmp.put("sortConfiguration", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SORTCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "expression");
    }


    public String getExpression()
    {
        return getExpression(getSession().getSessionContext());
    }


    public void setExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "expression", value);
    }


    public void setExpression(String value)
    {
        setExpression(getSession().getSessionContext(), value);
    }


    public EnumerationValue getOrder(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "order");
    }


    public EnumerationValue getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "order", value);
    }


    public void setOrder(EnumerationValue value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public AbstractAsSortConfiguration getSortConfiguration(SessionContext ctx)
    {
        return (AbstractAsSortConfiguration)getProperty(ctx, "sortConfiguration");
    }


    public AbstractAsSortConfiguration getSortConfiguration()
    {
        return getSortConfiguration(getSession().getSessionContext());
    }


    protected void setSortConfiguration(SessionContext ctx, AbstractAsSortConfiguration value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sortConfiguration' is not changeable", 0);
        }
        SORTCONFIGURATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setSortConfiguration(AbstractAsSortConfiguration value)
    {
        setSortConfiguration(getSession().getSessionContext(), value);
    }


    Integer getSortConfigurationPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "sortConfigurationPOS");
    }


    Integer getSortConfigurationPOS()
    {
        return getSortConfigurationPOS(getSession().getSessionContext());
    }


    int getSortConfigurationPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSortConfigurationPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSortConfigurationPOSAsPrimitive()
    {
        return getSortConfigurationPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSortConfigurationPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "sortConfigurationPOS", value);
    }


    void setSortConfigurationPOS(Integer value)
    {
        setSortConfigurationPOS(getSession().getSessionContext(), value);
    }


    void setSortConfigurationPOS(SessionContext ctx, int value)
    {
        setSortConfigurationPOS(ctx, Integer.valueOf(value));
    }


    void setSortConfigurationPOS(int value)
    {
        setSortConfigurationPOS(getSession().getSessionContext(), value);
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
