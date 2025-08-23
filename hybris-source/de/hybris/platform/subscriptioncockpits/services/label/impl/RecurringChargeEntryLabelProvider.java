package de.hybris.platform.subscriptioncockpits.services.label.impl;

import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.commons.lang.StringUtils;

public class RecurringChargeEntryLabelProvider extends AbstractSubscriptionModelLabelProvider<RecurringChargeEntryModel>
{
    protected String getItemLabel(RecurringChargeEntryModel model)
    {
        String label, price = "<null>";
        String currency = "<null>";
        String cycleStart = "<null>";
        String cycleEnd = "";
        if(model == null)
        {
            return getL10NService().getLocalizedString("cockpit.recurringchargeentryperiod.intervall.name", new Object[] {"<null>", "<null>", "<null>", "<null>"});
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
        if(model.getCycleStart() != null)
        {
            cycleStart = Integer.toString(model.getCycleStart().intValue());
        }
        if(model.getCycleEnd() != null)
        {
            cycleEnd = Integer.toString(model.getCycleEnd().intValue());
        }
        if(StringUtils.isEmpty(cycleEnd))
        {
            label = getL10NService().getLocalizedString("cockpit.recurringchargeentryperiod.intervall.openend.name", new Object[] {cycleStart, currency, price});
        }
        else
        {
            label = getL10NService().getLocalizedString("cockpit.recurringchargeentryperiod.intervall.name", new Object[] {cycleStart, cycleEnd, currency, price});
        }
        return label;
    }
}
