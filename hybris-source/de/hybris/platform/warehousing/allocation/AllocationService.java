package de.hybris.platform.warehousing.allocation;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import java.util.Collection;

public interface AllocationService
{
    Collection<ConsignmentModel> createConsignments(AbstractOrderModel paramAbstractOrderModel, String paramString, SourcingResults paramSourcingResults);


    ConsignmentModel createConsignment(AbstractOrderModel paramAbstractOrderModel, String paramString, SourcingResult paramSourcingResult);


    Collection<ConsignmentModel> manualReallocate(DeclineEntries paramDeclineEntries);


    void autoReallocate(DeclineEntries paramDeclineEntries);
}
