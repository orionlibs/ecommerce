package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.ArrayList;

public class ReturnRequestModelBuilder
{
    private final ReturnRequestModel model = new ReturnRequestModel();


    private ReturnRequestModel getModel()
    {
        return this.model;
    }


    public static ReturnRequestModelBuilder aModel()
    {
        return new ReturnRequestModelBuilder();
    }


    public ReturnRequestModel build()
    {
        return getModel();
    }


    public ReturnRequestModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public ReturnRequestModelBuilder withReturnEntries(ReturnEntryModel... returnEntries)
    {
        getModel().setReturnEntries(new ArrayList());
        return this;
    }


    public ReturnRequestModelBuilder withOrder(OrderModel order)
    {
        getModel().setOrder(order);
        return this;
    }
}
