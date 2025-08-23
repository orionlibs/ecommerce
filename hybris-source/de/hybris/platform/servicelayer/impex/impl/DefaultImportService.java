package de.hybris.platform.servicelayer.impex.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.distributed.process.ImportProcessCreationData;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.DistributedImportProcessModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHelper;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImpExError;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.persistence.PersistenceUtils;
import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.platform.util.zip.SafeZipInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultImportService extends AbstractBusinessService implements ImportService
{
    static final String DISTRIBUTED_IMPEX_SYNCHRONOUS_REMOVEONSUCCESS_ENABLED = "impex.import.service.distributed.synchronous.removeonsuccess.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultImportService.class);
    private CronJobService cronJobService;
    private DistributedProcessService distributedProcessService;
    private ImportDataDumpStrategy importDataDumpStrategy;
    private MediaService mediaService;
    private MimeService mimeService;


    public ImportResult importData(ImpExResource script)
    {
        ImportConfig config = new ImportConfig();
        config.setScript(script);
        return importData(config);
    }


    public ImportResult importData(ImportConfig config)
    {
        if(isDistributedImpexEnabled(config))
        {
            return importDataUsingDistributedImpex(config);
        }
        return importDataUsingStandardImpex(config);
    }


    public Stream<? extends ImpExError> collectImportErrors(ImportResult result)
    {
        Objects.requireNonNull(result, "result is required");
        Preconditions.checkState(result.isFinished(), "resulting process must be finished to perform this operation");
        LOG.debug("Result object [has unresolved lines: {}, is finished: {}, is error: {}, is running: {}, is successful: {}", new Object[] {Boolean.valueOf(result.hasUnresolvedLines()), Boolean.valueOf(result.isFinished()),
                        Boolean.valueOf(result.isError()), Boolean.valueOf(result.isRunning()), Boolean.valueOf(result.isSuccessful())});
        if(!result.hasUnresolvedLines())
        {
            LOG.debug(">>>>>>>>>>>>> result has got no dump, returning empty stream <<<<<<<<<<<<<<<");
            return Stream.empty();
        }
        ImpExMediaModel unresolvedLines = result.getUnresolvedLines();
        InputStream streamFromMedia = this.mediaService.getStreamFromMedia((MediaModel)unresolvedLines);
        return StreamSupport.stream((Spliterator<? extends ImpExError>)new ImpExErrorSpliterator(streamFromMedia), false);
    }


    boolean isDistributedImpexEnabled(ImportConfig config)
    {
        Boolean enabledFromConfig = config.isDistributedImpexEnabled();
        if(enabledFromConfig != null)
        {
            return enabledFromConfig.booleanValue();
        }
        return Config.getBoolean("impex.import.service.distributed.enabled", false);
    }


    private ImportResult importDataUsingDistributedImpex(ImportConfig config)
    {
        Preconditions.checkState(isDistributedImpexEnabled(config), "To import using distributed ImpEx you must enable it in a ImportConfig object directly");
        ImportProcessCreationData creationData = createImportProcessCreationData(config);
        try
        {
            DistributedImportProcessModel distributedImportProcess = (DistributedImportProcessModel)this.distributedProcessService.create((ProcessCreationData)creationData);
            this.distributedProcessService.start(distributedImportProcess.getCode());
            if(config.isSynchronous())
            {
                waitForDistributedImpEx(distributedImportProcess.getCode());
                if(checkIfRemoveDistributedImpexData(config, distributedImportProcess))
                {
                    removeImportMetadata(distributedImportProcess);
                }
            }
            return createImportCronJobResult(distributedImportProcess.getImpExImportCronJob());
        }
        catch(Exception e)
        {
            return (ImportResult)new ImportCronJobResult(prepareErrorImportCronJob(creationData.getProcessCode()));
        }
    }


    private boolean checkIfRemoveDistributedImpexData(ImportConfig config, DistributedImportProcessModel distributedImportProcess)
    {
        return (isRemoveOnSuccessForDistributedImpexEnabled(config) && isDistributedImportSuccessful(distributedImportProcess));
    }


    private boolean isDistributedImportSuccessful(DistributedImportProcessModel distributedImportProcess)
    {
        return (this.cronJobService.isSuccessful((CronJobModel)distributedImportProcess
                        .getImpExImportCronJob()) && DistributedProcessState.SUCCEEDED == distributedImportProcess
                        .getState());
    }


    boolean isRemoveOnSuccessForDistributedImpexEnabled(ImportConfig config)
    {
        Boolean enabledFromImportConfig = config.isRemoveOnSuccessForDistributedImpex();
        return (enabledFromImportConfig != null) ? enabledFromImportConfig.booleanValue() : isRemoveOnSuccessForDistributedImpexEnabledGlobally();
    }


    private boolean isRemoveOnSuccessForDistributedImpexEnabledGlobally()
    {
        return Config.getBoolean("impex.import.service.distributed.synchronous.removeonsuccess.enabled", false);
    }


    private ImportResult createImportCronJobResult(ImpExImportCronJobModel impExImportCronJob)
    {
        if(((Item)getModelService().getSource(impExImportCronJob)).isAlive())
        {
            return (ImportResult)new ImportCronJobResult(impExImportCronJob);
        }
        return (ImportResult)new ImportCronJobResult(null);
    }


    private void removeImportMetadata(DistributedImportProcessModel distributedImportProcess)
    {
        ImportServiceHelper.clearImportBatchContent(getModelService(), (DistributedProcessModel)distributedImportProcess);
        ImpExImportCronJobModel importCronJob = distributedImportProcess.getImpExImportCronJob();
        String importJobCode = importCronJob.getCode();
        getModelService().remove(distributedImportProcess);
        getModelService().remove(importCronJob);
        LOG.debug("Successful removed import metadata code={}", importJobCode);
    }


    private ImpExImportCronJobModel prepareErrorImportCronJob(String processCode)
    {
        ImpExImportJobModel job = (ImpExImportJobModel)getModelService().create(ImpExImportJobModel.class);
        job.setCode(processCode);
        ImpExImportCronJobModel cronJob = (ImpExImportCronJobModel)getModelService().create(ImpExImportCronJobModel.class);
        cronJob.setCode(processCode);
        cronJob.setJob((JobModel)job);
        cronJob.setStatus(CronJobStatus.FINISHED);
        cronJob.setResult(CronJobResult.ERROR);
        return cronJob;
    }


    private DistributedImportProcessModel waitForDistributedImpEx(String processCode)
    {
        try
        {
            DistributedImportProcessModel process;
            do
            {
                process = (DistributedImportProcessModel)this.distributedProcessService.wait(processCode, 5L);
            }
            while(!DistributedProcessHelper.isFinished((DistributedProcessModel)process));
            ImpExImportCronJobModel cronJob = process.getImpExImportCronJob();
            while(!this.cronJobService.isFinished((CronJobModel)cronJob))
            {
                Thread.sleep(1000L);
                getModelService().refresh(cronJob);
            }
            return process;
        }
        catch(InterruptedException e)
        {
            throw new SystemException(e);
        }
    }


    private ImportProcessCreationData createImportProcessCreationData(ImportConfig config)
    {
        ImpExResource script = Objects.<ImpExResource>requireNonNull(config.getScript(), "ImpExResource script is required");
        ImpExMediaModel impexMedia = script.getMedia();
        ImportProcessCreationData.ImportProcessContext ctx = new ImportProcessCreationData.ImportProcessContext();
        ctx.setLocale(config.getLocale());
        ctx.setValidationMode(config.getValidationMode().getCode());
        ctx.setLogLevel(config.getDistributedImpexLogLevel());
        ctx.setSldEnabled(isSldForDataEnabled(config));
        ctx.setCodeExecutionEnabled(isCodeExecutionEnabled(config));
        ctx.setNodeGroup(config.getNodeGroup());
        Collection<ImpExResource> referencedData = config.getReferencedData();
        if(CollectionUtils.isNotEmpty(referencedData))
        {
            ctx.setImpExMediaModels((List)referencedData.stream().map(ImpExResource::getMedia).collect(Collectors.toList()));
        }
        String processCode = getProcessCode(config);
        if(this.mimeService.isZipRelatedMime(impexMedia.getMime()))
        {
            ImpExMediaModel main = null;
            List<ImpExMediaModel> medias = Lists.newArrayList();
            try
            {
                SafeZipInputStream zin = new SafeZipInputStream(this.mediaService.getStreamFromMedia((MediaModel)impexMedia));
                try
                {
                    for(SafeZipEntry entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry())
                    {
                        String extractionId = "-" + UUID.randomUUID().toString();
                        String code = processCode + "-" + processCode + entry.getName();
                        ImpExMediaModel m = createImpExMedia(code, extractionId);
                        m.setZipentry(entry.getName());
                        getModelService().save(m);
                        setData(m, zin, entry.getName());
                        medias.add(m);
                        if(isMainEntry((ZipEntry)entry, config.getMainScriptWithinArchive()))
                        {
                            main = m;
                        }
                    }
                    zin.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        zin.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            }
            catch(IOException e)
            {
                throw new IllegalStateException("Reading zip file failed", e.getCause());
            }
            if(main == null)
            {
                throw new IllegalStateException("No main entry in zip impex file");
            }
            if(CollectionUtils.isNotEmpty(ctx.getImpExMediaModels()))
            {
                medias.addAll(ctx.getImpExMediaModels());
            }
            ctx.setImpExMediaModels(medias);
            return new ImportProcessCreationData(processCode, this.mediaService.getStreamFromMedia((MediaModel)main), this.importDataDumpStrategy, ctx);
        }
        return new ImportProcessCreationData(processCode, this.mediaService.getStreamFromMedia((MediaModel)impexMedia), this.importDataDumpStrategy, ctx);
    }


    private void setData(ImpExMediaModel model, SafeZipInputStream zin, String realFileName) throws IOException
    {
        FileOutputStream tempImpexOS = null;
        DataInputStream tempImpexIS = null;
        File temp = null;
        try
        {
            temp = File.createTempFile("zipentry", "impex");
            tempImpexOS = new FileOutputStream(temp);
            MediaUtil.copy((InputStream)zin, tempImpexOS);
            tempImpexIS = new DataInputStream(new FileInputStream(temp));
            this.mediaService.setStreamForMedia((MediaModel)model, tempImpexIS, realFileName, null);
        }
        finally
        {
            IOUtils.closeQuietly(tempImpexIS);
            IOUtils.closeQuietly(tempImpexOS);
            if(temp != null && !FileUtils.deleteQuietly(temp))
            {
                LOG.warn("Can not delete temporary file {}", temp.getAbsolutePath());
            }
        }
    }


    private boolean isMainEntry(ZipEntry entry, String mainScriptWithinArchive)
    {
        if(StringUtils.isNotEmpty(mainScriptWithinArchive))
        {
            return mainScriptWithinArchive.equals(entry.getName());
        }
        return "importscript.impex".equals(entry.getName());
    }


    private ImpExMediaModel createImpExMedia(String code, String extractionId)
    {
        ImpExMediaModel impExMediaModel = (ImpExMediaModel)getModelService().create(ImpExMediaModel.class);
        impExMediaModel.setCode(code);
        impExMediaModel.setExtractionId(extractionId);
        return impExMediaModel;
    }


    private boolean isSldForDataEnabled(ImportConfig config)
    {
        return (config.isSldForData() == null) ? (!PersistenceUtils.isPersistenceLegacyModeEnabled()) :
                        config.isSldForData().booleanValue();
    }


    private boolean isCodeExecutionEnabled(ImportConfig config)
    {
        return (config.getEnableCodeExecution() != null && config.getEnableCodeExecution().booleanValue());
    }


    private String getProcessCode(ImportConfig config)
    {
        String configuredCode = config.getDistributedImpexProcessCode();
        return StringUtils.isNotBlank(configuredCode) ? configuredCode : ("distributed-impex-" + UUID.randomUUID().toString());
    }


    private ImportResult importDataUsingStandardImpex(ImportConfig config)
    {
        Preconditions.checkState(!isDistributedImpexEnabled(config), "To import using standard ImpEx distributed ImpEx flag must be disabled");
        ImpExImportCronJobModel cronJob = (ImpExImportCronJobModel)getModelService().create("ImpExImportCronJob");
        try
        {
            getModelService().initDefaults(cronJob);
        }
        catch(ModelInitializationException e)
        {
            throw new SystemException(e);
        }
        configureCronJob(cronJob, config);
        getModelService().saveAll(new Object[] {cronJob.getJob(), cronJob});
        importData(cronJob, config.isSynchronous(), config.isRemoveOnSuccess());
        return createImportCronJobResult(cronJob);
    }


    protected void configureCronJob(ImpExImportCronJobModel cronJob, ImportConfig config)
    {
        if(config.getScript() == null)
        {
            throw new IllegalArgumentException("You have to provide an import script");
        }
        cronJob.setJobMedia(config.getScript().getMedia());
        if(config.getLocale() != null)
        {
            cronJob.setLocale(String.valueOf(config.getLocale()));
        }
        if(config.getReferencedData() != null)
        {
            Collection<ImpExMediaModel> externalData = new ArrayList<>();
            for(ImpExResource res : config.getReferencedData())
            {
                externalData.add(res.getMedia());
            }
            cronJob.setExternalDataCollection(externalData);
        }
        if(config.getMediaArchive() != null)
        {
            cronJob.setMediasMedia((MediaModel)config.getMediaArchive().getMedia());
        }
        if(config.getMaxThreads() > 0)
        {
            cronJob.setMaxThreads(Integer.valueOf(config.getMaxThreads()));
        }
        cronJob.setErrorMode(config.isFailOnError() ? ErrorMode.FAIL : ErrorMode.IGNORE);
        if(config.getValidationMode() != null)
        {
            cronJob.setMode(ImpExValidationModeEnum.valueOf(config.getValidationMode().getCode().toUpperCase(LocaleHelper.getPersistenceLocale())));
        }
        cronJob.setDumpingAllowed(Boolean.valueOf(config.isDumpingEnabled()));
        cronJob.setEnableHmcSavedValues(Boolean.valueOf(config.isHmcSavedValuesEnabled()));
        cronJob.setZipentry(config.getMainScriptWithinArchive());
        if(config.isLegacyMode() != null)
        {
            cronJob.setLegacyMode(config.isLegacyMode());
        }
        if(config.getEnableCodeExecution() != null)
        {
            cronJob.setEnableCodeExecution(config.getEnableCodeExecution());
        }
    }


    protected void importData(ImpExImportCronJobModel cronJob, boolean synchronous, boolean removeOnSuccess)
    {
        String cronJobString = "cronjob with code=" + cronJob.getCode();
        if(LOG.isInfoEnabled())
        {
            LOG.info("Starting import {} using {}", synchronous ? "synchronous" : "asynchronous", cronJobString);
        }
        this.cronJobService.performCronJob((CronJobModel)cronJob, synchronous);
        getModelService().refresh(cronJob);
        if(synchronous)
        {
            if(this.cronJobService.isSuccessful((CronJobModel)cronJob))
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Import was successful (using {})", cronJobString);
                }
                if(removeOnSuccess)
                {
                    getModelService().remove(cronJob);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Successful removed {}", cronJobString);
                    }
                }
            }
            else
            {
                LOG.error("Import has caused an error, see logs of {} for further details", cronJobString);
            }
        }
        else if(removeOnSuccess)
        {
            LOG.warn("Will ignore removeOnSuccess flag, because of asynchronous mode");
        }
    }


    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    @Required
    public void setDistributedProcessService(DistributedProcessService distributedProcessService)
    {
        this.distributedProcessService = distributedProcessService;
    }


    @Required
    public void setImportDataDumpStrategy(ImportDataDumpStrategy importDataDumpStrategy)
    {
        this.importDataDumpStrategy = importDataDumpStrategy;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setMimeService(MimeService mimeService)
    {
        this.mimeService = mimeService;
    }
}
