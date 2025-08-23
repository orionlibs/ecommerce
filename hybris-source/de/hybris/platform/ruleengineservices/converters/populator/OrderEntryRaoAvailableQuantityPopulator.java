package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;

public class OrderEntryRaoAvailableQuantityPopulator implements Populator<AbstractOrderEntryModel, OrderEntryRAO>
{
    public void populate(AbstractOrderEntryModel source, OrderEntryRAO target)
    {
        target.setAvailableQuantity(source.getQuantity().intValue());
    }
}
