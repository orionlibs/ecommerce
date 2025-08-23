package de.hybris.platform.processengine.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.task.Task;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProcessTask extends Task
{
    public static final String ACTION = "action";
    public static final String PROCESS = "process";
    protected static final BidirectionalOneToManyHandler<GeneratedProcessTask> PROCESSHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.PROCESSTASK, false, "process", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Task.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("action", Item.AttributeMode.INITIAL);
        tmp.put("process", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getAction(SessionContext ctx)
    {
        return (String)getProperty(ctx, "action");
    }


    public String getAction()
    {
        return getAction(getSession().getSessionContext());
    }


    public void setAction(SessionContext ctx, String value)
    {
        setProperty(ctx, "action", value);
    }


    public void setAction(String value)
    {
        setAction(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PROCESSHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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
}
