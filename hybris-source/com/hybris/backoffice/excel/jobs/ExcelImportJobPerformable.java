package com.hybris.backoffice.excel.jobs;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.importing.ExcelImportService;
import com.hybris.backoffice.excel.importing.ExcelImportWorkbookPostProcessor;
import com.hybris.backoffice.excel.importing.ImpexConverter;
import com.hybris.backoffice.excel.importing.data.ExcelImportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.model.ExcelImportCronJobModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ExcelImportJobPerformable extends AbstractJobPerformable<ExcelImportCronJobModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(ExcelImportJobPerformable.class);
    private ExcelImportService excelImportService;
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelTemplateService excelTemplateService;
    private ExcelWorkbookService excelWorkbookService;
    private ExcelImportWorkbookPostProcessor excelImportWorkbookPostProcessor;
    private ImpexConverter impexConverter;
    private ImportService importService;
    private MediaService mediaService;
    private Boolean failOnError = Boolean.valueOf(true);


    public PerformResult perform(ExcelImportCronJobModel cronJob)
    {
        try
        {
            return mapResult(getImportService().importData(createImportConfig(cronJob)));
        }
        catch(RuntimeException ex)
        {
            LOG.error("Error occurred while importing excel file", ex);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
        }
    }


    protected ImportConfig createImportConfig(ExcelImportCronJobModel cronJob)
    {
        ImportConfig config = new ImportConfig();
        config.setFailOnError(getFailOnError().booleanValue());
        config.setScript(generateImpexScript(cronJob));
        config.setEnableCodeExecution(Boolean.FALSE);
        config.setHmcSavedValuesEnabled(true);
        config.setLocale(Locale.ENGLISH);
        if(cronJob.getReferencedContent() != null)
        {
            config.setMediaArchive((ImpExResource)new StreamBasedImpExResource(this.mediaService.getStreamFromMedia(cronJob.getReferencedContent()), "UTF-8"));
        }
        return config;
    }


    protected String generateImpexScript(ExcelImportCronJobModel cronJob)
    {
        Workbook workbook = getExcelWorkbookService().createWorkbook(getMediaService().getStreamFromMedia(cronJob.getExcelFile()));
        try
        {
            Impex impex = getExcelImportService().convertToImpex(workbook);
            getExcelImportWorkbookPostProcessor().process(new ExcelImportResult(workbook, impex));
            return getImpexConverter().convert(impex);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)workbook);
        }
    }


    protected PerformResult mapResult(ImportResult importResult)
    {
        if(importResult.isError())
        {
            logImpexResult(importResult);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
        }
        if(importResult.isSuccessful())
        {
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        }
        return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.UNKNOWN);
    }


    protected void logImpexResult(ImportResult importResult)
    {
        if(importResult == null || importResult.getUnresolvedLines() == null || importResult.isSuccessful())
        {
            return;
        }
        try
        {
            InputStream is = getMediaService().getStreamFromMedia((MediaModel)importResult.getUnresolvedLines());
            try
            {
                StringBuilder unresolvedLines = new StringBuilder("\tUnresolved impex lines\n");
                IOUtils.readLines(is).forEach(line -> {
                    unresolvedLines.append(line);
                    unresolvedLines.append("\n");
                });
                if(LOG.isWarnEnabled())
                {
                    LOG.warn(unresolvedLines.toString());
                }
                if(is != null)
                {
                    is.close();
                }
            }
            catch(Throwable throwable)
            {
                if(is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            LOG.error("Cannot read unresolved lines: {}", e);
        }
    }


    public ExcelImportWorkbookPostProcessor getExcelImportWorkbookPostProcessor()
    {
        return this.excelImportWorkbookPostProcessor;
    }


    @Required
    public void setExcelImportWorkbookPostProcessor(ExcelImportWorkbookPostProcessor excelImportWorkbookPostProcessor)
    {
        this.excelImportWorkbookPostProcessor = excelImportWorkbookPostProcessor;
    }


    public ExcelImportService getExcelImportService()
    {
        return this.excelImportService;
    }


    @Required
    public void setExcelImportService(ExcelImportService excelImportService)
    {
        this.excelImportService = excelImportService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelTemplateService getExcelTemplateService()
    {
        return this.excelTemplateService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setExcelTemplateService(ExcelTemplateService excelTemplateService)
    {
        this.excelTemplateService = excelTemplateService;
    }


    public ExcelWorkbookService getExcelWorkbookService()
    {
        return this.excelWorkbookService;
    }


    @Required
    public void setExcelWorkbookService(ExcelWorkbookService excelWorkbookService)
    {
        this.excelWorkbookService = excelWorkbookService;
    }


    public ImpexConverter getImpexConverter()
    {
        return this.impexConverter;
    }


    @Required
    public void setImpexConverter(ImpexConverter impexConverter)
    {
        this.impexConverter = impexConverter;
    }


    public ImportService getImportService()
    {
        return this.importService;
    }


    @Required
    public void setImportService(ImportService importService)
    {
        this.importService = importService;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public Boolean getFailOnError()
    {
        return this.failOnError;
    }


    public void setFailOnError(Boolean failOnError)
    {
        this.failOnError = failOnError;
    }
}
