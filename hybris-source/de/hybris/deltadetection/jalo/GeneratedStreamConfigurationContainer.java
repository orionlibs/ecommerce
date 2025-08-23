package de.hybris.deltadetection.jalo;

import de.hybris.deltadetection.constants.GeneratedDeltadetectionConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedStreamConfigurationContainer extends GenericItem
{
    public static final String ID = "id";
    public static final String CONFIGURATIONS = "configurations";
    protected static final OneToManyHandler<StreamConfiguration> CONFIGURATIONSHANDLER = new OneToManyHandler(GeneratedDeltadetectionConstants.TC.STREAMCONFIGURATION, true, "container", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Set<StreamConfiguration> getConfigurations(SessionContext ctx)
    {
        return (Set<StreamConfiguration>)CONFIGURATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<StreamConfiguration> getConfigurations()
    {
        return getConfigurations(getSession().getSessionContext());
    }


    public void setConfigurations(SessionContext ctx, Set<StreamConfiguration> value)
    {
        CONFIGURATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConfigurations(Set<StreamConfiguration> value)
    {
        setConfigurations(getSession().getSessionContext(), value);
    }


    public void addToConfigurations(SessionContext ctx, StreamConfiguration value)
    {
        CONFIGURATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConfigurations(StreamConfiguration value)
    {
        addToConfigurations(getSession().getSessionContext(), value);
    }


    public void removeFromConfigurations(SessionContext ctx, StreamConfiguration value)
    {
        CONFIGURATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConfigurations(StreamConfiguration value)
    {
        removeFromConfigurations(getSession().getSessionContext(), value);
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    protected void setId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'id' is not changeable", 0);
        }
        setProperty(ctx, "id", value);
    }


    protected void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }
}
