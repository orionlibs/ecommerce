package de.hybris.platform.subscriptioncockpits.services.label.impl;

import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.commons.lang.StringUtils;

public class OneTimeChargeEntryLabelProvider extends AbstractSubscriptionModelLabelProvider<OneTimeChargeEntryModel>
{
    protected String getItemLabel(OneTimeChargeEntryModel model)
    {
        String itemLabel, price = "<null>";
        String currency = "<null>";
        String billingEvent = "<null>";
        String name = null;
        if(model != null)
        {
            if(model.getPrice() != null)
            {
                NumberFormat decimalFormat = DecimalFormat.getInstance();
                price = decimalFormat.format(model.getPrice().doubleValue());
            }
            if(model.getCurrency() != null)
            {
                currency = model.getCurrency().getSymbol();
            }
            if(model.getBillingEvent() != null)
            {
                billingEvent = model.getBillingEvent().getNameInCart();
            }
            if(StringUtils.isNotEmpty(model.getName()))
            {
                name = model.getName();
            }
        }
        if(StringUtils.isEmpty(name))
        {
            itemLabel = getL10NService().getLocalizedString("cockpit.onetimechargeentry.noname", new Object[] {currency, price, billingEvent});
        }
        else
        {
            itemLabel = getL10NService().getLocalizedString("cockpit.onetimechargeentry.name", new Object[] {name, currency, price, billingEvent});
        }
        return itemLabel;
    }
}
