package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

public class ReturnEntryModelBuilder
{
    private final ReturnEntryModel model = new ReturnEntryModel();


    private ReturnEntryModel getModel()
    {
        return this.model;
    }


    public static ReturnEntryModelBuilder aModel()
    {
        return new ReturnEntryModelBuilder();
    }


    public ReturnEntryModel build()
    {
        return getModel();
    }


    public ReturnEntryModelBuilder withStatus(ReturnStatus status)
    {
        getModel().setStatus(status);
        return this;
    }


    public ReturnEntryModelBuilder withAction(ReturnAction action)
    {
        getModel().setAction(action);
        return this;
    }


    public ReturnEntryModelBuilder withOrderEntry(AbstractOrderEntryModel orderEntry)
    {
        getModel().setOrderEntry(orderEntry);
        return this;
    }


    public ReturnEntryModelBuilder withReturnRequest(ReturnRequestModel returnRequest)
    {
        getModel().setReturnRequest(returnRequest);
        return this;
    }
}
