package de.hybris.platform.ordermanagementfacades.order.cancel;

import de.hybris.platform.basecommerce.enums.CancelReason;
import java.io.Serializable;

public class OrderCancelRecordEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String cancelResult;
    private String refusedMessage;
    private CancelReason cancelReason;


    public void setCancelResult(String cancelResult)
    {
        this.cancelResult = cancelResult;
    }


    public String getCancelResult()
    {
        return this.cancelResult;
    }


    public void setRefusedMessage(String refusedMessage)
    {
        this.refusedMessage = refusedMessage;
    }


    public String getRefusedMessage()
    {
        return this.refusedMessage;
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
