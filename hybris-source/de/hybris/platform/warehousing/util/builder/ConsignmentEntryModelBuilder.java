package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public class ConsignmentEntryModelBuilder
{
    private final ConsignmentEntryModel model = new ConsignmentEntryModel();


    private ConsignmentEntryModel getModel()
    {
        return this.model;
    }


    public static ConsignmentEntryModelBuilder aModel()
    {
        return new ConsignmentEntryModelBuilder();
    }


    public ConsignmentEntryModel build()
    {
        return getModel();
    }


    public ConsignmentEntryModelBuilder withQuantity(Long quantity)
    {
        getModel().setQuantity(quantity);
        return this;
    }


    public ConsignmentEntryModelBuilder withConsignment(ConsignmentModel consignment)
    {
        getModel().setConsignment(consignment);
        return this;
    }


    public ConsignmentEntryModelBuilder withOrderEntry(AbstractOrderEntryModel entry)
    {
        getModel().setOrderEntry(entry);
        return this;
    }
}
