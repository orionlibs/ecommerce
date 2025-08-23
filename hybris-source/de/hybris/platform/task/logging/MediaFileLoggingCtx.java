package de.hybris.platform.task.logging;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.LogFileModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLogger;
import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;

public class MediaFileLoggingCtx implements TaskLoggingCtx
{
    private final String cronJobId;
    private final String filename;
    private final TempFileWriter tempFileWriter;
    private final MediaFileLogListener listener;
    private final ModelService modelService;
    private final CronJobService cronJobService;
    private final MediaService mediaService;


    public MediaFileLoggingCtx(String cronJobId, String filename, Level effectiveLevel, String loggerName, ModelService modelService, CronJobService cronJobService, MediaService mediaService)
    {
        this.cronJobId = Objects.<String>requireNonNull(cronJobId, "cronJobId is required");
        this.filename = Objects.<String>requireNonNull(filename, "filename is required");
        this.modelService = Objects.<ModelService>requireNonNull(modelService, "modelService is required");
        this.cronJobService = Objects.<CronJobService>requireNonNull(cronJobService, "cronJobService is required");
        this.mediaService = Objects.<MediaService>requireNonNull(mediaService, "mediaService is required");
        this.tempFileWriter = new TempFileWriter();
        this.listener = new MediaFileLogListener(effectiveLevel, loggerName, this.tempFileWriter);
        HybrisLogger.addListener((HybrisLogListener)this.listener);
    }


    public void finishAndClose()
    {
        InputStream streamForTempFile = null;
        try
        {
            if(this.listener != null)
            {
                HybrisLogger.removeListener((HybrisLogListener)this.listener);
            }
            this.tempFileWriter.close();
            LogFileModel logFile = (LogFileModel)this.modelService.create(LogFileModel.class);
            logFile.setCode(this.filename);
            logFile.setOwner((ItemModel)this.cronJobService.getCronJob(this.cronJobId));
            this.modelService.save(logFile);
            streamForTempFile = this.tempFileWriter.getStreamForTempFile();
            this.mediaService.setStreamForMedia((MediaModel)logFile, streamForTempFile);
        }
        finally
        {
            this.tempFileWriter.deleteTempFile();
            IOUtils.closeQuietly(streamForTempFile);
        }
    }
}
