package de.hybris.platform.commercewebservicescommons.dto.queues;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OrderStatusUpdateElement", description = "Representation of an Order Status Update Element")
public class OrderStatusUpdateElementWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of update element of order status")
    private String code;
    @ApiModelProperty(name = "status", value = "Status of update element")
    private String status;
    @ApiModelProperty(name = "baseSiteId", value = "BaseSite identifier")
    private String baseSiteId;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setBaseSiteId(String baseSiteId)
    {
        this.baseSiteId = baseSiteId;
    }


    public String getBaseSiteId()
    {
        return this.baseSiteId;
    }
}
