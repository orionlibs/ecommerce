package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiOrderCancellation")
public class SapCpiOrderCancellation implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SapCpiConfig sapCpiConfig;
    private String orderId;
    private String rejectionReason;
    private List<SapCpiOrderCancellationItem> sapCpiOrderCancellationItems;


    public void setSapCpiConfig(SapCpiConfig sapCpiConfig)
    {
        this.sapCpiConfig = sapCpiConfig;
    }


    @XmlElement(name = "sapCpiConfig")
    public SapCpiConfig getSapCpiConfig()
    {
        return this.sapCpiConfig;
    }


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public String getOrderId()
    {
        return this.orderId;
    }


    public void setRejectionReason(String rejectionReason)
    {
        this.rejectionReason = rejectionReason;
    }


    public String getRejectionReason()
    {
        return this.rejectionReason;
    }


    public void setSapCpiOrderCancellationItems(List<SapCpiOrderCancellationItem> sapCpiOrderCancellationItems)
    {
        this.sapCpiOrderCancellationItems = sapCpiOrderCancellationItems;
    }


    @XmlElement(name = "sapCpiOrderCancellationItems")
    public List<SapCpiOrderCancellationItem> getSapCpiOrderCancellationItems()
    {
        return this.sapCpiOrderCancellationItems;
    }
}
