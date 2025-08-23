package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedRuleEngineCronJob extends CronJob
{
    public static final String SOURCERULES = "sourceRules";
    public static final String SRCMODULENAME = "srcModuleName";
    public static final String TARGETMODULENAME = "targetModuleName";
    public static final String ENABLEINCREMENTALUPDATE = "enableIncrementalUpdate";
    public static final String LOCKACQUIRED = "lockAcquired";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("sourceRules", Item.AttributeMode.INITIAL);
        tmp.put("srcModuleName", Item.AttributeMode.INITIAL);
        tmp.put("targetModuleName", Item.AttributeMode.INITIAL);
        tmp.put("enableIncrementalUpdate", Item.AttributeMode.INITIAL);
        tmp.put("lockAcquired", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isEnableIncrementalUpdate(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enableIncrementalUpdate");
    }


    public Boolean isEnableIncrementalUpdate()
    {
        return isEnableIncrementalUpdate(getSession().getSessionContext());
    }


    public boolean isEnableIncrementalUpdateAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnableIncrementalUpdate(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableIncrementalUpdateAsPrimitive()
    {
        return isEnableIncrementalUpdateAsPrimitive(getSession().getSessionContext());
    }


    public void setEnableIncrementalUpdate(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enableIncrementalUpdate", value);
    }


    public void setEnableIncrementalUpdate(Boolean value)
    {
        setEnableIncrementalUpdate(getSession().getSessionContext(), value);
    }


    public void setEnableIncrementalUpdate(SessionContext ctx, boolean value)
    {
        setEnableIncrementalUpdate(ctx, Boolean.valueOf(value));
    }


    public void setEnableIncrementalUpdate(boolean value)
    {
        setEnableIncrementalUpdate(getSession().getSessionContext(), value);
    }


    public Boolean isLockAcquired(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "lockAcquired");
    }


    public Boolean isLockAcquired()
    {
        return isLockAcquired(getSession().getSessionContext());
    }


    public boolean isLockAcquiredAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLockAcquired(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLockAcquiredAsPrimitive()
    {
        return isLockAcquiredAsPrimitive(getSession().getSessionContext());
    }


    public void setLockAcquired(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "lockAcquired", value);
    }


    public void setLockAcquired(Boolean value)
    {
        setLockAcquired(getSession().getSessionContext(), value);
    }


    public void setLockAcquired(SessionContext ctx, boolean value)
    {
        setLockAcquired(ctx, Boolean.valueOf(value));
    }


    public void setLockAcquired(boolean value)
    {
        setLockAcquired(getSession().getSessionContext(), value);
    }


    public List<SourceRule> getSourceRules(SessionContext ctx)
    {
        List<SourceRule> coll = (List<SourceRule>)getProperty(ctx, "sourceRules");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<SourceRule> getSourceRules()
    {
        return getSourceRules(getSession().getSessionContext());
    }


    public void setSourceRules(SessionContext ctx, List<SourceRule> value)
    {
        setProperty(ctx, "sourceRules", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setSourceRules(List<SourceRule> value)
    {
        setSourceRules(getSession().getSessionContext(), value);
    }


    public String getSrcModuleName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "srcModuleName");
    }


    public String getSrcModuleName()
    {
        return getSrcModuleName(getSession().getSessionContext());
    }


    public void setSrcModuleName(SessionContext ctx, String value)
    {
        setProperty(ctx, "srcModuleName", value);
    }


    public void setSrcModuleName(String value)
    {
        setSrcModuleName(getSession().getSessionContext(), value);
    }


    public String getTargetModuleName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "targetModuleName");
    }


    public String getTargetModuleName()
    {
        return getTargetModuleName(getSession().getSessionContext());
    }


    public void setTargetModuleName(SessionContext ctx, String value)
    {
        setProperty(ctx, "targetModuleName", value);
    }


    public void setTargetModuleName(String value)
    {
        setTargetModuleName(getSession().getSessionContext(), value);
    }
}
