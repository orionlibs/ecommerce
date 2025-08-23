package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;

public class AllocationEventModelBuilder
{
    private final AllocationEventModel model = new AllocationEventModel();


    private AllocationEventModel getModel()
    {
        return this.model;
    }


    public static AllocationEventModelBuilder aModel()
    {
        return new AllocationEventModelBuilder();
    }


    public AllocationEventModel build()
    {
        return getModel();
    }


    public AllocationEventModelBuilder withConsignmentEntry(ConsignmentEntryModel consignmentEntry)
    {
        getModel().setConsignmentEntry(consignmentEntry);
        return this;
    }


    public AllocationEventModelBuilder withStockLevel(StockLevelModel stockLevel)
    {
        getModel().setStockLevel(stockLevel);
        return this;
    }


    public AllocationEventModelBuilder withQuantity(long quantity)
    {
        getModel().setQuantity(quantity);
        return this;
    }
}
