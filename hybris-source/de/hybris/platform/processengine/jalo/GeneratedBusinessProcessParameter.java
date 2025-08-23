package de.hybris.platform.processengine.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBusinessProcessParameter extends GenericItem
{
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String PROCESS = "process";
    protected static final BidirectionalOneToManyHandler<GeneratedBusinessProcessParameter> PROCESSHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.BUSINESSPROCESSPARAMETER, false, "process", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("process", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PROCESSHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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


    public BusinessProcess getProcess(SessionContext ctx)
    {
        return (BusinessProcess)getProperty(ctx, "process");
    }


    public BusinessProcess getProcess()
    {
        return getProcess(getSession().getSessionContext());
    }


    public void setProcess(SessionContext ctx, BusinessProcess value)
    {
        PROCESSHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setProcess(BusinessProcess value)
    {
        setProcess(getSession().getSessionContext(), value);
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
}
