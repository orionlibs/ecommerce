package de.hybris.y2ysync.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processing.jalo.DistributedProcess;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedY2YDistributedProcess extends DistributedProcess
{
    public static final String BATCHSIZE = "batchSize";
    public static final String Y2YSYNCCRONJOB = "y2ySyncCronJob";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(DistributedProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("batchSize", Item.AttributeMode.INITIAL);
        tmp.put("y2ySyncCronJob", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getBatchSize(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "batchSize");
    }


    public Integer getBatchSize()
    {
        return getBatchSize(getSession().getSessionContext());
    }


    public int getBatchSizeAsPrimitive(SessionContext ctx)
    {
        Integer value = getBatchSize(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getBatchSizeAsPrimitive()
    {
        return getBatchSizeAsPrimitive(getSession().getSessionContext());
    }


    protected void setBatchSize(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'batchSize' is not changeable", 0);
        }
        setProperty(ctx, "batchSize", value);
    }


    protected void setBatchSize(Integer value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    protected void setBatchSize(SessionContext ctx, int value)
    {
        setBatchSize(ctx, Integer.valueOf(value));
    }


    protected void setBatchSize(int value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    public Y2YSyncCronJob getY2ySyncCronJob(SessionContext ctx)
    {
        return (Y2YSyncCronJob)getProperty(ctx, "y2ySyncCronJob");
    }


    public Y2YSyncCronJob getY2ySyncCronJob()
    {
        return getY2ySyncCronJob(getSession().getSessionContext());
    }


    public void setY2ySyncCronJob(SessionContext ctx, Y2YSyncCronJob value)
    {
        setProperty(ctx, "y2ySyncCronJob", value);
    }


    public void setY2ySyncCronJob(Y2YSyncCronJob value)
    {
        setY2ySyncCronJob(getSession().getSessionContext(), value);
    }
}
