package de.hybris.platform.subscriptioncockpits.services.label.impl;

import de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;

public class PerUnitUsageChargeLabelProvider extends AbstractSubscriptionModelLabelProvider<PerUnitUsageChargeModel>
{
    protected String getItemLabel(PerUnitUsageChargeModel model)
    {
        return getUsageChargeModelItemLabel((UsageChargeModel)model);
    }
}
