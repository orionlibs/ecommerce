package de.hybris.platform.retention;

import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.retention.job.AfterRetentionCleanupJobPerformable;

public interface RetentionCleanupAction
{
    void cleanup(AfterRetentionCleanupJobPerformable paramAfterRetentionCleanupJobPerformable, AbstractRetentionRuleModel paramAbstractRetentionRuleModel, ItemToCleanup paramItemToCleanup);
}
