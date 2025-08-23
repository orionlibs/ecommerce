package de.hybris.platform.ordermanagementfacades.returns.data;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReturnRequestData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String rma;
    private Date creationTime;
    private OrderData order;
    private PriceData deliveryCost;
    private List<ReturnEntryData> returnEntries;
    private ReturnStatus status;
    private Boolean refundDeliveryCost;
    private PriceData subtotal;
    private PriceData total;
    private String returnLabelDownloadUrl;
    private boolean cancellable;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setRma(String rma)
    {
        this.rma = rma;
    }


    public String getRma()
    {
        return this.rma;
    }


    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    public Date getCreationTime()
    {
        return this.creationTime;
    }


    public void setOrder(OrderData order)
    {
        this.order = order;
    }


    public OrderData getOrder()
    {
        return this.order;
    }


    public void setDeliveryCost(PriceData deliveryCost)
    {
        this.deliveryCost = deliveryCost;
    }


    public PriceData getDeliveryCost()
    {
        return this.deliveryCost;
    }


    public void setReturnEntries(List<ReturnEntryData> returnEntries)
    {
        this.returnEntries = returnEntries;
    }


    public List<ReturnEntryData> getReturnEntries()
    {
        return this.returnEntries;
    }


    public void setStatus(ReturnStatus status)
    {
        this.status = status;
    }


    public ReturnStatus getStatus()
    {
        return this.status;
    }


    public void setRefundDeliveryCost(Boolean refundDeliveryCost)
    {
        this.refundDeliveryCost = refundDeliveryCost;
    }


    public Boolean getRefundDeliveryCost()
    {
        return this.refundDeliveryCost;
    }


    public void setSubtotal(PriceData subtotal)
    {
        this.subtotal = subtotal;
    }


    public PriceData getSubtotal()
    {
        return this.subtotal;
    }


    public void setTotal(PriceData total)
    {
        this.total = total;
    }


    public PriceData getTotal()
    {
        return this.total;
    }


    public void setReturnLabelDownloadUrl(String returnLabelDownloadUrl)
    {
        this.returnLabelDownloadUrl = returnLabelDownloadUrl;
    }


    public String getReturnLabelDownloadUrl()
    {
        return this.returnLabelDownloadUrl;
    }


    public void setCancellable(boolean cancellable)
    {
        this.cancellable = cancellable;
    }


    public boolean isCancellable()
    {
        return this.cancellable;
    }
}
