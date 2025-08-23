/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.saprevenuecloudproduct.jobs;

import com.sap.hybris.saprevenuecloudproduct.enums.SapRevenueCloudReplicationModeEnum;
import com.sap.hybris.saprevenuecloudproduct.model.SapRevenueCloudProductCronjobModel;
import com.sap.hybris.saprevenuecloudproduct.service.SapRevenueCloudProductService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SapRevenueCloudProductImportJob extends AbstractJobPerformable<SapRevenueCloudProductCronjobModel>
{
    private final Logger LOG = LoggerFactory.getLogger(SapRevenueCloudProductImportJob.class);
    private ConfigurationService configurationService;
    private SapRevenueCloudProductService sapRevenueCloudProductService;
    private OutboundServiceFacade outboundServiceFacade;


    @Override
    public PerformResult perform(final SapRevenueCloudProductCronjobModel job)
    {
        // Check for the job replication mode and replication Date
        if(null != job.getReplicationMode()
                        && SapRevenueCloudReplicationModeEnum.DELTA.equals(job.getReplicationMode()))
        {
            Date lastSuccessRunTime = getSapRevenueCloudProductService()
                            .getProductReplicationDateForCronjob(job.getCode());
            if(null == lastSuccessRunTime)
            {
                LOG.info(String.format(
                                "Cannot find any successfully run history for the cronjob with code %s. Please perform atleast one FULL run",
                                job.getCode()));
                return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
            }
            job.setReplicationTime(lastSuccessRunTime);
        }
        else
        {
            job.setReplicationMode(SapRevenueCloudReplicationModeEnum.FULL);
        }
        // Send CronjobModel to SCPI
        getOutboundServiceFacade().send(job, "OutboundSapRevenueCloudProductCronjob", "scpiProductDestination")
                        .subscribe(
                                        // onNext
                                        responseEntityMap -> {
                                            job.setResult(CronJobResult.SUCCESS);
                                            job.setStatus(CronJobStatus.FINISHED);
                                            LOG.info(String.format("Product replication has been successfully completed "));
                                        }, error -> {
                                            job.setResult(CronJobResult.ERROR);
                                            job.setStatus(CronJobStatus.FINISHED);
                                            LOG.info(String.format("Product replication failed with message [%n%s]", error.getMessage()));
                                        }
                        );
        return new PerformResult(job.getResult(), job.getStatus());
    }


    /**
     * @return the configurationService
     */
    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    /**
     * @param configurationService the configurationService to set
     */
    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public SapRevenueCloudProductService getSapRevenueCloudProductService()
    {
        return sapRevenueCloudProductService;
    }


    public void setSapRevenueCloudProductService(SapRevenueCloudProductService sapRevenueCloudProductService)
    {
        this.sapRevenueCloudProductService = sapRevenueCloudProductService;
    }


    public OutboundServiceFacade getOutboundServiceFacade()
    {
        return outboundServiceFacade;
    }


    public void setOutboundServiceFacade(OutboundServiceFacade outboundServiceFacade)
    {
        this.outboundServiceFacade = outboundServiceFacade;
    }
}