package de.hybris.platform.ordermanagementfacades.cancellation.data;

import de.hybris.platform.basecommerce.enums.CancelReason;
import java.io.Serializable;

public class OrderCancelEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer orderEntryNumber;
    private Long cancelQuantity;
    private String notes;
    private CancelReason cancelReason;


    public void setOrderEntryNumber(Integer orderEntryNumber)
    {
        this.orderEntryNumber = orderEntryNumber;
    }


    public Integer getOrderEntryNumber()
    {
        return this.orderEntryNumber;
    }


    public void setCancelQuantity(Long cancelQuantity)
    {
        this.cancelQuantity = cancelQuantity;
    }


    public Long getCancelQuantity()
    {
        return this.cancelQuantity;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setCancelReason(CancelReason cancelReason)
    {
        this.cancelReason = cancelReason;
    }


    public CancelReason getCancelReason()
    {
        return this.cancelReason;
    }
}
