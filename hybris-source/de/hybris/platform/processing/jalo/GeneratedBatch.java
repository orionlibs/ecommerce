package de.hybris.platform.processing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBatch extends GenericItem
{
    public static final String ID = "id";
    public static final String EXECUTIONID = "executionId";
    public static final String TYPE = "type";
    public static final String REMAININGWORKLOAD = "remainingWorkLoad";
    public static final String PROCESS = "process";
    protected static final BidirectionalOneToManyHandler<GeneratedBatch> PROCESSHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.BATCH, false, "process", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("executionId", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("remainingWorkLoad", Item.AttributeMode.INITIAL);
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


    public String getExecutionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "executionId");
    }


    public String getExecutionId()
    {
        return getExecutionId(getSession().getSessionContext());
    }


    protected void setExecutionId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'executionId' is not changeable", 0);
        }
        setProperty(ctx, "executionId", value);
    }


    protected void setExecutionId(String value)
    {
        setExecutionId(getSession().getSessionContext(), value);
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


    public DistributedProcess getProcess(SessionContext ctx)
    {
        return (DistributedProcess)getProperty(ctx, "process");
    }


    public DistributedProcess getProcess()
    {
        return getProcess(getSession().getSessionContext());
    }


    protected void setProcess(SessionContext ctx, DistributedProcess value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'process' is not changeable", 0);
        }
        PROCESSHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setProcess(DistributedProcess value)
    {
        setProcess(getSession().getSessionContext(), value);
    }


    public Long getRemainingWorkLoad(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "remainingWorkLoad");
    }


    public Long getRemainingWorkLoad()
    {
        return getRemainingWorkLoad(getSession().getSessionContext());
    }


    public long getRemainingWorkLoadAsPrimitive(SessionContext ctx)
    {
        Long value = getRemainingWorkLoad(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getRemainingWorkLoadAsPrimitive()
    {
        return getRemainingWorkLoadAsPrimitive(getSession().getSessionContext());
    }


    protected void setRemainingWorkLoad(SessionContext ctx, Long value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'remainingWorkLoad' is not changeable", 0);
        }
        setProperty(ctx, "remainingWorkLoad", value);
    }


    protected void setRemainingWorkLoad(Long value)
    {
        setRemainingWorkLoad(getSession().getSessionContext(), value);
    }


    protected void setRemainingWorkLoad(SessionContext ctx, long value)
    {
        setRemainingWorkLoad(ctx, Long.valueOf(value));
    }


    protected void setRemainingWorkLoad(long value)
    {
        setRemainingWorkLoad(getSession().getSessionContext(), value);
    }


    public EnumerationValue getType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "type");
    }


    public EnumerationValue getType()
    {
        return getType(getSession().getSessionContext());
    }


    protected void setType(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'type' is not changeable", 0);
        }
        setProperty(ctx, "type", value);
    }


    protected void setType(EnumerationValue value)
    {
        setType(getSession().getSessionContext(), value);
    }
}
