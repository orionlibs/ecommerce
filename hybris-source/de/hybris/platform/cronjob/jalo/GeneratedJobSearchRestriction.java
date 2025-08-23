package de.hybris.platform.cronjob.jalo;

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

public abstract class GeneratedJobSearchRestriction extends GenericItem
{
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String QUERY = "query";
    public static final String JOBPOS = "jobPOS";
    public static final String JOB = "job";
    protected static final BidirectionalOneToManyHandler<GeneratedJobSearchRestriction> JOBHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.JOBSEARCHRESTRICTION, false, "job", "jobPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("query", Item.AttributeMode.INITIAL);
        tmp.put("jobPOS", Item.AttributeMode.INITIAL);
        tmp.put("job", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Job getJob(SessionContext ctx)
    {
        return (Job)getProperty(ctx, "job");
    }


    public Job getJob()
    {
        return getJob(getSession().getSessionContext());
    }


    public void setJob(SessionContext ctx, Job value)
    {
        JOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setJob(Job value)
    {
        setJob(getSession().getSessionContext(), value);
    }


    Integer getJobPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "jobPOS");
    }


    Integer getJobPOS()
    {
        return getJobPOS(getSession().getSessionContext());
    }


    int getJobPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getJobPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getJobPOSAsPrimitive()
    {
        return getJobPOSAsPrimitive(getSession().getSessionContext());
    }


    void setJobPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "jobPOS", value);
    }


    void setJobPOS(Integer value)
    {
        setJobPOS(getSession().getSessionContext(), value);
    }


    void setJobPOS(SessionContext ctx, int value)
    {
        setJobPOS(ctx, Integer.valueOf(value));
    }


    void setJobPOS(int value)
    {
        setJobPOS(getSession().getSessionContext(), value);
    }


    public String getQuery(SessionContext ctx)
    {
        return (String)getProperty(ctx, "query");
    }


    public String getQuery()
    {
        return getQuery(getSession().getSessionContext());
    }


    public void setQuery(SessionContext ctx, String value)
    {
        setProperty(ctx, "query", value);
    }


    public void setQuery(String value)
    {
        setQuery(getSession().getSessionContext(), value);
    }


    public ComposedType getType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "type");
    }


    public ComposedType getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(ComposedType value)
    {
        setType(getSession().getSessionContext(), value);
    }
}
