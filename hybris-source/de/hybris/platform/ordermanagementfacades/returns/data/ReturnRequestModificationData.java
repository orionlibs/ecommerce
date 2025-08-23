package de.hybris.platform.ordermanagementfacades.returns.data;

import java.io.Serializable;
import java.util.List;

public class ReturnRequestModificationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ReturnEntryModificationData> returnEntries;
    private Boolean refundDeliveryCost;


    public void setReturnEntries(List<ReturnEntryModificationData> returnEntries)
    {
        this.returnEntries = returnEntries;
    }


    public List<ReturnEntryModificationData> getReturnEntries()
    {
        return this.returnEntries;
    }


    public void setRefundDeliveryCost(Boolean refundDeliveryCost)
    {
        this.refundDeliveryCost = refundDeliveryCost;
    }


    public Boolean getRefundDeliveryCost()
    {
        return this.refundDeliveryCost;
    }
}
