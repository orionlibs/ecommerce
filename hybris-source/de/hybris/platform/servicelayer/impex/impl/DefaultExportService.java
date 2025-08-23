package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ScriptValidationReader;
import de.hybris.platform.impex.model.cronjob.ImpExExportCronJobModel;
import de.hybris.platform.impex.model.exp.ExportModel;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ExportConfig;
import de.hybris.platform.servicelayer.impex.ExportResult;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExportService extends AbstractBusinessService implements ExportService
{
    private static final Logger LOG = Logger.getLogger(DefaultExportService.class);
    private CronJobService cronJobService;


    protected ExportModel exportData(ImpExExportCronJobModel cronJob, boolean synchronous)
    {
        String cronJobString = "cronjob with code=" + cronJob.getCode();
        if(LOG.isInfoEnabled())
        {
            LOG.info("Starting export " + (synchronous ? "synchronous" : "asynchronous") + " using " + cronJobString);
        }
        this.cronJobService.performCronJob((CronJobModel)cronJob, synchronous);
        getModelService().refresh(cronJob);
        if(synchronous)
        {
            if(this.cronJobService.isSuccessful((CronJobModel)cronJob))
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Export was successful (using " + cronJobString + ")");
                }
            }
            else
            {
                LOG.error("Export has caused an error, see logs of " + cronJobString + " for further details");
            }
        }
        return cronJob.getExport();
    }


    protected void configureCronJob(ImpExExportCronJobModel cronJob, ExportConfig config)
    {
        if(config.getScript() == null)
        {
            throw new IllegalArgumentException("You have to provide an export script");
        }
        cronJob.setJobMedia(config.getScript().getMedia());
        cronJob.setErrorMode(config.isFailOnError() ? ErrorMode.FAIL : ErrorMode.IGNORE);
        cronJob.setMediasExportMediaCode(config.getExportedMediaCode());
        cronJob.setDataExportMediaCode(config.getExportedDataCode());
        if(config.getSessionUser() != null)
        {
            cronJob.setSessionUser((UserModel)config.getSessionUser());
        }
        if(config.getEncoding() != null)
        {
            cronJob.setEncoding(EncodingEnum.valueOf(config.getEncoding()));
        }
        cronJob.setCommentCharacter(Character.valueOf(config.getCommentCharacter()));
        cronJob.setQuoteCharacter(Character.valueOf(config.getQuoteCharacter()));
        cronJob.setFieldSeparator(Character.valueOf(config.getFieldSeparator()));
        cronJob.setSingleFile(Boolean.valueOf(config.isSingleFile()));
        cronJob.setMode(ImpExValidationModeEnum.valueOf(config.getValidationMode().getCode().toUpperCase(LocaleHelper.getPersistenceLocale())));
    }


    public ExportResult exportData(ExportConfig config)
    {
        ImpExExportCronJobModel cronJob = (ImpExExportCronJobModel)getModelService().create("ImpExExportCronJob");
        try
        {
            getModelService().initDefaults(cronJob);
        }
        catch(ModelInitializationException e)
        {
            throw new SystemException(e);
        }
        configureCronJob(cronJob, config);
        getModelService().save(cronJob);
        exportData(cronJob, config.isSynchronous());
        return (ExportResult)new ExportCronJobResult(cronJob);
    }


    public DefaultImpExValidationResult validateExportScript(String script, ImpExValidationModeEnum validationMode)
    {
        EnumerationValue validationExportMode;
        if(validationMode == null)
        {
            validationExportMode = ImpExManager.getExportOnlyMode();
        }
        else
        {
            validationExportMode = ImpExManager.getValidationMode(validationMode.getCode());
        }
        ScriptValidationReader reader = new ScriptValidationReader(new CSVReader(script), validationExportMode, new DocumentIDRegistry(new CSVWriter(new StringWriter())));
        try
        {
            reader.validateScript();
        }
        catch(ImpExException error)
        {
            return new DefaultImpExValidationResult(false, error);
        }
        return new DefaultImpExValidationResult(true);
    }


    public ExportResult exportData(ImpExResource script)
    {
        ExportConfig config = new ExportConfig();
        config.setScript(script);
        return exportData(config);
    }


    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }
}
