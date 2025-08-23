package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDynamicWidgetPreferences extends WidgetPreferences
{
    public static final String PARAMETERS = "parameters";
    protected static final OneToManyHandler<WidgetParameter> PARAMETERSHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.WIDGETPARAMETER, false, "widgetPreferences", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(WidgetPreferences.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<WidgetParameter> getParameters(SessionContext ctx)
    {
        return PARAMETERSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<WidgetParameter> getParameters()
    {
        return getParameters(getSession().getSessionContext());
    }


    public void setParameters(SessionContext ctx, Collection<WidgetParameter> value)
    {
        PARAMETERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setParameters(Collection<WidgetParameter> value)
    {
        setParameters(getSession().getSessionContext(), value);
    }


    public void addToParameters(SessionContext ctx, WidgetParameter value)
    {
        PARAMETERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToParameters(WidgetParameter value)
    {
        addToParameters(getSession().getSessionContext(), value);
    }


    public void removeFromParameters(SessionContext ctx, WidgetParameter value)
    {
        PARAMETERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromParameters(WidgetParameter value)
    {
        removeFromParameters(getSession().getSessionContext(), value);
    }
}
