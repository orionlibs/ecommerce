package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.ruleengineservices.rao.OrderEntryGroupRAO;

public class OrderEntryGroupRaoPopulator implements Populator<EntryGroup, OrderEntryGroupRAO>
{
    public void populate(EntryGroup source, OrderEntryGroupRAO target)
    {
        target.setEntryGroupId(source.getGroupNumber());
        target.setExternalReferenceId(source.getExternalReferenceId());
        target.setGroupType(source.getGroupType().getCode());
    }
}
