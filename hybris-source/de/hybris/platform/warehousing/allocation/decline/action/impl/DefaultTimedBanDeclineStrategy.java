package de.hybris.platform.warehousing.allocation.decline.action.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.allocation.decline.action.DeclineActionStrategy;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.sourcing.ban.service.SourcingBanService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTimedBanDeclineStrategy implements DeclineActionStrategy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTimedBanDeclineStrategy.class);
    private SourcingBanService sourcingBanService;


    public void execute(DeclineEntry declineEntry)
    {
        ServicesUtil.validateParameterNotNull(declineEntry, "Decline Entry cannot be null");
        LOGGER.debug("Default Decline Action Ban Strategy is being invoked, a Sourcing Ban will be created for warehouse: {}", declineEntry
                        .getConsignmentEntry().getConsignment().getWarehouse());
        getSourcingBanService().createSourcingBan(declineEntry.getConsignmentEntry().getConsignment().getWarehouse());
    }


    public void execute(Collection<DeclineEntry> declineEntries)
    {
        ServicesUtil.validateIfAnyResult(declineEntries, "Nothing to decline");
        DeclineEntry declineEntry = declineEntries.iterator().next();
        execute(declineEntry);
    }


    protected SourcingBanService getSourcingBanService()
    {
        return this.sourcingBanService;
    }


    @Required
    public void setSourcingBanService(SourcingBanService sourcingBanService)
    {
        this.sourcingBanService = sourcingBanService;
    }
}
