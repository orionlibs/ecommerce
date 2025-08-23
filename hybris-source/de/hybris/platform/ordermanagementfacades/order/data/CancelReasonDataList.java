package de.hybris.platform.ordermanagementfacades.order.data;

import de.hybris.platform.basecommerce.enums.CancelReason;
import java.io.Serializable;
import java.util.List;

public class CancelReasonDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CancelReason> reasons;


    public void setReasons(List<CancelReason> reasons)
    {
        this.reasons = reasons;
    }


    public List<CancelReason> getReasons()
    {
        return this.reasons;
    }
}
