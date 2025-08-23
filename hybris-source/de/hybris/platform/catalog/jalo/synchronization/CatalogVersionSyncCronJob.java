package de.hybris.platform.catalog.jalo.synchronization;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.ItemSyncDescriptor;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.collections.fast.YLongSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class CatalogVersionSyncCronJob extends GeneratedCatalogVersionSyncCronJob
{
    private static final Logger LOG = Logger.getLogger(CatalogVersionSyncCronJob.class);
    private static final String PROPERTY_SYNCHRONIZATION_DUMPFILE_TEMPDIR = "synchronization.dumpfile.tempdir";


    public boolean isIgnoreErrors()
    {
        EnumerationValue errorMode = getErrorMode();
        return (errorMode != null && GeneratedCronJobConstants.Enumerations.ErrorMode.IGNORE.equalsIgnoreCase(errorMode.getCode()));
    }


    @ForceJALO(reason = "something else")
    public Boolean isChangeRecordingEnabled(SessionContext ctx)
    {
        return Boolean.FALSE;
    }


    @ForceJALO(reason = "something else")
    public void setChangeRecordingEnabled(SessionContext ctx, boolean enabled)
    {
    }


    public ItemSyncDescriptor addPendingItem(PK srcPK, PK tgtPK)
    {
        addPendingItems((List)Collections.singletonList(new PK[] {srcPK, tgtPK}));
        return null;
    }


    public void addPendingItems(List<PK[]> tokens)
    {
        if(tokens != null && !tokens.isEmpty())
        {
            CatalogVersionSyncScheduleMedia scheduleMedia = getScheduleMedia(true);
            File tmpFile = getFile(scheduleMedia);
            try
            {
                SyncScheduleWriter sswr = createScheduleWriter(tmpFile, true, 0, 0);
                for(PK[] token : tokens)
                {
                    PK srcPK = token[0];
                    PK tgtPK = token[1];
                    PK tsPK = (token.length > 2) ? token[2] : null;
                    if(srcPK == null && tgtPK == null)
                    {
                        throw new JaloInvalidParameterException("found invalid schedule token " + Arrays.asList(token) + " - cannot have src AND tgt null", 0);
                    }
                    try
                    {
                        sswr.write(new SyncSchedule(srcPK, tgtPK, tsPK, null, null));
                    }
                    catch(IOException e)
                    {
                        sswr.closeQuietly();
                        throw new JaloSystemException(e);
                    }
                }
                sswr.close();
                scheduleMedia.setFile(tmpFile);
            }
            catch(IOException e)
            {
                throw new JaloSystemException(e);
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
            finally
            {
                FileUtils.deleteQuietly(tmpFile);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    public Collection getPendingItems()
    {
        return Collections.EMPTY_LIST;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    public Collection getFinishedItems()
    {
        return Collections.EMPTY_LIST;
    }


    protected CatalogVersionSyncScheduleMedia createNewScheduleMedia()
    {
        CatalogVersionSyncScheduleMedia ret;
        List<CatalogVersionSyncScheduleMedia> list = new ArrayList<>(getScheduleMedias());
        Item.ItemAttributeMap attributes = new Item.ItemAttributeMap();
        attributes.put("code", "sync_schedule_" + getPK().toString() + "_" + UUID.randomUUID());
        attributes.put("cronjob", this);
        try
        {
            ret = CatalogManager.getInstance().createCatalogVersionSyncScheduleMedia((Map)attributes);
            ret.setData(new byte[0]);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
        list.add(ret);
        setScheduleMedias(list);
        return ret;
    }


    public List<SyncSchedule> getOriginalSyncScheduleList()
    {
        List<SyncSchedule> syncSchedules = Lists.newArrayList();
        List<CatalogVersionSyncScheduleMedia> scheduleMedias = getScheduleMedias();
        if(scheduleMedias.isEmpty())
        {
            return syncSchedules;
        }
        SyncScheduleReader reader = null;
        try
        {
            CatalogVersionSyncScheduleMedia cvSyncScheduleMediaWithFullList = scheduleMedias.get(0);
            reader = getSyncSchedulesReader(cvSyncScheduleMediaWithFullList);
            while(reader.readNextLine())
            {
                syncSchedules.add(reader.getScheduleFromLine());
            }
            return syncSchedules;
        }
        finally
        {
            if(reader != null)
            {
                reader.closeQuietly();
            }
        }
    }


    private SyncScheduleReader getSyncSchedulesReader(CatalogVersionSyncScheduleMedia cvSyncScheduleMediaWithFullList)
    {
        try
        {
            InputStream mediaStream = getScheduleMediaStream((Media)cvSyncScheduleMediaWithFullList);
            return new SyncScheduleReader(new InputStreamReader(mediaStream, "UTF-8"), cvSyncScheduleMediaWithFullList
                            .getScheduledCountAsPrimitive());
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("cant handle UTF-8 encoding !? ", e);
        }
    }


    protected CatalogVersionSyncScheduleMedia getScheduleMedia(boolean create)
    {
        CatalogVersionSyncScheduleMedia media;
        List<CatalogVersionSyncScheduleMedia> scheduleMedias = getScheduleMedias();
        if(scheduleMedias.isEmpty())
        {
            if(create)
            {
                media = createNewScheduleMedia();
            }
            else
            {
                throw new IllegalStateException("got no schedule media for cronjob " + this);
            }
        }
        else
        {
            media = scheduleMedias.get(scheduleMedias.size() - 1);
        }
        return media;
    }


    protected File getFile(CatalogVersionSyncScheduleMedia scheduleMedia)
    {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File tmpFile = null;
        try
        {
            inputStream = getScheduleMediaStream((Media)scheduleMedia);
            outputStream = new FileOutputStream(tmpFile = File.createTempFile(scheduleMedia.getDataPK().toString(), null));
            IOUtils.copy(inputStream, outputStream);
            if(tmpFile == null)
            {
                throw new IllegalStateException("schedule media " + scheduleMedia + " got no data files");
            }
            return tmpFile;
        }
        catch(MediaNotFoundException e)
        {
            FileUtils.deleteQuietly(tmpFile);
            throw new IllegalStateException("schedule media " + scheduleMedia + " got no data files", e);
        }
        catch(IOException e)
        {
            FileUtils.deleteQuietly(tmpFile);
            throw new IllegalStateException("Problem with creating temp file from media input stream", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }


    public SyncScheduleReader createSyncScheduleReader()
    {
        try
        {
            CatalogVersionSyncScheduleMedia media = getScheduleMedia(false);
            InputStream mediaStream = getScheduleMediaStream((Media)media);
            return new SyncScheduleReader(new InputStreamReader(mediaStream, "UTF-8"), media.getScheduledCountAsPrimitive());
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("cant handle UTF-8 encoding !? ", e);
        }
    }


    private InputStream getScheduleMediaStream(Media media)
    {
        Preconditions.checkArgument((media.getFolder() != null), "MediaFolder in scheduleMedia object is required");
        if(media.getFolder() == null)
        {
            throw new IllegalStateException("MediaFolder in scheduleMedia object is required");
        }
        return MediaManager.getInstance().getMediaAsStream(media.getFolder().getQualifier(), media.getLocation());
    }


    protected String getDumpFileName()
    {
        return "sync_dump_" + getPK().toString() + ".csv";
    }


    protected String getDumpFileTempName()
    {
        return "sync_dump_" + getPK().toString() + "_old.csv";
    }


    protected SyncScheduleWriter createScheduleWriter(File file, boolean append, int count, int deadlockCount)
    {
        try
        {
            return (SyncScheduleWriter)new FileSyncScheduleWriter(file, append, count, deadlockCount);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("cant handle UTF-8 encoding !? ", e);
        }
        catch(FileNotFoundException e)
        {
            throw new IllegalStateException("media file invalid ?!", e);
        }
    }


    public boolean hasSchedules()
    {
        return !getScheduleMedias().isEmpty();
    }


    public SyncScheduleWriter createDumpScheduleWriter(SyncStatusHolder statusHandler)
    {
        if(statusHandler.getLastLine() > 0 && statusHandler.getLastLineDumpCount() > 0)
        {
            return recreateDumpWriter(statusHandler.getLastLineDumpCount());
        }
        String dumpFileName = getDumpFileName();
        return createScheduleWriter(getFileInTempDir(dumpFileName), false, 0, 0);
    }


    private File getFileInTempDir(String dumpFileName)
    {
        Objects.requireNonNull(dumpFileName);
        Path path = Paths.get(getDumpFilesDir(), new String[0]);
        if(Files.notExists(path, new java.nio.file.LinkOption[0]))
        {
            try
            {
                Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch(IOException e)
            {
                throw new IllegalStateException("could not create a directory for sync dump files", e);
            }
        }
        else
        {
            if(!Files.isDirectory(path, new java.nio.file.LinkOption[0]))
            {
                throw new IllegalStateException("temp directory for catalog version sync cron job is invalid");
            }
            if(!Files.isWritable(path))
            {
                throw new IllegalStateException("invalid permissions defined for temp dir for catalog version sync job dump files");
            }
        }
        return new File(getDumpFilesDir(), dumpFileName);
    }


    protected String getDumpFilesDir()
    {
        return Config.getString("synchronization.dumpfile.tempdir", Paths.get(".", new String[0]).toString());
    }


    protected SyncScheduleWriter recreateDumpWriter(int validDumpCount)
    {
        try
        {
            File toReadFrom, dump = getFileInTempDir(getDumpFileName());
            File dumpRenamed = getFileInTempDir(getDumpFileTempName());
            if(dumpRenamed.exists())
            {
                if(dump.exists())
                {
                    dump.delete();
                }
                toReadFrom = dumpRenamed;
            }
            else if(dump.exists())
            {
                toReadFrom = dump;
            }
            else
            {
                throw new IllegalStateException("no last dump file exists");
            }
            SyncScheduleReader reader = new SyncScheduleReader(new InputStreamReader(new FileInputStream(toReadFrom), "UTF-8"), -1);
            File newDumpFile = getFileInTempDir(getDumpFileName());
            SyncScheduleWriter writer = createScheduleWriter(newDumpFile, false, 0, 0);
            int toCopy = validDumpCount;
            while(toCopy > 0 && reader.readNextLine())
            {
                SyncSchedule rec = reader.getScheduleFromLine();
                writer.write(rec);
                toCopy--;
            }
            reader.closeQuietly();
            toReadFrom.delete();
            return writer;
        }
        catch(IOException e)
        {
            File toReadFrom;
            throw new RuntimeException(toReadFrom);
        }
    }


    public void storeDumpedSchedules(SyncScheduleWriter scheduleWriter)
    {
        try
        {
            scheduleWriter.close();
            CatalogVersionSyncScheduleMedia cvssm = createNewScheduleMedia();
            cvssm.setScheduledCount(scheduleWriter.getCount());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Saved " + scheduleWriter
                                .getCount() + " dumps at media " + cvssm.getCode() + "(" + cvssm.getPK() + ")");
            }
            cvssm.setFile(((FileSyncScheduleWriter)scheduleWriter).getFile());
            if(scheduleWriter instanceof FileSyncScheduleWriter)
            {
                ((FileSyncScheduleWriter)scheduleWriter).getFile().delete();
            }
        }
        catch(IOException e)
        {
            throw new IllegalStateException("could not store dump media", e);
        }
        catch(JaloBusinessException e)
        {
            throw new IllegalStateException("could not store dump media", e);
        }
    }


    public boolean isLastDumpsDifferent()
    {
        SyncScheduleReader oldDumpReader = null;
        SyncScheduleReader newDumpReader = null;
        try
        {
            List<CatalogVersionSyncScheduleMedia> medias = getScheduleMedias();
            if(medias.size() < 2)
            {
                return true;
            }
            CatalogVersionSyncScheduleMedia oldDumpMedia = medias.get(medias.size() - 2);
            CatalogVersionSyncScheduleMedia newDumpMedia = medias.get(medias.size() - 1);
            int oldCount = oldDumpMedia.getScheduledCountAsPrimitive();
            int newCount = newDumpMedia.getScheduledCountAsPrimitive();
            oldDumpReader = new SyncScheduleReader(new InputStreamReader(getScheduleMediaStream((Media)oldDumpMedia), "UTF-8"), oldCount);
            newDumpReader = new SyncScheduleReader(new InputStreamReader(getScheduleMediaStream((Media)newDumpMedia), "UTF-8"), newCount);
            YLongSet oldSrcSet = new YLongSet(oldCount);
            YLongSet newSrcSet = new YLongSet(newCount);
            YLongSet oldTgtSet = new YLongSet(oldCount / 2);
            YLongSet newTgtSet = new YLongSet(newCount / 2);
            if(LOG.isInfoEnabled())
            {
                LOG.info("comparing last dumps (" + oldCount + "/" + oldDumpMedia.getPK() + " vs " + newCount + "/" + newDumpMedia
                                .getPK() + ") - this might take some time...");
            }
            while(newDumpReader.readNextLine())
            {
                SyncSchedule scheduleFromLine = newDumpReader.getScheduleFromLine();
                if(scheduleFromLine.isDeadlockVictim())
                {
                    logDumpsConsideredDifferentBecauseOfDeadLockVictimInLastDump();
                    return true;
                }
                readSyncSchedule4Compare(newSrcSet, newTgtSet, scheduleFromLine);
            }
            while(oldDumpReader.readNextLine())
            {
                readSyncSchedule4Compare(oldSrcSet, oldTgtSet, oldDumpReader.getScheduleFromLine());
            }
            boolean different = (!oldSrcSet.equals(newSrcSet) || !oldTgtSet.equals(newTgtSet));
            logComparisonResult(different);
            return different;
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("cant handle UTF-8 encoding !? ", e);
        }
        finally
        {
            if(oldDumpReader != null)
            {
                oldDumpReader.closeQuietly();
            }
            if(newDumpReader != null)
            {
                newDumpReader.closeQuietly();
            }
        }
    }


    private void logDumpsConsideredDifferentBecauseOfDeadLockVictimInLastDump()
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("Deadlock victim found in last dump. This will cause next round of synchronization.");
        }
    }


    private void logComparisonResult(boolean different)
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("done comparing last dumps - dumps are " + (different ? "different" : "equal"));
        }
    }


    private void readSyncSchedule4Compare(YLongSet srcSet, YLongSet tgtSet, SyncSchedule syncs)
    {
        if(syncs.getSrcPK() != null)
        {
            srcSet.add(syncs.getSrcPK().getLongValue());
        }
        if(syncs.getTgtPK() != null)
        {
            tgtSet.add(syncs.getTgtPK().getLongValue());
        }
    }


    public SyncStatusHolder createStatusHolder()
    {
        if(getTenant().getConfig().getBoolean("catalog.sync.persist.status", true))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Using file based status holder (allows continuing crashed sync)");
            }
            return (SyncStatusHolder)new RAFileSyncStatusWriter("Sync_status_" + getPK() + ".log");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Using in-memory status holder (will not be able to resume sync in the case of a system crash)");
        }
        return (SyncStatusHolder)new DefaultSyncStatusHolder();
    }


    protected CatalogVersionSyncCronJobHistory instantiateCronJobHistory() throws JaloGenericCreationException, JaloAbstractTypeException
    {
        ComposedType composedType = TypeManager.getInstance().getComposedType(CatalogVersionSyncCronJobHistory.class);
        ImmutableMap immutableMap = ImmutableMap.of("cronJobCode", getCode(), "jobCode", getJob().getCode(), "startTime", new Date(), "status",
                        getStatus(), "fullSync",
                        isFullSync());
        return (CatalogVersionSyncCronJobHistory)composedType.newInstance((Map)immutableMap);
    }
}
