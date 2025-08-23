package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.ruleengineservices.rao.DiscountValueRAO;
import java.math.BigDecimal;

public class DiscountValueRaoPopulator implements Populator<DiscountModel, DiscountValueRAO>
{
    public void populate(DiscountModel source, DiscountValueRAO target)
    {
        if(source.getValue() != null)
        {
            target.setValue(BigDecimal.valueOf(source.getValue().doubleValue()));
        }
        if(source.getCurrency() != null)
        {
            target.setCurrencyIsoCode(source.getCurrency().getIsocode());
        }
    }
}
