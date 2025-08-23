package com.hybris.backoffice.searchservices.setup.impl;

import com.hybris.backoffice.search.setup.BackofficeSearchSystemSetupConfig;
import com.hybris.backoffice.search.setup.impl.AbstractBackofficeSearchImpexImportSystemSetup;
import com.hybris.backoffice.search.setup.impl.FileBasedImpExResourceFactory;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SystemSetup(extension = "backofficesearchservices")
public class DefaultBackofficeSearchservicesImpexImportSystemSetup extends AbstractBackofficeSearchImpexImportSystemSetup
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeSearchservicesImpexImportSystemSetup.class);
    private static final String INDEX_FULL_CRONJOB_NAME = "indexer-backoffice-product-full";
    private static final String INDEX_INCREMENTAL_CRONJOB_NAME = "indexer-backoffice-product-update";
    private static final String PROPERTY_CRONJOB_NODE_GROUP = "backoffice.search.services.cronjob.nodegroup";


    public DefaultBackofficeSearchservicesImpexImportSystemSetup(ImportService importService, CommonI18NService commonI18NService, ModelService modelService, CronJobService cronJobService, ConfigurationService configurationService,
                    BackofficeSearchSystemSetupConfig backofficeSearchservicesSystemSetupConfig, FileBasedImpExResourceFactory fileBasedImpExResourceFactory)
    {
        super(importService, commonI18NService, modelService, cronJobService, configurationService, backofficeSearchservicesSystemSetupConfig, fileBasedImpExResourceFactory);
    }


    protected void adjustIndexUpdatingCronjob()
    {
        try
        {
            CronJobModel fullCronJob = getCronJobService().getCronJob("indexer-backoffice-product-full");
            CronJobModel incrementalCronJob = getCronJobService().getCronJob("indexer-backoffice-product-update");
            String nodeGroup = getConfigurationService().getConfiguration().getString("backoffice.search.services.cronjob.nodegroup", "");
            if(fullCronJob != null && StringUtils.isNotBlank(nodeGroup))
            {
                fullCronJob.setNodeGroup(nodeGroup);
                getModelService().save(fullCronJob);
            }
            if(incrementalCronJob != null && StringUtils.isNotBlank(nodeGroup))
            {
                incrementalCronJob.setNodeGroup(nodeGroup);
                getModelService().save(incrementalCronJob);
            }
        }
        catch(SystemException e)
        {
            LOG.warn("Error adjusting CronJob", (Throwable)e);
        }
    }


    protected Logger getLOG()
    {
        return LOG;
    }
}
