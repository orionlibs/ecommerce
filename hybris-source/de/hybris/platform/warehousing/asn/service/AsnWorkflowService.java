package de.hybris.platform.warehousing.asn.service;

import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;

public interface AsnWorkflowService
{
    void startAsnCancellationWorkflow(AdvancedShippingNoticeModel paramAdvancedShippingNoticeModel);
}
