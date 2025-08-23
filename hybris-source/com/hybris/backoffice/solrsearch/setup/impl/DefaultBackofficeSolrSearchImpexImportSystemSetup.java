package com.hybris.backoffice.solrsearch.setup.impl;

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

@SystemSetup(extension = "backofficesolrsearch")
public class DefaultBackofficeSolrSearchImpexImportSystemSetup extends AbstractBackofficeSearchImpexImportSystemSetup
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeSolrSearchImpexImportSystemSetup.class);
    private static final String INDEX_UPDATING_CRONJOB_NAME = "update-backofficeIndex-CronJob";
    private static final String PROPERTY_CRONJOB_NODE_GROUP = "backofficesearch.cronjob.nodegroup";


    public DefaultBackofficeSolrSearchImpexImportSystemSetup(ImportService importService, CommonI18NService commonI18NService, ModelService modelService, CronJobService cronJobService, ConfigurationService configurationService, BackofficeSearchSystemSetupConfig backofficeSolrSearchSystemSetupConfig,
                    FileBasedImpExResourceFactory fileBasedImpExResourceFactory)
    {
        super(importService, commonI18NService, modelService, cronJobService, configurationService, backofficeSolrSearchSystemSetupConfig, fileBasedImpExResourceFactory);
    }


    protected void adjustIndexUpdatingCronjob()
    {
        try
        {
            CronJobModel cronJob = getCronJobService().getCronJob("update-backofficeIndex-CronJob");
            String nodeGroup = getConfigurationService().getConfiguration().getString("backofficesearch.cronjob.nodegroup", "");
            if(cronJob != null && StringUtils.isNotBlank(nodeGroup))
            {
                cronJob.setNodeGroup(nodeGroup);
                getModelService().save(cronJob);
            }
        }
        catch(SystemException e)
        {
            LOG.warn("Error adjusting update-backofficeIndex-CronJob", (Throwable)e);
        }
    }


    protected Logger getLOG()
    {
        return LOG;
    }
}
