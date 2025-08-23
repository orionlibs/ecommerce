package de.hybris.platform.subscriptioncockpits.services.label.impl;

import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.subscriptionservices.model.VolumeUsageChargeModel;

public class VolumeUsageChargeLabelProvider extends AbstractSubscriptionModelLabelProvider<VolumeUsageChargeModel>
{
    protected String getItemLabel(VolumeUsageChargeModel model)
    {
        return getUsageChargeModelItemLabel((UsageChargeModel)model);
    }
}
