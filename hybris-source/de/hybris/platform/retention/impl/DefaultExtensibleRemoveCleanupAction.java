package de.hybris.platform.retention.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.job.AfterRetentionCleanupJobPerformable;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExtensibleRemoveCleanupAction extends AbstractExtensibleRemoveCleanupAction
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExtensibleRemoveCleanupAction.class);


    public void cleanup(AfterRetentionCleanupJobPerformable retentionJob, AbstractRetentionRuleModel rule, ItemToCleanup item)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item to cleanup", item);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Cleaning up item and its audit records: {}", item.getPk());
        }
        cleanupRelatedObjects((ItemModel)getModelService().get(item.getPk()));
        getModelService().remove(item.getPk());
        removeAuditRecords(item);
    }
}
