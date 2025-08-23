package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWidgetParameter extends GenericItem
{
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String DEFAULTVALUEEXPRESSION = "defaultValueExpression";
    public static final String TARGETTYPE = "targetType";
    public static final String VALUE = "value";
    public static final String WIDGETPREFERENCES = "widgetPreferences";
    protected static final BidirectionalOneToManyHandler<GeneratedWidgetParameter> WIDGETPREFERENCESHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.WIDGETPARAMETER, false, "widgetPreferences", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("defaultValueExpression", Item.AttributeMode.INITIAL);
        tmp.put("targetType", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("widgetPreferences", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WIDGETPREFERENCESHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDefaultValueExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "defaultValueExpression");
    }


    public String getDefaultValueExpression()
    {
        return getDefaultValueExpression(getSession().getSessionContext());
    }


    public void setDefaultValueExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "defaultValueExpression", value);
    }


    public void setDefaultValueExpression(String value)
    {
        setDefaultValueExpression(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public String getTargetType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "targetType");
    }


    public String getTargetType()
    {
        return getTargetType(getSession().getSessionContext());
    }


    public void setTargetType(SessionContext ctx, String value)
    {
        setProperty(ctx, "targetType", value);
    }


    public void setTargetType(String value)
    {
        setTargetType(getSession().getSessionContext(), value);
    }


    public Type getType(SessionContext ctx)
    {
        return (Type)getProperty(ctx, "type");
    }


    public Type getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, Type value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(Type value)
    {
        setType(getSession().getSessionContext(), value);
    }


    public Object getValue(SessionContext ctx)
    {
        return getProperty(ctx, "value");
    }


    public Object getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(Object value)
    {
        setValue(getSession().getSessionContext(), value);
    }


    public DynamicWidgetPreferences getWidgetPreferences(SessionContext ctx)
    {
        return (DynamicWidgetPreferences)getProperty(ctx, "widgetPreferences");
    }


    public DynamicWidgetPreferences getWidgetPreferences()
    {
        return getWidgetPreferences(getSession().getSessionContext());
    }


    public void setWidgetPreferences(SessionContext ctx, DynamicWidgetPreferences value)
    {
        WIDGETPREFERENCESHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWidgetPreferences(DynamicWidgetPreferences value)
    {
        setWidgetPreferences(getSession().getSessionContext(), value);
    }
}
