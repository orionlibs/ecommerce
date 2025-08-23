package de.hybris.platform.processing.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDistributedProcess extends GenericItem
{
    public static final String CODE = "code";
    public static final String HANDLERBEANID = "handlerBeanId";
    public static final String CURRENTEXECUTIONID = "currentExecutionId";
    public static final String STATE = "state";
    public static final String STOPREQUESTED = "stopRequested";
    public static final String NODEGROUP = "nodeGroup";
    public static final String STATUS = "status";
    public static final String EXTENDEDSTATUS = "extendedStatus";
    public static final String PROGRESS = "progress";
    public static final String BATCHES = "batches";
    protected static final OneToManyHandler<Batch> BATCHESHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.BATCH, true, "process", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("handlerBeanId", Item.AttributeMode.INITIAL);
        tmp.put("currentExecutionId", Item.AttributeMode.INITIAL);
        tmp.put("state", Item.AttributeMode.INITIAL);
        tmp.put("stopRequested", Item.AttributeMode.INITIAL);
        tmp.put("nodeGroup", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("extendedStatus", Item.AttributeMode.INITIAL);
        tmp.put("progress", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Batch> getBatches(SessionContext ctx)
    {
        return BATCHESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Batch> getBatches()
    {
        return getBatches(getSession().getSessionContext());
    }


    public void setBatches(SessionContext ctx, Collection<Batch> value)
    {
        BATCHESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setBatches(Collection<Batch> value)
    {
        setBatches(getSession().getSessionContext(), value);
    }


    public void addToBatches(SessionContext ctx, Batch value)
    {
        BATCHESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToBatches(Batch value)
    {
        addToBatches(getSession().getSessionContext(), value);
    }


    public void removeFromBatches(SessionContext ctx, Batch value)
    {
        BATCHESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromBatches(Batch value)
    {
        removeFromBatches(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public String getCurrentExecutionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "currentExecutionId");
    }


    public String getCurrentExecutionId()
    {
        return getCurrentExecutionId(getSession().getSessionContext());
    }


    public void setCurrentExecutionId(SessionContext ctx, String value)
    {
        setProperty(ctx, "currentExecutionId", value);
    }


    public void setCurrentExecutionId(String value)
    {
        setCurrentExecutionId(getSession().getSessionContext(), value);
    }


    public String getExtendedStatus(SessionContext ctx)
    {
        return (String)getProperty(ctx, "extendedStatus");
    }


    public String getExtendedStatus()
    {
        return getExtendedStatus(getSession().getSessionContext());
    }


    public void setExtendedStatus(SessionContext ctx, String value)
    {
        setProperty(ctx, "extendedStatus", value);
    }


    public void setExtendedStatus(String value)
    {
        setExtendedStatus(getSession().getSessionContext(), value);
    }


    public String getHandlerBeanId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "handlerBeanId");
    }


    public String getHandlerBeanId()
    {
        return getHandlerBeanId(getSession().getSessionContext());
    }


    protected void setHandlerBeanId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'handlerBeanId' is not changeable", 0);
        }
        setProperty(ctx, "handlerBeanId", value);
    }


    protected void setHandlerBeanId(String value)
    {
        setHandlerBeanId(getSession().getSessionContext(), value);
    }


    public String getNodeGroup(SessionContext ctx)
    {
        return (String)getProperty(ctx, "nodeGroup");
    }


    public String getNodeGroup()
    {
        return getNodeGroup(getSession().getSessionContext());
    }


    protected void setNodeGroup(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'nodeGroup' is not changeable", 0);
        }
        setProperty(ctx, "nodeGroup", value);
    }


    protected void setNodeGroup(String value)
    {
        setNodeGroup(getSession().getSessionContext(), value);
    }


    public Double getProgress(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "progress");
    }


    public Double getProgress()
    {
        return getProgress(getSession().getSessionContext());
    }


    public double getProgressAsPrimitive(SessionContext ctx)
    {
        Double value = getProgress(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getProgressAsPrimitive()
    {
        return getProgressAsPrimitive(getSession().getSessionContext());
    }


    public void setProgress(SessionContext ctx, Double value)
    {
        setProperty(ctx, "progress", value);
    }


    public void setProgress(Double value)
    {
        setProgress(getSession().getSessionContext(), value);
    }


    public void setProgress(SessionContext ctx, double value)
    {
        setProgress(ctx, Double.valueOf(value));
    }


    public void setProgress(double value)
    {
        setProgress(getSession().getSessionContext(), value);
    }


    public EnumerationValue getState(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "state");
    }


    public EnumerationValue getState()
    {
        return getState(getSession().getSessionContext());
    }


    public void setState(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "state", value);
    }


    public void setState(EnumerationValue value)
    {
        setState(getSession().getSessionContext(), value);
    }


    public String getStatus(SessionContext ctx)
    {
        return (String)getProperty(ctx, "status");
    }


    public String getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, String value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(String value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public Boolean isStopRequested(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "stopRequested");
    }


    public Boolean isStopRequested()
    {
        return isStopRequested(getSession().getSessionContext());
    }


    public boolean isStopRequestedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isStopRequested(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isStopRequestedAsPrimitive()
    {
        return isStopRequestedAsPrimitive(getSession().getSessionContext());
    }


    public void setStopRequested(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "stopRequested", value);
    }


    public void setStopRequested(Boolean value)
    {
        setStopRequested(getSession().getSessionContext(), value);
    }


    public void setStopRequested(SessionContext ctx, boolean value)
    {
        setStopRequested(ctx, Boolean.valueOf(value));
    }


    public void setStopRequested(boolean value)
    {
        setStopRequested(getSession().getSessionContext(), value);
    }
}
