package de.hybris.platform.subscriptioncockpits.services.label.impl;

import de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OverageUsageChargeEntryLabelProvider extends AbstractSubscriptionModelLabelProvider<OverageUsageChargeEntryModel>
{
    protected String getItemLabel(OverageUsageChargeEntryModel model)
    {
        String currency = "<null>";
        String price = "<null>";
        if(model == null)
        {
            return getL10NService().getLocalizedString("cockpit.usagechargeentry.overage.name", new Object[] {"<null>", "<null>"});
        }
        if(model.getPrice() != null)
        {
            NumberFormat decimalFormat = DecimalFormat.getInstance();
            price = decimalFormat.format(model.getPrice().doubleValue());
        }
        if(model.getCurrency() != null)
        {
            currency = model.getCurrency().getSymbol();
        }
        if(model.getUsageCharge() instanceof de.hybris.platform.subscriptionservices.model.VolumeUsageChargeModel)
        {
            return getL10NService().getLocalizedString("cockpit.usagechargeentry.overage.each.name", new Object[] {currency, price});
        }
        return getL10NService().getLocalizedString("cockpit.usagechargeentry.overage.name", new Object[] {currency, price});
    }
}
