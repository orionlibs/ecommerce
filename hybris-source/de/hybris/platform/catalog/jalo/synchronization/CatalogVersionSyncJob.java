package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ThreadUtilities;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CatalogVersionSyncJob extends GeneratedCatalogVersionSyncJob
{
    private static final Logger LOG = Logger.getLogger(CatalogVersionSyncJob.class.getName());


    public SyncItemCronJob newExecution()
    {
        return (SyncItemCronJob)CatalogManager.getInstance().createCatalogVersionSyncCronJob(Collections.singletonMap("job", this));
    }


    protected boolean checkDependentJobsValidity()
    {
        try
        {
            loadDependentCatalogVersionMap(true);
            return true;
        }
        catch(IllegalStateException e)
        {
            LOG.error(e.getMessage());
            return false;
        }
    }


    protected boolean checkCatalogVersionValidity(CatalogVersionSyncCronJob cronJob)
    {
        CatalogVersion src = getSourceVersion();
        CatalogVersion tgt = getTargetVersion();
        SessionContext ctx = getSession().getSessionContext();
        boolean gotNoError = true;
        long time1 = System.currentTimeMillis();
        for(ComposedType root_ct : getRootTypes())
        {
            long duplicatedSrcCVItemCount = getDuplicatedCatalogItemsCount(ctx, src, (SyncItemCronJob)cronJob, root_ct);
            if(duplicatedSrcCVItemCount > 0L)
            {
                LOG.error("Cannot use source catalog version " + src + " for synchronization since it owns " + duplicatedSrcCVItemCount + " duplicate " + root_ct
                                .getCode() + " IDs");
                gotNoError = false;
            }
            long duplicatedTrgCVItemCount = getDuplicatedCatalogItemsCount(ctx, tgt, (SyncItemCronJob)cronJob, root_ct);
            if(duplicatedTrgCVItemCount > 0L)
            {
                LOG.error("Cannot use target catalog version " + tgt + " for synchronization since it owns " + duplicatedTrgCVItemCount + " duplicate " + root_ct
                                .getCode() + " IDs");
                gotNoError = false;
            }
        }
        long time2 = System.currentTimeMillis();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Checking version validity for " + src + " -> " + tgt + " took " + time2 - time1 + "ms");
        }
        return gotNoError;
    }


    protected SessionContext createSyncSessionContext(SyncItemCronJob cronJob)
    {
        return super.createSyncSessionContext(cronJob);
    }


    protected boolean canPerform(CronJob cronJob)
    {
        List<SyncSchedule> currentSyncSchedules = null;
        for(SyncItemCronJob otherCronJob : getExecutions())
        {
            if(cronJob.equals(otherCronJob))
            {
                continue;
            }
            if(otherCronJob.getStatus() == null || otherCronJob.getStatus()
                            .getCode()
                            .equals(CronJobStatus.UNKNOWN.getCode()))
            {
                int randomOffsetForRaceConditions = (new Random()).nextInt(1001) + 200;
                try
                {
                    LOG.warn("Other cron job '" + otherCronJob.getCode() + "' (pk:" + otherCronJob.getPK() + ") is still in UNKNOWN state. Waiting '" + randomOffsetForRaceConditions + "' milliseconds for start");
                    TimeUnit.MILLISECONDS.sleep(randomOffsetForRaceConditions);
                }
                catch(InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
            if(otherCronJob.isRunning() || otherCronJob.isRunningRestart())
            {
                CatalogVersionSyncCronJob canPerformCronJob = (CatalogVersionSyncCronJob)cronJob;
                CatalogVersionSyncCronJob runningCronJob = (CatalogVersionSyncCronJob)otherCronJob;
                if(canPerformCronJob.isFullSyncAsPrimitive())
                {
                    LOG.error("Cannot perform full sync cron job when others are running. Running cron job '" + otherCronJob
                                    .getCode() + "' (pk:" + otherCronJob
                                    .getPK() + ")");
                    abortWhenConfigured(canPerformCronJob);
                    return false;
                }
                if(runningCronJob.isFullSyncAsPrimitive())
                {
                    LOG.error("Cannot perform partial sync cron job when there is other full sync cron job running. Running cron job '" + otherCronJob
                                    .getCode() + "' (pk:" + otherCronJob.getPK() + ")");
                    abortWhenConfigured(canPerformCronJob);
                    return false;
                }
                if(currentSyncSchedules == null)
                {
                    currentSyncSchedules = canPerformCronJob.getOriginalSyncScheduleList();
                }
                if(numberOfItemsToSyncExceedLimit(currentSyncSchedules))
                {
                    LOG.error("Cannot perform partial sync when number of items to sync exceed the limit. Limit: " +
                                    maxItemsToSyncInPartialSyncJob());
                    abortWhenConfigured(canPerformCronJob);
                    return false;
                }
                if(schedulesOverlap(runningCronJob, currentSyncSchedules))
                {
                    LOG.error("Cannot perform partial sync when there are items overlapping from other running cron job '" + cronJob
                                    .getCode() + "' (pk:" + cronJob.getPK() + ")");
                    abortWhenConfigured(canPerformCronJob);
                    return false;
                }
            }
        }
        return true;
    }


    protected void abortWhenConfigured(CatalogVersionSyncCronJob cronJob)
    {
        if(cronJob.isAbortOnCollidingSyncAsPrimitive() || Config.getBoolean("synchronization.cronjob.abortOnCollidingSync.always", false))
        {
            LOG.info("CronJob " + cronJob.getCode() + " (pk: " + cronJob.getPK() + ") will be aborted.");
            cronJob.setStatus(cronJob.getAbortedStatus());
            cronJob.setResult(cronJob.getUnknownResult());
        }
    }


    private boolean numberOfItemsToSyncExceedLimit(List<SyncSchedule> syncSchedules)
    {
        int maxItemsToSyncPartially = maxItemsToSyncInPartialSyncJob();
        return (syncSchedules.size() > maxItemsToSyncPartially);
    }


    private int maxItemsToSyncInPartialSyncJob()
    {
        return Config.getInt("catalog.sync.partial.max.items", 500);
    }


    private boolean schedulesOverlap(CatalogVersionSyncCronJob runningCronJob, List<SyncSchedule> syncSchedules)
    {
        List<PK> currentSrcPKs = extractSrcPKs(syncSchedules);
        List<PK> runningJobSrcPKs = extractSrcPKs(runningCronJob.getOriginalSyncScheduleList());
        return !Collections.disjoint(currentSrcPKs, runningJobSrcPKs);
    }


    private List<PK> extractSrcPKs(List<SyncSchedule> syncSchedules)
    {
        return (List<PK>)syncSchedules.stream().filter(syncSchedule -> (syncSchedule.getSrcPK() != null)).map(SyncSchedule::getSrcPK)
                        .collect(Collectors.toList());
    }


    public void addCatalogItemsToSync(SyncItemCronJob _cj, Collection<? extends Item> items)
    {
        CatalogVersionSyncCronJob cronJob = (CatalogVersionSyncCronJob)_cj;
        CatalogVersion tgt = getTargetVersion();
        CatalogManager catalogManager = CatalogManager.getInstance();
        List<PK[]> itemPKs = (List)new ArrayList<>(items.size());
        for(Item i : items)
        {
            if(i instanceof Category)
            {
                throw new IllegalArgumentException("cannot add categories to sync via addCatalogItemsToSync() - use addCategoriesToSync() instead");
            }
            Item copy = catalogManager.getSynchronizedCopy(i, (SyncItemJob)this);
            if(copy == null)
            {
                copy = catalogManager.getCounterpartItem(i, tgt);
            }
            itemPKs.add(new PK[] {i
                            .getPK(), (copy != null) ? copy.getPK() : null});
        }
        cronJob.addPendingItems(itemPKs);
        if(LOG.isInfoEnabled())
        {
            LOG.info("Added pending sync actions for " + itemPKs.size() + " items");
        }
    }


    public void addCategoriesToSync(SyncItemCronJob _cj, Collection<? extends Category> categories, boolean includeSubcategories, boolean includeProducts)
    {
        CatalogVersionSyncCronJob cronJob = (CatalogVersionSyncCronJob)_cj;
        boolean createNew = isCreateNewItemsAsPrimitive();
        CatalogVersion src = getSourceVersion();
        CatalogVersion tgt = getTargetVersion();
        SessionContext ctx = createSyncSessionContext((SyncItemCronJob)cronJob);
        CatalogManager catalogManager = CatalogManager.getInstance();
        Set<Category> controlSet = new HashSet<>();
        List<Item> updates = new ArrayList<>();
        List<Item> removes = new ArrayList<>();
        for(Collection<? extends Category> current = categories; current != null && !current.isEmpty(); )
        {
            List<Category> newCurrents = new ArrayList<>();
            for(Category cat : current)
            {
                Category tgtCat = (Category)catalogManager.getCounterpartItem((Item)cat, tgt);
                if(tgtCat == null && !createNew)
                {
                    continue;
                }
                updates.add(cat);
                updates.add(tgtCat);
                controlSet.add(cat);
                if(includeSubcategories)
                {
                    Set<Category> subCats = new HashSet(cat.getSubcategories(ctx));
                    filterExternalCategories(subCats, src);
                    Set<Category> tgtSubCats = (tgtCat != null) ? new HashSet(tgtCat.getSubcategories(ctx)) : null;
                    if(CollectionUtils.isNotEmpty(tgtSubCats))
                    {
                        filterExternalCategories(tgtSubCats, tgt);
                        Set<Item> missingTargetItems = getMissingTargetItems(subCats, tgtSubCats, src);
                        for(Item missingOne : missingTargetItems)
                        {
                            removes.add(missingOne);
                        }
                    }
                    newCurrents.addAll(subCats);
                }
                if(includeProducts)
                {
                    Set<Product> products = new HashSet<>(cat.getProducts(ctx));
                    filterExternalProducts(products, src);
                    Set<Product> tgtProducts = (tgtCat != null) ? new HashSet<>(tgtCat.getProducts(ctx)) : null;
                    if(tgtProducts != null)
                    {
                        filterExternalProducts(tgtProducts, tgt);
                        for(Item missingOne : getMissingTargetItems(products, tgtProducts, src))
                        {
                            removes.add(missingOne);
                        }
                    }
                    for(Product p : products)
                    {
                        updates.add(p);
                        updates.add(catalogManager.getCounterpartItem((Item)p, tgt));
                    }
                }
            }
            newCurrents.removeAll(controlSet);
            current = newCurrents;
        }
        List<PK[]> itemPKs = (List)new ArrayList<>(removes.size());
        for(Item toRemove : removes)
        {
            itemPKs.add(new PK[] {null, toRemove
                            .getPK()});
        }
        cronJob.addPendingItems(itemPKs);
        if(LOG.isInfoEnabled())
        {
            LOG.info("Added pending remove actions for " + itemPKs.size() + " items");
        }
        itemPKs = (List)new ArrayList<>(updates.size() / 2);
        for(Iterator<Item> it = updates.iterator(); it.hasNext(); )
        {
            Item toUpdate = it.next();
            Item existingCopy = it.next();
            itemPKs.add(new PK[] {toUpdate
                            .getPK(), (existingCopy != null) ? existingCopy.getPK() : null});
        }
        cronJob.addPendingItems(itemPKs);
        if(LOG.isInfoEnabled())
        {
            LOG.info("Added pending update actions for " + itemPKs.size() + " items");
        }
    }


    private void filterExternalCategories(Set<Category> subCategories, CatalogVersion requiredVersion)
    {
        CatalogManager catalogManager = CatalogManager.getInstance();
        for(Iterator<Category> it = subCategories.iterator(); it.hasNext(); )
        {
            Category category = it.next();
            if(!requiredVersion.equals(catalogManager.getCatalogVersion(category)))
            {
                it.remove();
            }
        }
    }


    private void filterExternalProducts(Set<Product> products, CatalogVersion requiredVersion)
    {
        CatalogManager catalogManager = CatalogManager.getInstance();
        for(Iterator<Product> it = products.iterator(); it.hasNext(); )
        {
            Product product = it.next();
            if(!requiredVersion.equals(catalogManager.getCatalogVersion(product)))
            {
                it.remove();
            }
        }
    }


    protected void closeSyncWriter(SyncScheduleWriter syncScheduleWriter)
    {
        try
        {
            syncScheduleWriter.close();
        }
        catch(IOException e1)
        {
            throw new JaloSystemException(e1);
        }
    }


    public void configureFullVersionSync(SyncItemCronJob cronJob)
    {
        CatalogVersionSyncCronJob catalogVersionSyncCronJob = (CatalogVersionSyncCronJob)cronJob;
        SessionContext ctx = createSyncSessionContext(cronJob);
        List<ComposedType> rootTypes = getRootTypes(ctx);
        ExecutorService executorService = Executors.newFixedThreadPool(getMaxSchedulerThreads(ctx).intValue(), (ThreadFactory)new SyncSchedulerThreadFactory(
                        getTenant()));
        List<File> registeredTempFiles = new ArrayList<>();
        SyncScheduleWriter scheduleWriter = null;
        catalogVersionSyncCronJob.setStatusMessage("Scheduling composed types ...");
        int total = 0;
        try
        {
            Map<ComposedType, List<File>> rootTypeScheduleWriters = new HashMap<>();
            if(isRemoveMissingItemsAsPrimitive())
            {
                total += scheduleRemovalOfMissingItems(ctx, rootTypeScheduleWriters, rootTypes, registeredTempFiles, catalogVersionSyncCronJob, executorService);
            }
            if(isCreateNewItemsAsPrimitive())
            {
                total += scheduleCreationOfNewItems(ctx, rootTypeScheduleWriters, rootTypes, registeredTempFiles, catalogVersionSyncCronJob, executorService);
            }
            total += scheduleUpdatesAndConnectsOfModifiedItems(ctx, rootTypeScheduleWriters, rootTypes, registeredTempFiles, catalogVersionSyncCronJob, executorService);
            getSession().getSessionContext().setAttribute("full.sync.total." + cronJob.getPK(), Integer.valueOf(total));
            CatalogVersionSyncScheduleMedia scheduleMedia = catalogVersionSyncCronJob.getScheduleMedia(true);
            File scheduleMediaTempFile = catalogVersionSyncCronJob.getFile(scheduleMedia);
            registeredTempFiles.add(scheduleMediaTempFile);
            scheduleWriter = new SyncScheduleWriter(new FileWriter(scheduleMediaTempFile));
            int linesCount = mergeScheduleFiles(scheduleWriter, rootTypes, rootTypeScheduleWriters);
            scheduleMedia.setFile(scheduleMediaTempFile);
            LOG.info("Sync  '" +
                            getCode() + "' (pk:" + getPK() + ") configured " + total + " entries for job '" + cronJob.getCode() + "' (pk:" + cronJob
                            .getPK() + ") schedule medias: " + catalogVersionSyncCronJob.getScheduleMedias()
                            .size());
            if(linesCount != total)
            {
                LOG.error("mismatch scheduled total / merged lines : " + total + " / " + linesCount);
            }
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            List<Runnable> runningThreads = executorService.shutdownNow();
            if(runningThreads != null && !runningThreads.isEmpty())
            {
                LOG.error("Synchronization schedule task could not be shutdown for " + runningThreads.size() + " types");
            }
            if(scheduleWriter != null)
            {
                try
                {
                    scheduleWriter.flush();
                }
                catch(IOException e)
                {
                    LOG.error(e);
                }
                scheduleWriter.closeQuietly();
            }
            deleteRegisteredTempFiles(registeredTempFiles);
        }
    }


    private int mergeScheduleFiles(SyncScheduleWriter scheduleWriter, List<ComposedType> rootTypes, Map<ComposedType, List<File>> rootTypeScheduleWriters) throws IOException
    {
        int linesCount = 0;
        for(ComposedType rootType : rootTypes)
        {
            List<File> rootTypeFiles = rootTypeScheduleWriters.get(rootType);
            if(rootTypeFiles != null)
            {
                scheduleWriter.writeComment("------------------------------------------------");
                scheduleWriter.writeComment("Schedules for root type '" + rootType.getCode() + "'");
                scheduleWriter.writeComment("------------------------------------------------");
                for(File singleFile : rootTypeFiles)
                {
                    scheduleWriter.writeComment("file: '" + singleFile.getName() + "'");
                    SyncScheduleReader reader = null;
                    try
                    {
                        reader = new SyncScheduleReader(new FileReader(singleFile), -1);
                        while(reader.readNextLine())
                        {
                            SyncSchedule syncSchedule = reader.getScheduleFromLine();
                            scheduleWriter.write(syncSchedule);
                            linesCount++;
                        }
                        scheduleWriter.flush();
                    }
                    finally
                    {
                        if(reader != null)
                        {
                            reader.closeQuietly();
                        }
                    }
                }
                for(File f : rootTypeFiles)
                {
                    FileUtils.deleteQuietly(f);
                }
            }
        }
        return linesCount;
    }


    private int scheduleUpdatesAndConnectsOfModifiedItems(SessionContext ctx, Map<ComposedType, List<File>> rootTypeScheduleWriters, List<ComposedType> rootTypes, List<File> registeredTempFiles, CatalogVersionSyncCronJob cronJob, ExecutorService executorService)
                    throws IOException, InterruptedException, ExecutionException
    {
        int total = 0;
        boolean force = cronJob.isForceUpdateAsPrimitive();
        List<Callable<Integer>> schedulerCallables = new ArrayList<>();
        Collection<SyncScheduleWriter> syncScheduleWriters = new ArrayList<>(rootTypes.size());
        for(ComposedType rootType : rootTypes)
        {
            List<File> files = rootTypeScheduleWriters.get(rootType);
            if(files == null || files.isEmpty())
            {
                files = new ArrayList<>(4);
                rootTypeScheduleWriters.put(rootType, files);
            }
            File updateFile = createTempFile(registeredTempFiles, (CronJob)cronJob, rootType, "update");
            SyncScheduleWriter syncScheduleWriter = new SyncScheduleWriter(new FileWriter(updateFile));
            syncScheduleWriters.add(syncScheduleWriter);
            files.add(updateFile);
            schedulerCallables.add(new SyncSchedulerUpdateCallable(this, ctx, rootType, syncScheduleWriter, force));
            File connectFile = createTempFile(registeredTempFiles, (CronJob)cronJob, rootType, "connect");
            syncScheduleWriter = new SyncScheduleWriter(new FileWriter(connectFile));
            syncScheduleWriters.add(syncScheduleWriter);
            files.add(connectFile);
            schedulerCallables.add(new SyncSchedulerConnectCallable(this, ctx, rootType, syncScheduleWriter,
                            getAdditionalQueryRestrictions(ctx, rootType, (SyncItemCronJob)cronJob)));
        }
        if(!schedulerCallables.isEmpty())
        {
            for(Future<Integer> future : executorService.<Integer>invokeAll(schedulerCallables))
            {
                total += ((Integer)future.get()).intValue();
            }
        }
        closeScheduleWriters(syncScheduleWriters);
        return total;
    }


    private int scheduleCreationOfNewItems(SessionContext ctx, Map<ComposedType, List<File>> rootTypeScheduleWriters, List<ComposedType> rootTypes, List<File> registeredTempFiles, CatalogVersionSyncCronJob cronJob, ExecutorService executorService)
                    throws IOException, InterruptedException, ExecutionException
    {
        int total = 0;
        List<Callable<Integer>> schedulerCallables = new ArrayList<>();
        Collection<SyncScheduleWriter> syncScheduleWriters = new ArrayList<>(rootTypes.size());
        for(ComposedType rootType : rootTypes)
        {
            List<File> files = rootTypeScheduleWriters.get(rootType);
            if(files == null || files.isEmpty())
            {
                files = new ArrayList<>(4);
                rootTypeScheduleWriters.put(rootType, files);
            }
            File file = createTempFile(registeredTempFiles, (CronJob)cronJob, rootType, "create");
            SyncScheduleWriter syncScheduleWriter = new SyncScheduleWriter(new FileWriter(file));
            syncScheduleWriters.add(syncScheduleWriter);
            files.add(file);
            schedulerCallables.add(new SyncSchedulerAddCallable(this, ctx, rootType, syncScheduleWriter,
                            getAdditionalQueryRestrictions(ctx, rootType, (SyncItemCronJob)cronJob)));
        }
        if(!schedulerCallables.isEmpty())
        {
            for(Future<Integer> future : executorService.<Integer>invokeAll(schedulerCallables))
            {
                total += ((Integer)future.get()).intValue();
            }
        }
        closeScheduleWriters(syncScheduleWriters);
        return total;
    }


    private int scheduleRemovalOfMissingItems(SessionContext ctx, Map<ComposedType, List<File>> rootTypeScheduleWriters, List<ComposedType> rootTypes, List<File> registeredTempFiles, CatalogVersionSyncCronJob cronJob, ExecutorService executorService)
                    throws IOException, InterruptedException, ExecutionException
    {
        int total = 0;
        List<Callable<Integer>> schedulerCallables = new ArrayList<>();
        Collection<SyncScheduleWriter> syncScheduleWriters = new ArrayList<>(rootTypes.size());
        for(ComposedType rootType : rootTypes)
        {
            List<File> files = rootTypeScheduleWriters.get(rootType);
            if(files == null || files.isEmpty())
            {
                files = new ArrayList<>(4);
                rootTypeScheduleWriters.put(rootType, files);
            }
            File file = createTempFile(registeredTempFiles, (CronJob)cronJob, rootType, "remove");
            SyncScheduleWriter syncScheduleWriter = new SyncScheduleWriter(new FileWriter(file));
            syncScheduleWriters.add(syncScheduleWriter);
            files.add(file);
            schedulerCallables.add(new SyncSchedulerRemoveCallable(this, ctx, rootType, syncScheduleWriter,
                            getAdditionalQueryRestrictions(ctx, rootType, (SyncItemCronJob)cronJob)));
        }
        if(!schedulerCallables.isEmpty())
        {
            for(Future<Integer> future : executorService.<Integer>invokeAll(schedulerCallables))
            {
                total += ((Integer)future.get()).intValue();
            }
        }
        closeScheduleWriters(syncScheduleWriters);
        return total;
    }


    private void closeScheduleWriters(Collection<SyncScheduleWriter> registeredSyncScheduleWriter)
    {
        for(SyncScheduleWriter w : registeredSyncScheduleWriter)
        {
            w.closeQuietly();
        }
    }


    private File createTempFile(List<File> registeredTempFiles, CronJob cronJob, ComposedType rootType, String action) throws IOException
    {
        File file = File.createTempFile(cronJob.getPK().toString() + "_" + cronJob.getPK().toString() + "_" + rootType.getCode() + "_", ".csv");
        file.deleteOnExit();
        registeredTempFiles.add(file);
        return file;
    }


    private void deleteRegisteredTempFiles(List<File> registeredTempFiles)
    {
        for(File f : registeredTempFiles)
        {
            if(f.exists())
            {
                FileUtils.deleteQuietly(f);
            }
        }
    }


    @ForceJALO(reason = "something else")
    public SyncItemJob.CompletionInfo getCompletionInfo(SyncItemCronJob cronJob)
    {
        if(cronJob.getConfigurator() != null)
        {
            SyncItemJob.CompletionInfo configInfo = cronJob.getConfigurator().getCompletionInfo();
            return (configInfo != null) ? new SyncItemJob.CompletionInfo(configInfo.getCurrentTask(), configInfo.getPercentage(), configInfo
                            .getTotalCount(), configInfo.getCompletedCount(), 5 * configInfo
                            .getPercentage() / 100) :
                            new SyncItemJob.CompletionInfo("configuring", 0, 0, 0, 0);
        }
        return new SyncItemJob.CompletionInfo("<todo>", 33, 33, 11, 33);
    }


    @ForceJALO(reason = "something else")
    public boolean isPerformable(CronJob cronJob)
    {
        if(!super.isPerformable(cronJob))
        {
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("runningState", cronJob.getRunningStatus());
        List resultList = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATALOGVERSIONSYNCCRONJOB + "} WHERE {job} = ?me AND {status}=?runningState", params, Collections.singletonList(CatalogVersionSyncCronJob.class), true, true, 0, -1)
                        .getResult();
        return resultList.isEmpty();
    }


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        boolean doInfo = LOG.isInfoEnabled();
        CatalogVersionSyncCronJob catalogVersionSyncCronJob = (CatalogVersionSyncCronJob)cronJob;
        try
        {
            if(!getSession().getUser().equals(UserManager.getInstance().getAdminEmployee()))
            {
                LOG.info("Session user is not admin but " + getSession().getUser() + " - due to possible restrictions synchronization may not cover all items");
            }
            if(!checkDependentJobsValidity())
            {
                LOG.error("Aborted due to dependent jobs validation error");
                return catalogVersionSyncCronJob.getAbortResult();
            }
            if(!checkCatalogVersionValidity(catalogVersionSyncCronJob))
            {
                LOG.error("Aborted due to version validation error");
                return catalogVersionSyncCronJob.getAbortResult();
            }
            initializeTimeCounter((SyncItemCronJob)catalogVersionSyncCronJob);
            if(catalogVersionSyncCronJob.getConfigurator() != null)
            {
                long l = System.currentTimeMillis();
                if(doInfo)
                {
                    LOG.info("Starting configuration ...");
                }
                catalogVersionSyncCronJob.getConfigurator().configureCronjob((SyncItemCronJob)catalogVersionSyncCronJob, null);
                if(doInfo)
                {
                    LOG.info("Finished configuration in " + Utilities.formatTime(System.currentTimeMillis() - l) + ".");
                }
                catalogVersionSyncCronJob.setConfigurator(null);
            }
            else if(!catalogVersionSyncCronJob.hasSchedules())
            {
                long l = System.currentTimeMillis();
                if(doInfo)
                {
                    LOG.info("Starting configureFullVersionSync ...");
                }
                configureFullVersionSync((SyncItemCronJob)catalogVersionSyncCronJob);
                if(doInfo)
                {
                    LOG.info("Finished configureFullVersionSync in " + System.currentTimeMillis() - l + "ms");
                }
            }
            long time1 = System.currentTimeMillis();
            if(doInfo)
            {
                LOG.info("Starting synchronization ...");
            }
            SessionContext ctx = getSession().getSessionContext();
            String key = "full.sync.total." + catalogVersionSyncCronJob.getPK();
            Integer total = (Integer)ctx.getAttribute(key);
            ctx.removeAttribute(key);
            CatalogVersionSyncMaster master = new CatalogVersionSyncMaster(this, catalogVersionSyncCronJob);
            master.setFirstTurnTotal((total != null) ? total.intValue() : -1);
            master.doSynchronization();
            if(master.hasErrors())
            {
                LOG.error("Finished synchronization in " + Utilities.formatTime(System.currentTimeMillis() - time1) + ". There were errors during the synchronization!");
            }
            else if(doInfo)
            {
                LOG.info("Finished synchronization in " + Utilities.formatTime(
                                System.currentTimeMillis() - time1) + ". No errors.");
            }
            return cronJob.getFinishedResult(!master.hasErrors());
        }
        catch(AbortCronJobException e)
        {
            return doAbort((SyncItemCronJob)catalogVersionSyncCronJob);
        }
        finally
        {
            catalogVersionSyncCronJob.setConfigurator(null);
        }
    }


    Map<CatalogVersion, CatalogVersion> loadDependentCatalogVersionMap(boolean failOnError)
    {
        CatalogVersion mySourceV = getSourceVersion();
        Map<CatalogVersion, CatalogVersion> returnMap = null;
        for(CatalogVersionSyncJob depJob : getDependentSyncJobs())
        {
            CatalogVersion dependentSourceV = depJob.getSourceVersion();
            if(mySourceV.equals(dependentSourceV) || (returnMap != null && returnMap.containsKey(dependentSourceV)))
            {
                String msg = "Cannot add dependent SyncJob " + syncJobInfo(depJob) + "to the CatalogVersionSyncCopyContext of the job " + syncJobInfo(this) + " because a mapping for the dependent source catalogversion already exists!";
                if(failOnError)
                {
                    throw new IllegalStateException(msg);
                }
                LOG.error(msg);
                continue;
            }
            if(returnMap == null)
            {
                returnMap = new HashMap<>();
            }
            returnMap.put(dependentSourceV, depJob.getTargetVersion());
        }
        for(CatalogVersionSyncJob depOnJob : getDependsOnSyncJobs())
        {
            CatalogVersion dependsOnSourceV = depOnJob.getSourceVersion();
            if(mySourceV.equals(dependsOnSourceV) || (returnMap != null && returnMap.containsKey(dependsOnSourceV)))
            {
                String msg = "Cannot add dependsOn SyncJob " + syncJobInfo(depOnJob) + "to the CatalogVersionSyncCopyContext of the job " + syncJobInfo(this) + " because a mapping for the dependsOn source catalogversion already exists!";
                if(failOnError)
                {
                    throw new IllegalStateException(msg);
                }
                LOG.error(msg);
                continue;
            }
            if(returnMap == null)
            {
                returnMap = new HashMap<>();
            }
            returnMap.put(dependsOnSourceV, depOnJob.getTargetVersion());
        }
        return MapUtils.isNotEmpty(returnMap) ? returnMap : Collections.EMPTY_MAP;
    }


    private String syncJobInfo(CatalogVersionSyncJob job)
    {
        return "[code:" + job.getCode() + " srcCV:" + job.getSourceVersion() + " trgCV:" + job.getTargetVersion() + "]";
    }


    @ForceJALO(reason = "something else")
    public void setEnableTransactions(SessionContext ctx, Boolean enableTransaction)
    {
        super.setEnableTransactions(ctx,
                        (enableTransaction != null) ? enableTransaction : Boolean.valueOf(Config.getBoolean("catalog.sync.enable.transactions", false)));
    }


    @ForceJALO(reason = "something else")
    public Boolean isEnableTransactions(SessionContext ctx)
    {
        Boolean enableTransaction = super.isEnableTransactions(ctx);
        return (enableTransaction != null) ? enableTransaction : Boolean.valueOf(Config.getBoolean("catalog.sync.enable.transactions", false));
    }


    @ForceJALO(reason = "something else")
    public void setCopyCacheSize(SessionContext ctx, Integer size)
    {
        Integer toSet = (size != null) ? ((size.intValue() >= 0) ? size : Integer.valueOf(0)) : Integer.valueOf(Config.getInt("catalog.sync.workers.cache", 1000));
        super.setCopyCacheSize(ctx, toSet);
    }


    @ForceJALO(reason = "something else")
    public Integer getCopyCacheSize(SessionContext ctx)
    {
        Integer size = super.getCopyCacheSize(ctx);
        return (size != null) ? ((size.intValue() >= 0) ? size : Integer.valueOf(0)) : Integer.valueOf(Config.getInt("catalog.sync.workers.cache", 1000));
    }


    protected boolean doSynchronization(SyncItemJob.SyncItemCopyContext syncItemCopyContext, SyncItemCronJob cronJob) throws AbortCronJobException
    {
        throw new UnsupportedOperationException();
    }


    @ForceJALO(reason = "something else")
    public void setMaxThreads(SessionContext ctx, Integer maxThreads)
    {
        Integer toSet = (maxThreads != null) ? ((maxThreads.intValue() > 0) ? maxThreads : Integer.valueOf(1)) : Integer.valueOf(getDefaultMaxThreads(getTenant()));
        super.setMaxThreads(ctx, toSet);
    }


    @ForceJALO(reason = "something else")
    public void setMaxSchedulerThreads(SessionContext ctx, Integer maxSchedulerThreads)
    {
        Integer toSet = (maxSchedulerThreads != null) ? ((maxSchedulerThreads.intValue() > 0) ? maxSchedulerThreads : Integer.valueOf(1)) : Integer.valueOf(getDefaultMaxThreads(getTenant()));
        super.setMaxSchedulerThreads(ctx, toSet);
    }


    @ForceJALO(reason = "something else")
    public Integer getMaxThreads(SessionContext ctx)
    {
        Integer maxThreads = super.getMaxThreads(ctx);
        return (maxThreads != null) ? ((maxThreads.intValue() > 0) ? maxThreads : Integer.valueOf(1)) :
                        Integer.valueOf(getDefaultMaxThreads(getTenant()));
    }


    @ForceJALO(reason = "something else")
    public Integer getMaxSchedulerThreads(SessionContext ctx)
    {
        Integer maxSchedulerThreads = super.getMaxSchedulerThreads(ctx);
        return (maxSchedulerThreads != null) ? ((maxSchedulerThreads.intValue() > 0) ? maxSchedulerThreads : Integer.valueOf(1)) :
                        Integer.valueOf(getDefaultMaxThreads(getTenant()));
    }


    public static int getDefaultMaxThreads(Tenant tenant)
    {
        String dbName = tenant.getDataSource().getDatabaseName().toLowerCase();
        String expression = Config.getParameter("catalog.sync.workers." + dbName);
        if(StringUtils.isBlank(expression))
        {
            expression = Config.getParameter("catalog.sync.workers");
        }
        return ThreadUtilities.getNumberOfThreadsFromExpression(expression, 2);
    }


    @ForceJALO(reason = "something else")
    protected List<List> getAttributeAndConfigPairs()
    {
        CatalogManager catalogManager = CatalogManager.getInstance();
        Set<ComposedType> _includedTypes = new HashSet<>();
        for(ComposedType catType : catalogManager.getAllCatalogItemTypes())
        {
            _includedTypes.add(catType);
            _includedTypes.addAll(catType.getAllSuperTypes());
        }
        if(!_includedTypes.isEmpty())
        {
            CatalogVersionSyncCopyContext ctx = createCopyContext(null, null);
            for(ComposedType dependent : ctx.getDependentItemTypes())
            {
                _includedTypes.add(dependent);
                _includedTypes.addAll(dependent.getAllSuperTypes());
            }
        }
        List<List> ret = new ArrayList<>();
        String query = "SELECT {ad:" + Item.PK + "},{cfg:" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(AttributeDescriptor.class).getCode() + " AS ad LEFT JOIN " + GeneratedCatalogConstants.TC.SYNCATTRIBUTEDESCRIPTORCONFIG + " AS cfg ON {ad:" + Item.PK
                        + "}={cfg:attributeDescriptor} AND {cfg:syncJob}=?me } ";
        query = query + " WHERE {ad." + query + "} IN (?includedTypes)";
        Set<PK> includedTypesPks = (Set<PK>)_includedTypes.stream().map(Item::getPK).collect(Collectors.toSet());
        SearchResult<List<?>> search = FlexibleSearch.getInstance().search(query,
                        Map.of("me", this, "includedTypes", includedTypesPks),
                        List.of(AttributeDescriptor.class, SyncAttributeDescriptorConfig.class), true, true, 0, -1);
        List<List<?>> rows = search.getResult();
        for(List<?> row : rows)
        {
            AttributeDescriptor attributeDescriptor = (AttributeDescriptor)row.get(0);
            SyncAttributeDescriptorConfig cfg = (SyncAttributeDescriptorConfig)row.get(1);
            int modifiers = attributeDescriptor.getModifiers();
            if((modifiers & 0x400) == 1024 || (modifiers & 0x802) == 0)
            {
                continue;
            }
            ret.add(Arrays.asList(new LocalizableItem[] {(LocalizableItem)attributeDescriptor, (LocalizableItem)cfg}));
        }
        return ret;
    }


    protected CatalogVersionSyncCopyContext createCopyContext(CatalogVersionSyncCronJob catalogVersionSyncCronJob, CatalogVersionSyncWorker worker)
    {
        return new CatalogVersionSyncCopyContext(this, catalogVersionSyncCronJob, worker);
    }
}
