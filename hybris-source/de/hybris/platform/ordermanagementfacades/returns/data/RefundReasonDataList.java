package de.hybris.platform.ordermanagementfacades.returns.data;

import de.hybris.platform.basecommerce.enums.RefundReason;
import java.io.Serializable;
import java.util.List;

public class RefundReasonDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<RefundReason> refundReasons;


    public void setRefundReasons(List<RefundReason> refundReasons)
    {
        this.refundReasons = refundReasons;
    }


    public List<RefundReason> getRefundReasons()
    {
        return this.refundReasons;
    }
}
