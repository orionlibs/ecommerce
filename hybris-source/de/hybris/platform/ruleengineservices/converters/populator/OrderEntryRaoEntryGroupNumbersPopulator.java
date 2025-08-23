package de.hybris.platform.ruleengineservices.converters.populator;

import com.google.common.collect.Lists;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import org.apache.commons.collections4.CollectionUtils;

public class OrderEntryRaoEntryGroupNumbersPopulator implements Populator<AbstractOrderEntryModel, OrderEntryRAO>
{
    public void populate(AbstractOrderEntryModel source, OrderEntryRAO target)
    {
        if(CollectionUtils.isNotEmpty(source.getEntryGroupNumbers()))
        {
            target.setEntryGroupNumbers(Lists.newArrayList(source.getEntryGroupNumbers()));
        }
    }
}
