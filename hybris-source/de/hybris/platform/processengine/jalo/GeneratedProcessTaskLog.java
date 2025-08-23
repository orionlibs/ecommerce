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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProcessTaskLog extends GenericItem
{
    public static final String RETURNCODE = "returnCode";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String ACTIONID = "actionId";
    public static final String CLUSTERID = "clusterId";
    public static final String LOGMESSAGES = "logMessages";
    public static final String PROCESS = "process";
    protected static final BidirectionalOneToManyHandler<GeneratedProcessTaskLog> PROCESSHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.PROCESSTASKLOG, false, "process", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("returnCode", Item.AttributeMode.INITIAL);
        tmp.put("startDate", Item.AttributeMode.INITIAL);
        tmp.put("endDate", Item.AttributeMode.INITIAL);
        tmp.put("actionId", Item.AttributeMode.INITIAL);
        tmp.put("clusterId", Item.AttributeMode.INITIAL);
        tmp.put("logMessages", Item.AttributeMode.INITIAL);
        tmp.put("process", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getActionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "actionId");
    }


    public String getActionId()
    {
        return getActionId(getSession().getSessionContext());
    }


    public void setActionId(SessionContext ctx, String value)
    {
        setProperty(ctx, "actionId", value);
    }


    public void setActionId(String value)
    {
        setActionId(getSession().getSessionContext(), value);
    }


    public Integer getClusterId(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "clusterId");
    }


    public Integer getClusterId()
    {
        return getClusterId(getSession().getSessionContext());
    }


    public int getClusterIdAsPrimitive(SessionContext ctx)
    {
        Integer value = getClusterId(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getClusterIdAsPrimitive()
    {
        return getClusterIdAsPrimitive(getSession().getSessionContext());
    }


    public void setClusterId(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "clusterId", value);
    }


    public void setClusterId(Integer value)
    {
        setClusterId(getSession().getSessionContext(), value);
    }


    public void setClusterId(SessionContext ctx, int value)
    {
        setClusterId(ctx, Integer.valueOf(value));
    }


    public void setClusterId(int value)
    {
        setClusterId(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PROCESSHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Date getEndDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endDate");
    }


    public Date getEndDate()
    {
        return getEndDate(getSession().getSessionContext());
    }


    public void setEndDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endDate", value);
    }


    public void setEndDate(Date value)
    {
        setEndDate(getSession().getSessionContext(), value);
    }


    public String getLogMessages(SessionContext ctx)
    {
        return (String)getProperty(ctx, "logMessages");
    }


    public String getLogMessages()
    {
        return getLogMessages(getSession().getSessionContext());
    }


    public void setLogMessages(SessionContext ctx, String value)
    {
        setProperty(ctx, "logMessages", value);
    }


    public void setLogMessages(String value)
    {
        setLogMessages(getSession().getSessionContext(), value);
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


    public String getReturnCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "returnCode");
    }


    public String getReturnCode()
    {
        return getReturnCode(getSession().getSessionContext());
    }


    public void setReturnCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "returnCode", value);
    }


    public void setReturnCode(String value)
    {
        setReturnCode(getSession().getSessionContext(), value);
    }


    public Date getStartDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startDate");
    }


    public Date getStartDate()
    {
        return getStartDate(getSession().getSessionContext());
    }


    public void setStartDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startDate", value);
    }


    public void setStartDate(Date value)
    {
        setStartDate(getSession().getSessionContext(), value);
    }
}
