package de.hybris.platform.cronjob.jalo;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCronJobHistory extends GenericItem
{
    public static final String CRONJOBCODE = "cronJobCode";
    public static final String JOBCODE = "jobCode";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String NODEID = "nodeID";
    public static final String SCHEDULED = "scheduled";
    public static final String USERUID = "userUid";
    public static final String STATUS = "status";
    public static final String RESULT = "result";
    public static final String FAILUREMESSAGE = "failureMessage";
    public static final String PROGRESS = "progress";
    public static final String STATUSLINE = "statusLine";
    public static final String CRONJOB = "cronJob";
    protected static final BidirectionalOneToManyHandler<GeneratedCronJobHistory> CRONJOBHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.CRONJOBHISTORY, false, "cronJob", "creationtime", false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("cronJobCode", Item.AttributeMode.INITIAL);
        tmp.put("jobCode", Item.AttributeMode.INITIAL);
        tmp.put("startTime", Item.AttributeMode.INITIAL);
        tmp.put("endTime", Item.AttributeMode.INITIAL);
        tmp.put("nodeID", Item.AttributeMode.INITIAL);
        tmp.put("scheduled", Item.AttributeMode.INITIAL);
        tmp.put("userUid", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("result", Item.AttributeMode.INITIAL);
        tmp.put("failureMessage", Item.AttributeMode.INITIAL);
        tmp.put("progress", Item.AttributeMode.INITIAL);
        tmp.put("statusLine", Item.AttributeMode.INITIAL);
        tmp.put("cronJob", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CRONJOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CronJob getCronJob(SessionContext ctx)
    {
        return (CronJob)getProperty(ctx, "cronJob");
    }


    public CronJob getCronJob()
    {
        return getCronJob(getSession().getSessionContext());
    }


    public void setCronJob(SessionContext ctx, CronJob value)
    {
        CRONJOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCronJob(CronJob value)
    {
        setCronJob(getSession().getSessionContext(), value);
    }


    public String getCronJobCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "cronJobCode");
    }


    public String getCronJobCode()
    {
        return getCronJobCode(getSession().getSessionContext());
    }


    protected void setCronJobCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'cronJobCode' is not changeable", 0);
        }
        setProperty(ctx, "cronJobCode", value);
    }


    protected void setCronJobCode(String value)
    {
        setCronJobCode(getSession().getSessionContext(), value);
    }


    public Date getEndTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endTime");
    }


    public Date getEndTime()
    {
        return getEndTime(getSession().getSessionContext());
    }


    public void setEndTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endTime", value);
    }


    public void setEndTime(Date value)
    {
        setEndTime(getSession().getSessionContext(), value);
    }


    public String getFailureMessage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "failureMessage");
    }


    public String getFailureMessage()
    {
        return getFailureMessage(getSession().getSessionContext());
    }


    public void setFailureMessage(SessionContext ctx, String value)
    {
        setProperty(ctx, "failureMessage", value);
    }


    public void setFailureMessage(String value)
    {
        setFailureMessage(getSession().getSessionContext(), value);
    }


    public String getJobCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "jobCode");
    }


    public String getJobCode()
    {
        return getJobCode(getSession().getSessionContext());
    }


    protected void setJobCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'jobCode' is not changeable", 0);
        }
        setProperty(ctx, "jobCode", value);
    }


    protected void setJobCode(String value)
    {
        setJobCode(getSession().getSessionContext(), value);
    }


    public Integer getNodeID(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "nodeID");
    }


    public Integer getNodeID()
    {
        return getNodeID(getSession().getSessionContext());
    }


    public int getNodeIDAsPrimitive(SessionContext ctx)
    {
        Integer value = getNodeID(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNodeIDAsPrimitive()
    {
        return getNodeIDAsPrimitive(getSession().getSessionContext());
    }


    public void setNodeID(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "nodeID", value);
    }


    public void setNodeID(Integer value)
    {
        setNodeID(getSession().getSessionContext(), value);
    }


    public void setNodeID(SessionContext ctx, int value)
    {
        setNodeID(ctx, Integer.valueOf(value));
    }


    public void setNodeID(int value)
    {
        setNodeID(getSession().getSessionContext(), value);
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


    public EnumerationValue getResult(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "result");
    }


    public EnumerationValue getResult()
    {
        return getResult(getSession().getSessionContext());
    }


    public void setResult(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "result", value);
    }


    public void setResult(EnumerationValue value)
    {
        setResult(getSession().getSessionContext(), value);
    }


    public Boolean isScheduled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "scheduled");
    }


    public Boolean isScheduled()
    {
        return isScheduled(getSession().getSessionContext());
    }


    public boolean isScheduledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isScheduled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isScheduledAsPrimitive()
    {
        return isScheduledAsPrimitive(getSession().getSessionContext());
    }


    public void setScheduled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "scheduled", value);
    }


    public void setScheduled(Boolean value)
    {
        setScheduled(getSession().getSessionContext(), value);
    }


    public void setScheduled(SessionContext ctx, boolean value)
    {
        setScheduled(ctx, Boolean.valueOf(value));
    }


    public void setScheduled(boolean value)
    {
        setScheduled(getSession().getSessionContext(), value);
    }


    public Date getStartTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startTime");
    }


    public Date getStartTime()
    {
        return getStartTime(getSession().getSessionContext());
    }


    protected void setStartTime(SessionContext ctx, Date value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'startTime' is not changeable", 0);
        }
        setProperty(ctx, "startTime", value);
    }


    protected void setStartTime(Date value)
    {
        setStartTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public String getStatusLine(SessionContext ctx)
    {
        return (String)getProperty(ctx, "statusLine");
    }


    public String getStatusLine()
    {
        return getStatusLine(getSession().getSessionContext());
    }


    public void setStatusLine(SessionContext ctx, String value)
    {
        setProperty(ctx, "statusLine", value);
    }


    public void setStatusLine(String value)
    {
        setStatusLine(getSession().getSessionContext(), value);
    }


    public String getUserUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "userUid");
    }


    public String getUserUid()
    {
        return getUserUid(getSession().getSessionContext());
    }


    public void setUserUid(SessionContext ctx, String value)
    {
        setProperty(ctx, "userUid", value);
    }


    public void setUserUid(String value)
    {
        setUserUid(getSession().getSessionContext(), value);
    }
}
