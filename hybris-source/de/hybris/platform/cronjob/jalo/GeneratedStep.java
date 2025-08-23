package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedStep extends GenericItem
{
    public static final String BATCHJOB = "batchJob";
    public static final String CODE = "code";
    public static final String SEQUENCENUMBER = "sequenceNumber";
    public static final String SYNCHRONOUS = "synchronous";
    public static final String ERRORMODE = "errorMode";
    public static final String PROCESSEDCRONJOBS = "processedCronJobs";
    protected static String CRONJOBPROCESSEDSTEPSRELATION_SRC_ORDERED = "relation.CronJobProcessedStepsRelation.source.ordered";
    protected static String CRONJOBPROCESSEDSTEPSRELATION_TGT_ORDERED = "relation.CronJobProcessedStepsRelation.target.ordered";
    protected static String CRONJOBPROCESSEDSTEPSRELATION_MARKMODIFIED = "relation.CronJobProcessedStepsRelation.markmodified";
    public static final String PENDINGCRONJOBS = "pendingCronJobs";
    protected static String CRONJOBPENDINGSTEPSRELATION_SRC_ORDERED = "relation.CronJobPendingStepsRelation.source.ordered";
    protected static String CRONJOBPENDINGSTEPSRELATION_TGT_ORDERED = "relation.CronJobPendingStepsRelation.target.ordered";
    protected static String CRONJOBPENDINGSTEPSRELATION_MARKMODIFIED = "relation.CronJobPendingStepsRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("batchJob", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("sequenceNumber", Item.AttributeMode.INITIAL);
        tmp.put("synchronous", Item.AttributeMode.INITIAL);
        tmp.put("errorMode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BatchJob getBatchJob(SessionContext ctx)
    {
        return (BatchJob)getProperty(ctx, "batchJob");
    }


    public BatchJob getBatchJob()
    {
        return getBatchJob(getSession().getSessionContext());
    }


    protected void setBatchJob(SessionContext ctx, BatchJob value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'batchJob' is not changeable", 0);
        }
        setProperty(ctx, "batchJob", value);
    }


    protected void setBatchJob(BatchJob value)
    {
        setBatchJob(getSession().getSessionContext(), value);
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


    public EnumerationValue getErrorMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "errorMode");
    }


    public EnumerationValue getErrorMode()
    {
        return getErrorMode(getSession().getSessionContext());
    }


    public void setErrorMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "errorMode", value);
    }


    public void setErrorMode(EnumerationValue value)
    {
        setErrorMode(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CronJob");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CRONJOBPROCESSEDSTEPSRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("CronJob");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CRONJOBPENDINGSTEPSRELATION_MARKMODIFIED);
        }
        return true;
    }


    Collection<CronJob> getPendingCronJobs(SessionContext ctx)
    {
        List<CronJob> items = getLinkedItems(ctx, false, GeneratedProcessingConstants.Relations.CRONJOBPENDINGSTEPSRELATION, "CronJob", null,
                        Utilities.getRelationOrderingOverride(CRONJOBPENDINGSTEPSRELATION_SRC_ORDERED, true), false);
        return items;
    }


    Collection<CronJob> getPendingCronJobs()
    {
        return getPendingCronJobs(getSession().getSessionContext());
    }


    long getPendingCronJobsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedProcessingConstants.Relations.CRONJOBPENDINGSTEPSRELATION, "CronJob", null);
    }


    long getPendingCronJobsCount()
    {
        return getPendingCronJobsCount(getSession().getSessionContext());
    }


    Collection<CronJob> getProcessedCronJobs(SessionContext ctx)
    {
        List<CronJob> items = getLinkedItems(ctx, false, GeneratedProcessingConstants.Relations.CRONJOBPROCESSEDSTEPSRELATION, "CronJob", null,
                        Utilities.getRelationOrderingOverride(CRONJOBPROCESSEDSTEPSRELATION_SRC_ORDERED, true), false);
        return items;
    }


    Collection<CronJob> getProcessedCronJobs()
    {
        return getProcessedCronJobs(getSession().getSessionContext());
    }


    long getProcessedCronJobsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedProcessingConstants.Relations.CRONJOBPROCESSEDSTEPSRELATION, "CronJob", null);
    }


    long getProcessedCronJobsCount()
    {
        return getProcessedCronJobsCount(getSession().getSessionContext());
    }


    public Integer getSequenceNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "sequenceNumber");
    }


    public Integer getSequenceNumber()
    {
        return getSequenceNumber(getSession().getSessionContext());
    }


    public int getSequenceNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getSequenceNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getSequenceNumberAsPrimitive()
    {
        return getSequenceNumberAsPrimitive(getSession().getSessionContext());
    }


    public void setSequenceNumber(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "sequenceNumber", value);
    }


    public void setSequenceNumber(Integer value)
    {
        setSequenceNumber(getSession().getSessionContext(), value);
    }


    public void setSequenceNumber(SessionContext ctx, int value)
    {
        setSequenceNumber(ctx, Integer.valueOf(value));
    }


    public void setSequenceNumber(int value)
    {
        setSequenceNumber(getSession().getSessionContext(), value);
    }


    public Boolean isSynchronous(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "synchronous");
    }


    public Boolean isSynchronous()
    {
        return isSynchronous(getSession().getSessionContext());
    }


    public boolean isSynchronousAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSynchronous(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSynchronousAsPrimitive()
    {
        return isSynchronousAsPrimitive(getSession().getSessionContext());
    }


    public void setSynchronous(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "synchronous", value);
    }


    public void setSynchronous(Boolean value)
    {
        setSynchronous(getSession().getSessionContext(), value);
    }


    public void setSynchronous(SessionContext ctx, boolean value)
    {
        setSynchronous(ctx, Boolean.valueOf(value));
    }


    public void setSynchronous(boolean value)
    {
        setSynchronous(getSession().getSessionContext(), value);
    }
}
