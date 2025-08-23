package de.hybris.platform.retention.impl;

import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.RetentionCleanupAction;
import de.hybris.platform.retention.job.AfterRetentionCleanupJobPerformable;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicRemoveCleanupAction implements RetentionCleanupAction
{
    public static final String SPRING_ID = "basicRemoveCleanupAction";
    private static final Logger LOG = LoggerFactory.getLogger(BasicRemoveCleanupAction.class);
    private ModelService modelService;
    private WriteAuditGateway writeAuditGateway;
    private AuditEnablementService auditEnablementService;


    public BasicRemoveCleanupAction(ModelService modelService, WriteAuditGateway writeAuditGateway, AuditEnablementService auditEnablementService)
    {
        this.modelService = modelService;
        this.writeAuditGateway = writeAuditGateway;
        this.auditEnablementService = auditEnablementService;
    }


    public void cleanup(AfterRetentionCleanupJobPerformable retentionJob, AbstractRetentionRuleModel rule, ItemToCleanup item)
    {
        LOG.debug("Removing item {}", item);
        this.modelService.remove(item.getPk());
        this.writeAuditGateway.removeAuditRecordsForType(item.getItemType(), item.getPk());
    }
}
