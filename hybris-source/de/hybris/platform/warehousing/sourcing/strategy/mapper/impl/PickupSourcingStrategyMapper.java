package de.hybris.platform.warehousing.sourcing.strategy.mapper.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.sourcing.strategy.AbstractSourcingStrategyMapper;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickupSourcingStrategyMapper extends AbstractSourcingStrategyMapper
{
    private static final Logger LOG = LoggerFactory.getLogger(PickupSourcingStrategyMapper.class);


    public Boolean isMatch(SourcingContext context)
    {
        if(CollectionUtils.isEmpty(context.getOrderEntries()))
        {
            return Boolean.FALSE;
        }
        Boolean match = Boolean.valueOf(
                        !((Set)context.getOrderEntries().stream().map(entry -> isMatch(entry)).collect(Collectors.toSet())).contains(Boolean.FALSE));
        if(LOG.isDebugEnabled() && match.booleanValue())
        {
            LOG.debug("Match found for context.");
        }
        return match;
    }


    protected Boolean isMatch(AbstractOrderEntryModel entry)
    {
        return Boolean.valueOf((entry.getDeliveryPointOfService() != null));
    }
}
