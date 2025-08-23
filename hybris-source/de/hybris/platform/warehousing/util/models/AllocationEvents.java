package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.util.builder.AllocationEventModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class AllocationEvents extends AbstractItems<AllocationEventModel>
{
    private WarehousingDao<AllocationEventModel> allocationEventDao;
    private Consignments consignments;


    public AllocationEventModel Camera_ShippedFromMontrealToMontrealNancyHome(Long quantity, StockLevelModel stockLevel)
    {
        ConsignmentModel consignment = getConsignments().Camera_ShippedFromMontrealToMontrealNancyHome(ConsignmentStatus.READY, quantity);
        return (AllocationEventModel)getOrSaveAndReturn(() -> (AllocationEventModel)getAllocationEventDao().getByCode(consignment.getCode()),
                        () -> AllocationEventModelBuilder.aModel().withConsignmentEntry(consignment.getConsignmentEntries().iterator().next()).withQuantity(quantity.longValue()).withStockLevel(stockLevel).build());
    }


    public AllocationEventModel Camera_ShippedFromBostonToMontrealNancyHome(Long quantity, StockLevelModel stockLevel)
    {
        ConsignmentModel consignment = getConsignments().Camera_ShippedFromBostonToMontrealNancyHome(ConsignmentStatus.READY, quantity);
        return (AllocationEventModel)getOrSaveAndReturn(() -> (AllocationEventModel)getAllocationEventDao().getByCode(consignment.getCode()),
                        () -> AllocationEventModelBuilder.aModel().withConsignmentEntry(consignment.getConsignmentEntries().iterator().next()).withQuantity(quantity.longValue()).withStockLevel(stockLevel).build());
    }


    public WarehousingDao<AllocationEventModel> getAllocationEventDao()
    {
        return this.allocationEventDao;
    }


    @Required
    public void setAllocationEventDao(WarehousingDao<AllocationEventModel> allocationEventDao)
    {
        this.allocationEventDao = allocationEventDao;
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
