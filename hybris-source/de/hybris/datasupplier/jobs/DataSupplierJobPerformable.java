package de.hybris.datasupplier.jobs;

import de.hybris.datasupplier.data.DSSendResult;
import de.hybris.datasupplier.facades.DataSupplierFacade;
import de.hybris.datasupplier.model.DataSupplierCronJobModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.media.MediaService;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DataSupplierJobPerformable extends AbstractJobPerformable<DataSupplierCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(DataSupplierJobPerformable.class);
    private final DataSupplierFacade supplierFacade;
    private final MediaService mediaService;


    public DataSupplierJobPerformable(DataSupplierFacade supplierFacade, MediaService mediaService)
    {
        this.supplierFacade = supplierFacade;
        this.mediaService = mediaService;
    }


    public PerformResult perform(DataSupplierCronJobModel cronJob)
    {
        LOG.info("Supplying data to SLD");
        DSSendResult supplyResult = this.supplierFacade.supplyData();
        CronJobResult cronJobResult = null;
        switch(null.$SwitchMap$de$hybris$datasupplier$data$DSSendResult$SupplyStatus[supplyResult.getSupplyStatus().ordinal()])
        {
            case 1:
                LOG.info("SLD data sending is disbaled");
                cronJobResult = CronJobResult.SUCCESS;
                break;
            case 2:
                LOG.info("An error occured while supplying SLD data");
                cronJobResult = CronJobResult.ERROR;
                break;
            case 3:
                LOG.info("SLD data has been sent");
                cronJobResult = CronJobResult.SUCCESS;
                break;
            default:
                cronJobResult = CronJobResult.UNKNOWN;
                break;
        }
        if(!StringUtils.isEmpty(supplyResult.getPayload()) && Boolean.TRUE.equals(cronJob.getSavePayload()))
        {
            savePayload(cronJob, supplyResult.getPayload());
        }
        return new PerformResult(cronJobResult, CronJobStatus.FINISHED);
    }


    protected void savePayload(DataSupplierCronJobModel cronJob, String payload)
    {
        CatalogUnawareMediaModel mediaPayload = null;
        if(cronJob.getRecentPayload() == null)
        {
            mediaPayload = (CatalogUnawareMediaModel)this.modelService.create(CatalogUnawareMediaModel.class);
            mediaPayload.setCatalogVersion(null);
            mediaPayload.setCode("SLDPayload_" + cronJob.getCode() + (new Date()).getTime() + "_media");
            this.modelService.save(mediaPayload);
            cronJob.setRecentPayload(mediaPayload);
            this.modelService.save(cronJob);
        }
        else
        {
            mediaPayload = cronJob.getRecentPayload();
        }
        this.mediaService.setDataForMedia((MediaModel)mediaPayload, payload.getBytes());
    }
}
