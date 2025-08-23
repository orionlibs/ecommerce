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
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedJobLog extends GenericItem
{
    public static final String STEP = "step";
    public static final String MESSAGE = "message";
    public static final String SHORTMESSAGE = "shortMessage";
    public static final String LEVEL = "level";
    public static final String CRONJOB = "cronJob";
    protected static final BidirectionalOneToManyHandler<GeneratedJobLog> CRONJOBHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.JOBLOG, false, "cronJob", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("step", Item.AttributeMode.INITIAL);
        tmp.put("message", Item.AttributeMode.INITIAL);
        tmp.put("level", Item.AttributeMode.INITIAL);
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


    protected void setCronJob(SessionContext ctx, CronJob value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'cronJob' is not changeable", 0);
        }
        CRONJOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setCronJob(CronJob value)
    {
        setCronJob(getSession().getSessionContext(), value);
    }


    public EnumerationValue getLevel(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "level");
    }


    public EnumerationValue getLevel()
    {
        return getLevel(getSession().getSessionContext());
    }


    protected void setLevel(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'level' is not changeable", 0);
        }
        setProperty(ctx, "level", value);
    }


    protected void setLevel(EnumerationValue value)
    {
        setLevel(getSession().getSessionContext(), value);
    }


    public String getMessage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "message");
    }


    public String getMessage()
    {
        return getMessage(getSession().getSessionContext());
    }


    protected void setMessage(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'message' is not changeable", 0);
        }
        setProperty(ctx, "message", value);
    }


    protected void setMessage(String value)
    {
        setMessage(getSession().getSessionContext(), value);
    }


    public String getShortMessage()
    {
        return getShortMessage(getSession().getSessionContext());
    }


    public Step getStep(SessionContext ctx)
    {
        return (Step)getProperty(ctx, "step");
    }


    public Step getStep()
    {
        return getStep(getSession().getSessionContext());
    }


    protected void setStep(SessionContext ctx, Step value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'step' is not changeable", 0);
        }
        setProperty(ctx, "step", value);
    }


    protected void setStep(Step value)
    {
        setStep(getSession().getSessionContext(), value);
    }


    public abstract String getShortMessage(SessionContext paramSessionContext);
}
