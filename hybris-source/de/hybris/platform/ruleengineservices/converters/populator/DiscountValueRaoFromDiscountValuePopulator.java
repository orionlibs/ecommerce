package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.ruleengineservices.rao.DiscountValueRAO;
import de.hybris.platform.util.DiscountValue;
import java.math.BigDecimal;

public class DiscountValueRaoFromDiscountValuePopulator implements Populator<DiscountValue, DiscountValueRAO>
{
    public void populate(DiscountValue source, DiscountValueRAO target)
    {
        target.setValue(BigDecimal.valueOf(source.getAppliedValue()));
        target.setCurrencyIsoCode(source.getCurrencyIsoCode());
    }
}
