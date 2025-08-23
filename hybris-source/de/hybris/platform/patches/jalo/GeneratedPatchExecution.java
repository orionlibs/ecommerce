package de.hybris.platform.patches.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.patches.constants.GeneratedPatchesConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPatchExecution extends GenericItem
{
    public static final String PATCHID = "patchId";
    public static final String PATCHNAME = "patchName";
    public static final String PATCHDESCRIPTION = "patchDescription";
    public static final String EXECUTIONTIME = "executionTime";
    public static final String PREVIOUSEXECUTION = "previousExecution";
    public static final String NEXTEXECUTION = "nextExecution";
    public static final String JENKINSBUILD = "jenkinsBuild";
    public static final String GITHASHCODE = "gitHashCode";
    public static final String EXECUTIONSTATUS = "executionStatus";
    public static final String ERRORLOG = "errorLog";
    public static final String RERUNNABLE = "rerunnable";
    public static final String PATCHUNITS = "patchUnits";
    protected static final OneToManyHandler<PatchExecutionUnit> PATCHUNITSHANDLER = new OneToManyHandler(GeneratedPatchesConstants.TC.PATCHEXECUTIONUNIT, true, "patch", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("patchId", Item.AttributeMode.INITIAL);
        tmp.put("patchName", Item.AttributeMode.INITIAL);
        tmp.put("patchDescription", Item.AttributeMode.INITIAL);
        tmp.put("executionTime", Item.AttributeMode.INITIAL);
        tmp.put("previousExecution", Item.AttributeMode.INITIAL);
        tmp.put("nextExecution", Item.AttributeMode.INITIAL);
        tmp.put("jenkinsBuild", Item.AttributeMode.INITIAL);
        tmp.put("gitHashCode", Item.AttributeMode.INITIAL);
        tmp.put("executionStatus", Item.AttributeMode.INITIAL);
        tmp.put("errorLog", Item.AttributeMode.INITIAL);
        tmp.put("rerunnable", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getErrorLog(SessionContext ctx)
    {
        return (String)getProperty(ctx, "errorLog");
    }


    public String getErrorLog()
    {
        return getErrorLog(getSession().getSessionContext());
    }


    public void setErrorLog(SessionContext ctx, String value)
    {
        setProperty(ctx, "errorLog", value);
    }


    public void setErrorLog(String value)
    {
        setErrorLog(getSession().getSessionContext(), value);
    }


    public EnumerationValue getExecutionStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "executionStatus");
    }


    public EnumerationValue getExecutionStatus()
    {
        return getExecutionStatus(getSession().getSessionContext());
    }


    public void setExecutionStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "executionStatus", value);
    }


    public void setExecutionStatus(EnumerationValue value)
    {
        setExecutionStatus(getSession().getSessionContext(), value);
    }


    public Date getExecutionTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "executionTime");
    }


    public Date getExecutionTime()
    {
        return getExecutionTime(getSession().getSessionContext());
    }


    public void setExecutionTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "executionTime", value);
    }


    public void setExecutionTime(Date value)
    {
        setExecutionTime(getSession().getSessionContext(), value);
    }


    public String getGitHashCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "gitHashCode");
    }


    public String getGitHashCode()
    {
        return getGitHashCode(getSession().getSessionContext());
    }


    public void setGitHashCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "gitHashCode", value);
    }


    public void setGitHashCode(String value)
    {
        setGitHashCode(getSession().getSessionContext(), value);
    }


    public String getJenkinsBuild(SessionContext ctx)
    {
        return (String)getProperty(ctx, "jenkinsBuild");
    }


    public String getJenkinsBuild()
    {
        return getJenkinsBuild(getSession().getSessionContext());
    }


    public void setJenkinsBuild(SessionContext ctx, String value)
    {
        setProperty(ctx, "jenkinsBuild", value);
    }


    public void setJenkinsBuild(String value)
    {
        setJenkinsBuild(getSession().getSessionContext(), value);
    }


    public PatchExecution getNextExecution(SessionContext ctx)
    {
        return (PatchExecution)getProperty(ctx, "nextExecution");
    }


    public PatchExecution getNextExecution()
    {
        return getNextExecution(getSession().getSessionContext());
    }


    public void setNextExecution(SessionContext ctx, PatchExecution value)
    {
        setProperty(ctx, "nextExecution", value);
    }


    public void setNextExecution(PatchExecution value)
    {
        setNextExecution(getSession().getSessionContext(), value);
    }


    public String getPatchDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "patchDescription");
    }


    public String getPatchDescription()
    {
        return getPatchDescription(getSession().getSessionContext());
    }


    public void setPatchDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "patchDescription", value);
    }


    public void setPatchDescription(String value)
    {
        setPatchDescription(getSession().getSessionContext(), value);
    }


    public String getPatchId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "patchId");
    }


    public String getPatchId()
    {
        return getPatchId(getSession().getSessionContext());
    }


    public void setPatchId(SessionContext ctx, String value)
    {
        setProperty(ctx, "patchId", value);
    }


    public void setPatchId(String value)
    {
        setPatchId(getSession().getSessionContext(), value);
    }


    public String getPatchName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "patchName");
    }


    public String getPatchName()
    {
        return getPatchName(getSession().getSessionContext());
    }


    public void setPatchName(SessionContext ctx, String value)
    {
        setProperty(ctx, "patchName", value);
    }


    public void setPatchName(String value)
    {
        setPatchName(getSession().getSessionContext(), value);
    }


    public Collection<PatchExecutionUnit> getPatchUnits(SessionContext ctx)
    {
        return PATCHUNITSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<PatchExecutionUnit> getPatchUnits()
    {
        return getPatchUnits(getSession().getSessionContext());
    }


    public void setPatchUnits(SessionContext ctx, Collection<PatchExecutionUnit> value)
    {
        PATCHUNITSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPatchUnits(Collection<PatchExecutionUnit> value)
    {
        setPatchUnits(getSession().getSessionContext(), value);
    }


    public void addToPatchUnits(SessionContext ctx, PatchExecutionUnit value)
    {
        PATCHUNITSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPatchUnits(PatchExecutionUnit value)
    {
        addToPatchUnits(getSession().getSessionContext(), value);
    }


    public void removeFromPatchUnits(SessionContext ctx, PatchExecutionUnit value)
    {
        PATCHUNITSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPatchUnits(PatchExecutionUnit value)
    {
        removeFromPatchUnits(getSession().getSessionContext(), value);
    }


    public PatchExecution getPreviousExecution(SessionContext ctx)
    {
        return (PatchExecution)getProperty(ctx, "previousExecution");
    }


    public PatchExecution getPreviousExecution()
    {
        return getPreviousExecution(getSession().getSessionContext());
    }


    public void setPreviousExecution(SessionContext ctx, PatchExecution value)
    {
        setProperty(ctx, "previousExecution", value);
    }


    public void setPreviousExecution(PatchExecution value)
    {
        setPreviousExecution(getSession().getSessionContext(), value);
    }


    public Boolean isRerunnable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "rerunnable");
    }


    public Boolean isRerunnable()
    {
        return isRerunnable(getSession().getSessionContext());
    }


    public boolean isRerunnableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRerunnable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRerunnableAsPrimitive()
    {
        return isRerunnableAsPrimitive(getSession().getSessionContext());
    }


    public void setRerunnable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "rerunnable", value);
    }


    public void setRerunnable(Boolean value)
    {
        setRerunnable(getSession().getSessionContext(), value);
    }


    public void setRerunnable(SessionContext ctx, boolean value)
    {
        setRerunnable(ctx, Boolean.valueOf(value));
    }


    public void setRerunnable(boolean value)
    {
        setRerunnable(getSession().getSessionContext(), value);
    }
}
