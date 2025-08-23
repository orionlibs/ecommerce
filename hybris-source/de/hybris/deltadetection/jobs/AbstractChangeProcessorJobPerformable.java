package de.hybris.deltadetection.jobs;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.model.AbstractChangeProcessorJobModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.CSVReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractChangeProcessorJobPerformable extends AbstractJobPerformable<CronJobModel>
{
    private static final Integer STREAM_ID_COLUMN = Integer.valueOf(0);
    private static final Integer ITEM_PK_COLUMN = Integer.valueOf(1);
    private static final Integer ITEM_COMPOSED_TYPE_COLUMN = Integer.valueOf(2);
    private static final Integer CHANGE_TYPE_COLUMN = Integer.valueOf(3);
    private static final Integer DATE_COLUMN = Integer.valueOf(4);
    private static final Integer INFO_COLUMN = Integer.valueOf(5);
    private static final Logger LOG = Logger.getLogger(AbstractChangeProcessorJobPerformable.class.getName());
    protected ChangeDetectionService changeDetectionService;
    protected MediaService mediaService;


    public PerformResult perform(CronJobModel cronJob)
    {
        Map<String, Object> ctx = new HashMap<>();
        init(ctx, cronJob);
        AbstractChangeProcessorJobModel job = (AbstractChangeProcessorJobModel)cronJob.getJob();
        MediaModel input = job.getInput();
        CronJobResult cronjobResult = CronJobResult.SUCCESS;
        CSVReader csvReader = null;
        try
        {
            int i = 0;
            csvReader = new CSVReader(this.mediaService.getStreamFromMedia(input), null);
            if(csvReader.readNextLine())
            {
                while(csvReader.readNextLine())
                {
                    i++;
                    if(!processChange(parseChange(csvReader.getLine()), ctx))
                    {
                        LOG.info("processing changes stopped");
                        break;
                    }
                }
                LOG.info("Processing changes finished, amount of processed changes in total: " + i);
            }
            else
            {
                LOG.info("The input media resource: " + input.getCode() + " contains only header, but no data. Processing finished, nothing consumed.");
            }
        }
        catch(Exception e)
        {
            LOG.error("Problems during reading csv data from resource: " + input.getRealFileName() + " Processing stopped.", e);
            cronjobResult = CronJobResult.ERROR;
        }
        finally
        {
            if(csvReader != null)
            {
                csvReader.closeQuietly();
            }
        }
        return new PerformResult(cronjobResult, CronJobStatus.FINISHED);
    }


    void init(Map<String, Object> ctx, CronJobModel cronjob)
    {
    }


    private ItemChangeDTO parseChange(Map<Integer, String> entryData)
    {
        Date parsedDate = new Date(Long.valueOf(entryData.get(DATE_COLUMN)).longValue());
        ItemChangeDTO change = new ItemChangeDTO(Long.valueOf(entryData.get(ITEM_PK_COLUMN)), parsedDate, ChangeType.valueOf(entryData.get(CHANGE_TYPE_COLUMN)), entryData.get(INFO_COLUMN), entryData.get(ITEM_COMPOSED_TYPE_COLUMN), entryData.get(STREAM_ID_COLUMN));
        return change;
    }


    abstract boolean processChange(ItemChangeDTO paramItemChangeDTO, Map<String, Object> paramMap);


    public ChangeDetectionService getChangeDetectionService()
    {
        return this.changeDetectionService;
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
