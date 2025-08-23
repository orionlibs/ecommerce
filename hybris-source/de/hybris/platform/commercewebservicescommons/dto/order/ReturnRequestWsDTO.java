package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "ReturnRequest", description = "Representation of a return request for an order")
public class ReturnRequestWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "cancellable", value = "Boolean flag for whether the return request is cancellable", example = "true")
    private Boolean cancellable;
    @ApiModelProperty(name = "code", value = "Return request code", example = "00000001")
    private String code;
    @ApiModelProperty(name = "creationTime", value = "Date of the return request creation", example = "2020-12-31T09:00:00+0000")
    private Date creationTime;
    @ApiModelProperty(name = "deliveryCost", value = "Delivery cost")
    private PriceWsDTO deliveryCost;
    @ApiModelProperty(name = "order", value = "Order related to the return request")
    private OrderWsDTO order;
    @ApiModelProperty(name = "refundDeliveryCost", value = "Boolean flag for whether there is a delivery cost for refund", example = "false")
    private Boolean refundDeliveryCost;
    @ApiModelProperty(name = "returnEntries", value = "Entries of the return request which contains information about the returned product")
    private List<ReturnRequestEntryWsDTO> returnEntries;
    @ApiModelProperty(name = "returnLabelDownloadUrl", value = "URL of the return label")
    private String returnLabelDownloadUrl;
    @ApiModelProperty(name = "rma", value = "Return merchandise authorization number", example = "00000001")
    private String rma;
    @ApiModelProperty(name = "status", value = "Status of return request")
    private String status;
    @ApiModelProperty(name = "subTotal", value = "Subtotal price")
    private PriceWsDTO subTotal;
    @ApiModelProperty(name = "totalPrice", value = "Total price")
    private PriceWsDTO totalPrice;


    public void setCancellable(Boolean cancellable)
    {
        this.cancellable = cancellable;
    }


    public Boolean getCancellable()
    {
        return this.cancellable;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    public Date getCreationTime()
    {
        return this.creationTime;
    }


    public void setDeliveryCost(PriceWsDTO deliveryCost)
    {
        this.deliveryCost = deliveryCost;
    }


    public PriceWsDTO getDeliveryCost()
    {
        return this.deliveryCost;
    }


    public void setOrder(OrderWsDTO order)
    {
        this.order = order;
    }


    public OrderWsDTO getOrder()
    {
        return this.order;
    }


    public void setRefundDeliveryCost(Boolean refundDeliveryCost)
    {
        this.refundDeliveryCost = refundDeliveryCost;
    }


    public Boolean getRefundDeliveryCost()
    {
        return this.refundDeliveryCost;
    }


    public void setReturnEntries(List<ReturnRequestEntryWsDTO> returnEntries)
    {
        this.returnEntries = returnEntries;
    }


    public List<ReturnRequestEntryWsDTO> getReturnEntries()
    {
        return this.returnEntries;
    }


    public void setReturnLabelDownloadUrl(String returnLabelDownloadUrl)
    {
        this.returnLabelDownloadUrl = returnLabelDownloadUrl;
    }


    public String getReturnLabelDownloadUrl()
    {
        return this.returnLabelDownloadUrl;
    }


    public void setRma(String rma)
    {
        this.rma = rma;
    }


    public String getRma()
    {
        return this.rma;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setSubTotal(PriceWsDTO subTotal)
    {
        this.subTotal = subTotal;
    }


    public PriceWsDTO getSubTotal()
    {
        return this.subTotal;
    }


    public void setTotalPrice(PriceWsDTO totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public PriceWsDTO getTotalPrice()
    {
        return this.totalPrice;
    }
}
