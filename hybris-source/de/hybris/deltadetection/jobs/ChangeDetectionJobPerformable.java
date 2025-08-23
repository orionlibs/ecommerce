package de.hybris.deltadetection.jobs;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.impl.CsvReportChangesCollector;
import de.hybris.deltadetection.model.ChangeDetectionJobModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ChangeDetectionJobPerformable extends AbstractJobPerformable<CronJobModel>
{
    private static final Logger LOG = Logger.getLogger(ChangeDetectionJobPerformable.class.getName());
    private ChangeDetectionService changeDetectionService;
    private MediaService mediaService;


    public PerformResult perform(CronJobModel cronJob)
    {
        PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        ChangeDetectionJobModel job = (ChangeDetectionJobModel)cronJob.getJob();
        LOG.info("Starting executing change detection job for type: " + job.getTypePK().getCode());
        String reportFolder = ConfigUtil.getPlatformConfig(ChangeDetectionJobPerformable.class).getSystemConfig().getTempDir().getAbsolutePath() + "/deltadetection/" + ConfigUtil.getPlatformConfig(ChangeDetectionJobPerformable.class).getSystemConfig().getTempDir().getAbsolutePath();
        String reportFileName = "report_" + job.getStreamId() + "_" + job.getTypePK().getCode() + "_" + cronJob.getCode() + "_" + job.getCode();
        FileWriter csvWriter = null;
        File reportFile = null;
        try
        {
            createReportFolderIfNecessary(reportFolder);
            Path path = Files.createTempFile(Paths.get(reportFolder, new String[0]), reportFileName, ".csv", (FileAttribute<?>[])new FileAttribute[0]);
            reportFile = path.toFile();
            csvWriter = new FileWriter(reportFile);
            this.changeDetectionService.collectChangesForType(job.getTypePK(), job.getStreamId(), (ChangesCollector)new CsvReportChangesCollector(csvWriter));
        }
        catch(Exception e)
        {
            LOG.error("Errors during generating report for type: " + job.getTypePK().getCode(), e);
            result = new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
        }
        finally
        {
            if(csvWriter != null)
            {
                try
                {
                    csvWriter.close();
                    generateMediaReportForJobInTransaction(job, reportFileName, reportFile);
                }
                catch(IOException ioe)
                {
                    LOG.error("An IOException occured during closing the CsvWriter! " + ioe.getMessage(), ioe);
                    result = new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
                }
                catch(Exception e)
                {
                    LOG.error("Generating media report failed for job " + job.getCode(), e);
                    result = new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
                }
            }
        }
        return result;
    }


    private void generateMediaReportForJobInTransaction(ChangeDetectionJobModel job, String reportFileName, File reportFile) throws Exception
    {
        Transaction.current().execute((TransactionBody)new Object(this, reportFileName, reportFile, job));
    }


    private void createReportFolderIfNecessary(String reportFolder) throws IOException
    {
        if(Files.notExists(Paths.get(reportFolder, new String[0]), new java.nio.file.LinkOption[0]))
        {
            Path newFolder = Files.createDirectories(Paths.get(reportFolder, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("New folder for csv report created under :" + newFolder.toFile().getAbsolutePath());
            }
        }
    }


    private CatalogUnawareMediaModel prepareMedia(String code, String reportFileName, String mimeType)
    {
        CatalogUnawareMediaModel media = (CatalogUnawareMediaModel)this.modelService.create(CatalogUnawareMediaModel.class);
        media.setCode(code);
        media.setMime(mimeType);
        media.setRealFileName(reportFileName);
        return media;
    }


    @Required
    public void setChangeDetectionService(ChangeDetectionService changeDetectionService)
    {
        this.changeDetectionService = changeDetectionService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
