package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class BatchJob extends GeneratedBatchJob
{
    private static final ThreadLocal CURRENT_STEP = new ThreadLocal();
    private static final Logger LOG = Logger.getLogger(BatchJob.class.getName());


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        checkRemovable(ctx);
        for(Iterator<Step> it = getSteps().iterator(); it.hasNext(); )
        {
            Step step = it.next();
            step.remove();
        }
        super.remove(ctx);
    }


    protected static final void setCurrentlyExecutingStep(Step step)
    {
        if(step == null)
        {
            throw new NullPointerException();
        }
        if(CURRENT_STEP.get() != null)
        {
            throw new IllegalStateException("current step is already set to " + ((Step)CURRENT_STEP.get()).getPK() + " - cannot set to " + step
                            .getPK());
        }
        CURRENT_STEP.set(step);
    }


    protected static final void unsetCurrentlyExecutingStep()
    {
        CURRENT_STEP.set(null);
    }


    protected static final Step getCurrentlyExecutingStep()
    {
        Step step = CURRENT_STEP.get();
        if(step == null)
        {
            throw new IllegalStateException("current step is not set");
        }
        return step;
    }


    protected static final boolean hasCurrentlyExecutingStep()
    {
        return (CURRENT_STEP.get() != null);
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable(CronJob cronJob)
    {
        Step current = cronJob.getCurrentStep();
        return (current != null) ? current.isAbortable() : super.isAbortable(cronJob);
    }


    @ForceJALO(reason = "something else")
    public void setSteps(SessionContext ctx, List<Step> steps)
    {
        Collection<Step> toRemove = new ArrayList<>(getSteps(ctx));
        if(steps != null)
        {
            toRemove.removeAll(steps);
        }
        for(Iterator<Step> it = toRemove.iterator(); it.hasNext(); )
        {
            try
            {
                Step step = it.next();
                if(equals(step.getBatchJob()))
                {
                    step.remove(ctx);
                    continue;
                }
                LOG.error("Skipped deletion of step: " + step + "! Because its BatchJob was not: " + getCode());
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    @ForceJALO(reason = "something else")
    public List<Step> getSteps()
    {
        return getSteps(getSession().getSessionContext());
    }


    @ForceJALO(reason = "something else")
    public List<Step> getSteps(SessionContext ctx)
    {
        return getSteps(ctx, 0, -1);
    }


    public List<Step> getSteps(int start, int count)
    {
        return getSteps(getSession().getSessionContext(), start, count);
    }


    public List<Step> getSteps(SessionContext ctx, int start, int count)
    {
        return getSession().getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                                        getSession().getTypeManager()
                                                                        .getComposedType(Step.class)
                                                                        .getCode() + "} WHERE {batchJob} = ?me ORDER BY {sequenceNumber} ASC",
                                        Collections.singletonMap("me", this), Collections.singletonList(Step.class), true, true, start, count)
                        .getResult();
    }


    public Step getFirstStep()
    {
        return getFirstStep(getSession().getSessionContext());
    }


    public Step getFirstStep(SessionContext ctx)
    {
        Collection<Step> result = getSteps(getSession().getSessionContext(), 0, 1);
        return result.isEmpty() ? null : result.iterator().next();
    }


    public Step getLastStep()
    {
        return getLastStep(getSession().getSessionContext());
    }


    public Step getLastStep(SessionContext ctx)
    {
        List<Step> list = getSession().getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + getSession().getTypeManager().getComposedType(Step.class).getCode() + "} WHERE {batchJob} = ?me ORDER BY {sequenceNumber} DESC", Collections.singletonMap("me", this), Collections.singletonList(Step.class), true, true, 0, 1)
                        .getResult();
        return list.isEmpty() ? null : list.iterator().next();
    }


    public Collection<Step> getAllPreviousSteps(Step step)
    {
        return getAllPreviousSteps(getSession().getSessionContext(), step);
    }


    public Collection<Step> getAllPreviousSteps(SessionContext ctx, Step step)
    {
        if(step != null)
        {
            Map<Object, Object> values = new HashMap<>();
            values.put("me", this);
            values.put("sequencenumber", step.getSequenceNumber());
            return getSession().getFlexibleSearch().search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                                            getSession().getTypeManager().getComposedType(Step.class).getCode() + "} WHERE {batchJob} = ?me AND {sequenceNumber} <?sequencenumber ORDER BY {sequenceNumber} ASC", values,
                                            Collections.singletonList(Step.class), true, true, 0, -1)
                            .getResult();
        }
        return Collections.EMPTY_LIST;
    }


    protected Integer getNextSequenceNumber()
    {
        return getNextSequenceNumber(getSession().getSessionContext());
    }


    protected Integer getNextSequenceNumber(SessionContext ctx)
    {
        List<Integer> rows = getSession().getFlexibleSearch()
                        .search("SELECT MAX({sequenceNumber}) FROM {" + getSession().getTypeManager().getComposedType(Step.class).getCode() + "} WHERE {batchJob}=?me ", Collections.singletonMap("me", this), Collections.singletonList(Integer.class), true, true, 0, -1).getResult();
        return Integer.valueOf((!rows.isEmpty() && rows.get(0) != null) ? (((Integer)rows.get(0)).intValue() + 1) : 0);
    }


    protected Step addStep()
    {
        return addStep(getSession().getTypeManager().getComposedType(Step.class));
    }


    public Step addStep(ComposedType stepType)
    {
        return addStep(stepType, null);
    }


    public Step addStep(ComposedType stepType, Map additionalAttributes)
    {
        try
        {
            Item.ItemAttributeMap<String, BatchJob> itemAttributeMap = (additionalAttributes == null) ? new Item.ItemAttributeMap() : new Item.ItemAttributeMap(additionalAttributes);
            itemAttributeMap.put("batchJob", this);
            itemAttributeMap.put("sequenceNumber", getNextSequenceNumber());
            Step step = (Step)stepType.newInstance((Map)itemAttributeMap);
            return step;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable nested = e.getCause();
            if(nested != null)
            {
                if(nested instanceof RuntimeException)
                {
                    throw (RuntimeException)nested;
                }
                throw new JaloSystemException(nested);
            }
            throw new JaloSystemException(e);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private static final int getLastPosition(List orderdSteps, Collection previousSteps)
    {
        int pos = -1;
        for(Iterator<Step> it = previousSteps.iterator(); it.hasNext(); )
        {
            Step step = it.next();
            int position = orderdSteps.indexOf(step);
            if(position < 0)
            {
                return -1;
            }
            pos = Math.max(pos, position);
        }
        return pos;
    }


    private List<Step> prepareSteps(CronJob cronJob, boolean undo)
    {
        if(undo)
        {
            List<Step> sorted = new ArrayList<>();
            Step step = cronJob.getCurrentStep();
            if(step != null)
            {
                sorted.add(step);
            }
            List<Step> list = new ArrayList<>(cronJob.getProcessedSteps());
            Collections.reverse(list);
            sorted.addAll(list);
            return sorted;
        }
        Set<Step> processed = new HashSet<>(cronJob.getProcessedSteps());
        Set<Step> toOrder = new HashSet<>(cronJob.getPendingSteps());
        toOrder.addAll(processed);
        Step current = cronJob.getCurrentStep();
        if(current != null)
        {
            toOrder.add(current);
        }
        List<Step> ordered = new ArrayList();
        while(!toOrder.isEmpty())
        {
            Set<Step> newSet = new HashSet<>();
            boolean addedOne = false;
            for(Iterator<Step> it = toOrder.iterator(); it.hasNext(); )
            {
                Step step = it.next();
                Collection<Step> allPrevious = getAllPreviousSteps(step);
                if(allPrevious.isEmpty())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("found independent step " + step);
                    }
                    ordered.add(step);
                    addedOne = true;
                    continue;
                }
                int insertAfterPos = getLastPosition(ordered, allPrevious);
                if(insertAfterPos < 0)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("found dependent step " + step + " - waiting for next round");
                    }
                    newSet.add(step);
                    continue;
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("found dependent step " + step + " - inserting at " + insertAfterPos + 1);
                }
                ordered.add(insertAfterPos + 1, step);
                addedOne = true;
            }
            if(!addedOne && !newSet.isEmpty())
            {
                throw new JaloInvalidParameterException("cannot prepare steps due to cyclic dependencies ( pending steps = " + cronJob
                                .getPendingSteps() + ", current = " + cronJob.getCurrentStep() + ", processed = " + cronJob
                                .getProcessedSteps() + " )", 0);
            }
            toOrder = newSet;
            if(LOG.isDebugEnabled())
            {
                LOG.debug("ordered steps = " + ordered);
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("removing processed steps = " + processed);
        }
        ordered.removeAll(processed);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("ordered steps = " + ordered);
        }
        return ordered;
    }


    protected CronJob.CronJobResult undoCronJob(CronJob cronJob)
    {
        for(Iterator<Step> it = prepareSteps(cronJob, true).iterator(); it.hasNext() && !cronJob.isPaused(); )
        {
            Step step = it.next();
            if(!step.canUndo(cronJob))
            {
                return cronJob.getPausedResult();
            }
            setCurrentlyExecutingStep(step);
            cronJob.setCurrentStep(step, true);
            try
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Starting Step UNDO " + step.getCode());
                }
                long stepStartTime = System.currentTimeMillis();
                step.undoStep(cronJob);
                long stepEndTime = System.currentTimeMillis();
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Finished Step UNDO " + step
                                    .getCode() + " (Duration: " + stepEndTime - stepStartTime + " ms)");
                }
                cronJob.currentStepDone(true);
                unsetCurrentlyExecutingStep();
                if(cronJob.isPaused())
                {
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("Step " + step.getCode() + " paused this CronJob");
                    }
                    return cronJob.getPausedResult();
                }
            }
            catch(Exception e)
            {
                LOG.error("Error executing step " + step + " : " + e + "\n" + Utilities.getStackTraceAsString(e));
                return cronJob.getAbortResult();
            }
            finally
            {
                unsetCurrentlyExecutingStep();
            }
        }
        return cronJob.getUndoFinishedResult(true);
    }


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        for(Iterator<Step> it = prepareSteps(cronJob, false).iterator(); it.hasNext() && !cronJob.isPaused(); )
        {
            Step step = it.next();
            if(!step.canPerform(cronJob))
            {
                LOG.warn("Step " + step.getCode() + " can not be performed");
                return cronJob.getPausedResult();
            }
            if(cronJob.isRequestAbortAsPrimitive())
            {
                LOG.warn("CronJob was aborted by client before step " + step.getCode());
                cronJob.setRequestAbort(null);
                return cronJob.getAbortResult();
            }
            cronJob.setCurrentStep(step, false);
            try
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Starting Step " + step.getCode());
                }
                long stepStartTime = System.currentTimeMillis();
                step.performStep(cronJob);
                long stepEndTime = System.currentTimeMillis();
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Finished Step " + step.getCode() + " (Duration: " +
                                    Utilities.formatTime(stepEndTime - stepStartTime) + ")");
                }
                cronJob.currentStepDone(false);
                if(cronJob.isPaused())
                {
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("Step " + step.getCode() + " paused this CronJob");
                    }
                    return cronJob.getPausedResult();
                }
            }
            catch(AbortCronJobException e)
            {
                LOG.warn("CronJob was aborted by client during step " + step.getCode());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("aborted at " + Utilities.getStackTraceAsString((Throwable)e));
                }
                cronJob.setRequestAbort(null);
                return cronJob.getAbortResult();
            }
            catch(Exception e)
            {
                LOG.error("Error executing step " + step + " : " + e + "\n" + Utilities.getStackTraceAsString(e));
                return cronJob.getAbortResult();
            }
        }
        CronJob.CronJobResult finishedResult = cronJob.getFinishedResult(true);
        return finishedResult;
    }


    protected boolean canUndo(CronJob cronJob)
    {
        return true;
    }
}
