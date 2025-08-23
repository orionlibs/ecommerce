package de.hybris.platform.ordermanagementfacades.returns.data;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReplacementReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import java.io.Serializable;
import java.util.Date;

public class ReturnEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long expectedQuantity;
    private Long receivedQuantity;
    private Date reachedDate;
    private OrderEntryData orderEntry;
    private String notes;
    private ReturnAction action;
    private RefundReason refundReason;
    private ReplacementReason replacementReason;
    private PriceData refundAmount;
    private Date refundedDate;


    public void setExpectedQuantity(Long expectedQuantity)
    {
        this.expectedQuantity = expectedQuantity;
    }


    public Long getExpectedQuantity()
    {
        return this.expectedQuantity;
    }


    public void setReceivedQuantity(Long receivedQuantity)
    {
        this.receivedQuantity = receivedQuantity;
    }


    public Long getReceivedQuantity()
    {
        return this.receivedQuantity;
    }


    public void setReachedDate(Date reachedDate)
    {
        this.reachedDate = reachedDate;
    }


    public Date getReachedDate()
    {
        return this.reachedDate;
    }


    public void setOrderEntry(OrderEntryData orderEntry)
    {
        this.orderEntry = orderEntry;
    }


    public OrderEntryData getOrderEntry()
    {
        return this.orderEntry;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setAction(ReturnAction action)
    {
        this.action = action;
    }


    public ReturnAction getAction()
    {
        return this.action;
    }


    public void setRefundReason(RefundReason refundReason)
    {
        this.refundReason = refundReason;
    }


    public RefundReason getRefundReason()
    {
        return this.refundReason;
    }


    public void setReplacementReason(ReplacementReason replacementReason)
    {
        this.replacementReason = replacementReason;
    }


    public ReplacementReason getReplacementReason()
    {
        return this.replacementReason;
    }


    public void setRefundAmount(PriceData refundAmount)
    {
        this.refundAmount = refundAmount;
    }


    public PriceData getRefundAmount()
    {
        return this.refundAmount;
    }


    public void setRefundedDate(Date refundedDate)
    {
        this.refundedDate = refundedDate;
    }


    public Date getRefundedDate()
    {
        return this.refundedDate;
    }
}
