package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.ExportConfiguration;
import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.Exporter;
import de.hybris.platform.impex.jalo.exp.converter.ExportConverter;
import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import org.apache.log4j.Logger;

public class ImpExExportJob extends GeneratedImpExExportJob
{
    private static final Logger log = Logger.getLogger(ImpExExportJob.class.getName());


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        boolean result = true;
        ImpExExportCronJob cron = (ImpExExportCronJob)cronJob;
        cron.setDataExportTarget(ExportUtils.createDataExportTarget(cron.getDataExportMediaCode()));
        cron.setMediasExportTarget(ExportUtils.createMediasExportTarget(cron.getMediasExportMediaCode()));
        try
        {
            ExportConverter converter = cron.getConverter();
            try
            {
                ExportConfiguration config = new ExportConfiguration(cron.getJobMedia(), cron.getMode());
                config.setDataExportTarget(cron.getDataExportTarget());
                config.setMediasExportTarget(cron.getMediasExportTarget());
                config.setFieldSeparator(String.valueOf(cron.getFieldSeparator()));
                config.setCommentCharacter(String.valueOf(cron.getCommentCharacterAsPrimitive()));
                config.setQuoteCharacter(String.valueOf(cron.getQuoteCharacterAsPrimitive()));
                config.setSingleFile(cron.isSingleFileAsPrimitive());
                MediaDataTranslator.setMediaDataHandler((MediaDataHandler)config.getMediaDataHandler());
                Export export = (new Exporter(config)).export();
                cron.setExport(export);
                if(converter != null)
                {
                    converter.setExport(export);
                    converter.start();
                }
                if(converter != null)
                {
                    converter.close();
                }
            }
            catch(Throwable throwable)
            {
                if(converter != null)
                {
                    try
                    {
                        converter.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
            result = false;
        }
        finally
        {
            MediaDataTranslator.unsetMediaDataHandler();
        }
        return cronJob.getFinishedResult(result);
    }
}
