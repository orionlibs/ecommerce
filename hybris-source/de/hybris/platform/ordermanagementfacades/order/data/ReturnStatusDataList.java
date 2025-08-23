package de.hybris.platform.ordermanagementfacades.order.data;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import java.io.Serializable;
import java.util.List;

public class ReturnStatusDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ReturnStatus> statuses;


    public void setStatuses(List<ReturnStatus> statuses)
    {
        this.statuses = statuses;
    }


    public List<ReturnStatus> getStatuses()
    {
        return this.statuses;
    }
}
