package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.math.BigDecimal;
import java.util.Date;

public class RefundEntryModelBuilder
{
    private final RefundEntryModel model = new RefundEntryModel();


    private RefundEntryModel getModel()
    {
        return this.model;
    }


    public static RefundEntryModelBuilder aModel()
    {
        return new RefundEntryModelBuilder();
    }


    public RefundEntryModel build()
    {
        return getModel();
    }


    public RefundEntryModelBuilder withStatus(ReturnStatus status)
    {
        getModel().setStatus(status);
        return this;
    }


    public RefundEntryModelBuilder withReason(RefundReason reason)
    {
        getModel().setReason(reason);
        return this;
    }


    public RefundEntryModelBuilder withAmount(BigDecimal amount)
    {
        getModel().setAmount(amount);
        return this;
    }


    public RefundEntryModelBuilder withExpectedQTY(Long quantity)
    {
        getModel().setExpectedQuantity(quantity);
        return this;
    }


    public RefundEntryModelBuilder withRefundedDate(Date date)
    {
        getModel().setRefundedDate(date);
        return this;
    }


    public RefundEntryModelBuilder withAction(ReturnAction action)
    {
        getModel().setAction(action);
        return this;
    }


    public RefundEntryModelBuilder withOrderEntry(AbstractOrderEntryModel orderEntry)
    {
        getModel().setOrderEntry(orderEntry);
        return this;
    }


    public RefundEntryModelBuilder withReturnRequest(ReturnRequestModel returnRequest)
    {
        getModel().setReturnRequest(returnRequest);
        return this;
    }
}
