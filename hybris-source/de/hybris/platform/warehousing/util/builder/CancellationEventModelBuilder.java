package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.CancellationEventModel;

public class CancellationEventModelBuilder
{
    private final CancellationEventModel model = new CancellationEventModel();


    private CancellationEventModel getModel()
    {
        return this.model;
    }


    public static CancellationEventModelBuilder aModel()
    {
        return new CancellationEventModelBuilder();
    }


    public CancellationEventModel build()
    {
        return getModel();
    }


    public CancellationEventModelBuilder withConsignmentEntry(ConsignmentEntryModel consignmentEntry)
    {
        getModel().setConsignmentEntry(consignmentEntry);
        getModel().setOrderEntry((OrderEntryModel)consignmentEntry.getOrderEntry());
        return this;
    }


    public CancellationEventModelBuilder withReason(CancelReason reason)
    {
        getModel().setReason(reason.getCode());
        return this;
    }


    public CancellationEventModelBuilder withQuantity(long quantity)
    {
        getModel().setQuantity(quantity);
        return this;
    }


    public CancellationEventModelBuilder withStockLevel(StockLevelModel stockLevel)
    {
        getModel().setStockLevel(stockLevel);
        return this;
    }
}
