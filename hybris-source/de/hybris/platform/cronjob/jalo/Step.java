package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public abstract class Step extends GeneratedStep
{
    private static final Logger logger = Logger.getLogger(Step.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("batchJob", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("code", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("sequenceNumber", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new Step", 0);
        }
        Step step = (Step)super.createItem(ctx, type, allAttributes);
        step.setBatchJob(ctx, (BatchJob)allAttributes.get("batchJob"));
        step.setSequenceNumber(ctx, (Integer)allAttributes.get("sequenceNumber"));
        step.setCode(ctx, (String)allAttributes.get("code"));
        return (Item)step;
    }


    @ForceJALO(reason = "something else")
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap map = super.getNonInitialAttributes(ctx, allAttributes);
        map.remove("code");
        map.remove("sequenceNumber");
        map.remove("batchJob");
        return map;
    }


    @ForceJALO(reason = "something else")
    public void setNonInitialAttributes(SessionContext ctx, Item item, Item.ItemAttributeMap nonInitialAttributes) throws JaloBusinessException
    {
        Item.ItemAttributeMap myattributes = new Item.ItemAttributeMap((Map)nonInitialAttributes);
        if(myattributes.get("synchronous") == null)
        {
            myattributes.put("synchronous", Boolean.TRUE);
        }
        if(myattributes.get("errorMode") == null)
        {
            myattributes.put("errorMode", item.getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.ERRORMODE, GeneratedCronJobConstants.Enumerations.ErrorMode.FAIL));
        }
        super.setNonInitialAttributes(ctx, item, myattributes);
    }


    public void log(String message, Level level)
    {
        getBatchJob().log(message, level);
    }


    public void debug(String message)
    {
        logger.debug(message);
    }


    public void info(String message)
    {
        logger.info(message);
    }


    public void warn(String message)
    {
        logger.warn(message);
    }


    public void error(String message)
    {
        logger.error(message);
    }


    public void fatal(String message)
    {
        logger.fatal(message);
    }


    public boolean isDebugEnabled()
    {
        return logger.isDebugEnabled();
    }


    public boolean isInfoEnabled()
    {
        return logger.isInfoEnabled();
    }


    public boolean isWarnEnabled()
    {
        return logger.isEnabledFor((Priority)Level.WARN);
    }


    public boolean isErrorEnabled()
    {
        return logger.isEnabledFor((Priority)Level.ERROR);
    }


    protected Collection<ChangeDescriptor> getChanges(CronJob cronJob)
    {
        return cronJob.getChanges(this);
    }


    protected Collection<ChangeDescriptor> getChanges(CronJob cronJob, String changeType)
    {
        return cronJob.getChanges(this, changeType);
    }


    protected Collection<ChangeDescriptor> getChanges(CronJob cronJob, int start, int count)
    {
        return cronJob.getChanges(this, start, count);
    }


    protected Collection<ChangeDescriptor> getChanges(CronJob cronJob, String changeType, int start, int count)
    {
        return cronJob.getChanges(this, changeType, start, count);
    }


    protected ChangeDescriptor addChange(CronJob cronJob, String changeType, Item changedItem, String description)
    {
        return cronJob.addChangeDescriptor(this, changeType, changedItem, description);
    }


    protected boolean hasChanges(CronJob cronJob, String changeType)
    {
        return (getMostRecentChange(cronJob, changeType) != null);
    }


    protected ChangeDescriptor getMostRecentChange(CronJob cronJob, String changeType)
    {
        return cronJob.getMostRecentChange(this, changeType);
    }


    protected boolean canUndo(CronJob cronJob)
    {
        return false;
    }


    protected abstract void undoStep(CronJob paramCronJob);


    protected abstract boolean canPerform(CronJob paramCronJob);


    protected abstract void performStep(CronJob paramCronJob) throws AbortCronJobException;


    public EnumerationValue getFailErrorMode()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.ERRORMODE, GeneratedCronJobConstants.Enumerations.ErrorMode.FAIL);
    }


    public EnumerationValue getPauseErrorMode()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.ERRORMODE, GeneratedCronJobConstants.Enumerations.ErrorMode.PAUSE);
    }


    public EnumerationValue getIgnoreErrorMode()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.ERRORMODE, GeneratedCronJobConstants.Enumerations.ErrorMode.IGNORE);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        EnumerationValue errorMode = getErrorMode();
        return getBatchJob().getCode() + "->" + getBatchJob().getCode() + "[" + getCode() + "," + (isSynchronousAsPrimitive() ? "sync" : "async") + "]";
    }


    public boolean isAbortable()
    {
        return false;
    }
}
