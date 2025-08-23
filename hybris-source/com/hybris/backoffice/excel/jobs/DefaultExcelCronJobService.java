package com.hybris.backoffice.excel.jobs;

import com.hybris.backoffice.model.ExcelImportCronJobModel;
import com.hybris.backoffice.model.ExcelImportJobModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelCronJobService implements ExcelCronJobService
{
    public static final String EXCEL_IMPORT_CRON_JOB_CODE_PREFIX = "ExcelImport";
    private String mediaFolder;
    private MediaService mediaService;
    private ModelService modelService;
    private String cronJobPerformableSpringId;


    public ExcelImportCronJobModel createImportJob(FileContent excelFile, FileContent referencedContentFile)
    {
        MediaModel excelMedia = createMedia(excelFile.getData(), excelFile.getName(), excelFile.getContentType());
        MediaModel referencedContentMedia = (referencedContentFile != null) ? createMedia(referencedContentFile.getData(), referencedContentFile.getName(), referencedContentFile.getContentType()) : null;
        return createCronJob(excelMedia, referencedContentMedia);
    }


    protected MediaModel createMedia(byte[] data, String fileName, String contentType)
    {
        CatalogUnawareMediaModel mediaModel = (CatalogUnawareMediaModel)getModelService().create("CatalogUnawareMedia");
        mediaModel.setCode(generateId(fileName));
        MediaFolderModel importFolder = getMediaService().getFolder(getMediaFolder());
        mediaModel.setFolder(importFolder);
        mediaModel.setRealFileName(fileName);
        mediaModel.setMime(contentType);
        getModelService().save(mediaModel);
        getMediaService().setDataForMedia((MediaModel)mediaModel, data);
        return (MediaModel)mediaModel;
    }


    protected ExcelImportCronJobModel createCronJob(MediaModel excelMedia, MediaModel referencedContentMedia)
    {
        String id = generateId(excelMedia.getRealFileName());
        ExcelImportJobModel jobModel = (ExcelImportJobModel)getModelService().create(ExcelImportJobModel.class);
        jobModel.setCode(id);
        jobModel.setSpringId(getCronJobPerformableSpringId());
        getModelService().save(jobModel);
        ExcelImportCronJobModel cronJobModel = (ExcelImportCronJobModel)getModelService().create(ExcelImportCronJobModel.class);
        cronJobModel.setCode(id);
        cronJobModel.setActive(Boolean.TRUE);
        cronJobModel.setJob((JobModel)jobModel);
        cronJobModel.setExcelFile(excelMedia);
        cronJobModel.setReferencedContent(referencedContentMedia);
        getModelService().save(cronJobModel);
        return cronJobModel;
    }


    protected String generateId(String fileName)
    {
        return String.format("%s%s%s", new Object[] {"ExcelImport", fileName.replaceAll("[^0-9a-zA-Z]", ""),
                        Long.valueOf(UUID.randomUUID().getMostSignificantBits())});
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


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setMediaFolder(String mediaFolder)
    {
        this.mediaFolder = mediaFolder;
    }


    public String getMediaFolder()
    {
        return this.mediaFolder;
    }


    public String getCronJobPerformableSpringId()
    {
        return this.cronJobPerformableSpringId;
    }


    @Required
    public void setCronJobPerformableSpringId(String cronJobPerformableSpringId)
    {
        this.cronJobPerformableSpringId = cronJobPerformableSpringId;
    }
}
