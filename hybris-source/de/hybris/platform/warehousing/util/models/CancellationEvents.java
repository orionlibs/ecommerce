package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.CancellationEventModel;
import de.hybris.platform.warehousing.util.builder.CancellationEventModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class CancellationEvents extends AbstractItems<CancellationEventModel>
{
    private WarehousingDao<CancellationEventModel> cancellationEventDao;
    private Consignments consignments;


    public CancellationEventModel Camera_Cancellation(Long quantity, CancelReason cancelReason, StockLevelModel stockLevel)
    {
        ConsignmentModel consignment = getConsignments().Camera_ShippedFromMontrealToMontrealNancyHome(ConsignmentStatus.READY, quantity);
        return (CancellationEventModel)getOrSaveAndReturn(() -> (CancellationEventModel)getCancellationEventDao().getByCode(consignment.getCode()),
                        () -> CancellationEventModelBuilder.aModel().withConsignmentEntry(consignment.getConsignmentEntries().iterator().next()).withQuantity(quantity.longValue()).withReason(cancelReason).withStockLevel(stockLevel).build());
    }


    public WarehousingDao<CancellationEventModel> getCancellationEventDao()
    {
        return this.cancellationEventDao;
    }


    @Required
    public void setCancellationEventDao(WarehousingDao<CancellationEventModel> cancellationEventDao)
    {
        this.cancellationEventDao = cancellationEventDao;
    }


    public Consignments getConsignments()
    {
        return this.consignments;
    }


    @Required
    public void setConsignments(Consignments consignments)
    {
        this.consignments = consignments;
    }
}
