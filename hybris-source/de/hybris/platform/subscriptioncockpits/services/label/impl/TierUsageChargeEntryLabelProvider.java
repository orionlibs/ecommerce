package de.hybris.platform.subscriptioncockpits.services.label.impl;

import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TierUsageChargeEntryLabelProvider extends AbstractSubscriptionModelLabelProvider<TierUsageChargeEntryModel>
{
    protected String getItemLabel(TierUsageChargeEntryModel model)
    {
        if(model == null)
        {
            return getL10NService().getLocalizedString("cockpit.usagechargeentry.tier.name", new Object[] {"<null>", "<null>", "<null>", "<null>", "<null>"});
        }
        String currency = getCurrency(model);
        String usageUnit = getUsageUnit(model);
        String tierStart = getTierStart(model);
        String tierEnd = getTierEnd(model);
        String price = getPrice(model);
        if(model.getUsageCharge() instanceof de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel)
        {
            return getL10NService().getLocalizedString("cockpit.usagechargeentry.tier.each.name", new Object[] {tierStart, tierEnd, usageUnit, currency, price});
        }
        return getL10NService().getLocalizedString("cockpit.usagechargeentry.tier.name", new Object[] {tierStart, tierEnd, usageUnit, currency, price});
    }


    protected String getCurrency(TierUsageChargeEntryModel model)
    {
        return (model.getCurrency() == null) ? "<null>" : model.getCurrency().getSymbol();
    }


    protected String getUsageUnit(TierUsageChargeEntryModel model)
    {
        return (model.getUsageCharge() == null) ? "<null>" : model.getUsageCharge().getUsageUnit().getNamePlural();
    }


    protected String getTierStart(TierUsageChargeEntryModel model)
    {
        return (model.getTierStart() == null) ? "<null>" : model.getTierStart().toString();
    }


    protected String getTierEnd(TierUsageChargeEntryModel model)
    {
        return (model.getTierEnd() == null) ? "<null>" : model.getTierEnd().toString();
    }


    protected String getPrice(TierUsageChargeEntryModel model)
    {
        if(model.getPrice() != null)
        {
            NumberFormat decimalFormat = DecimalFormat.getInstance();
            return decimalFormat.format(model.getPrice().doubleValue());
        }
        return "<null>";
    }
}
