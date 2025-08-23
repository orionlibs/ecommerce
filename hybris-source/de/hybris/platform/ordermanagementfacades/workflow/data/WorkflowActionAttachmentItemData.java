package de.hybris.platform.ordermanagementfacades.workflow.data;

import java.io.Serializable;

public class WorkflowActionAttachmentItemData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String orderCode;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }
}
