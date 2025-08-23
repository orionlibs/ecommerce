package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ConsignmentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String trackingID;
    private ConsignmentStatus status;
    private Date statusDate;
    private List<ConsignmentEntryData> entries;
    private AddressData shippingAddress;
    private PointOfServiceData deliveryPointOfService;
    private String statusDisplay;
    private String statusMessage;
    private String shippingDate;
    private String consignmentStatus;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setTrackingID(String trackingID)
    {
        this.trackingID = trackingID;
    }


    public String getTrackingID()
    {
        return this.trackingID;
    }


    public void setStatus(ConsignmentStatus status)
    {
        this.status = status;
    }


    public ConsignmentStatus getStatus()
    {
        return this.status;
    }


    public void setStatusDate(Date statusDate)
    {
        this.statusDate = statusDate;
    }


    public Date getStatusDate()
    {
        return this.statusDate;
    }


    public void setEntries(List<ConsignmentEntryData> entries)
    {
        this.entries = entries;
    }


    public List<ConsignmentEntryData> getEntries()
    {
        return this.entries;
    }


    public void setShippingAddress(AddressData shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }


    public AddressData getShippingAddress()
    {
        return this.shippingAddress;
    }


    public void setDeliveryPointOfService(PointOfServiceData deliveryPointOfService)
    {
        this.deliveryPointOfService = deliveryPointOfService;
    }


    public PointOfServiceData getDeliveryPointOfService()
    {
        return this.deliveryPointOfService;
    }


    public void setStatusDisplay(String statusDisplay)
    {
        this.statusDisplay = statusDisplay;
    }


    public String getStatusDisplay()
    {
        return this.statusDisplay;
    }


    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }


    public String getStatusMessage()
    {
        return this.statusMessage;
    }


    public void setShippingDate(String shippingDate)
    {
        this.shippingDate = shippingDate;
    }


    public String getShippingDate()
    {
        return this.shippingDate;
    }


    public void setConsignmentStatus(String consignmentStatus)
    {
        this.consignmentStatus = consignmentStatus;
    }


    public String getConsignmentStatus()
    {
        return this.consignmentStatus;
    }
}
