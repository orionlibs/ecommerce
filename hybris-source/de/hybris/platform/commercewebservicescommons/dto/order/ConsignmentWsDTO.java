package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "Consignment", description = "Representation of a Consignment")
public class ConsignmentWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Consignment code")
    private String code;
    @ApiModelProperty(name = "trackingID", value = "Consignment tracking identifier")
    private String trackingID;
    @ApiModelProperty(name = "status", value = "Consignment status")
    private String status;
    @ApiModelProperty(name = "statusDisplay", value = "Consignment status display")
    private String statusDisplay;
    @ApiModelProperty(name = "statusDate", value = "Consignment status date")
    private Date statusDate;
    @ApiModelProperty(name = "entries", value = "List of consignment entries")
    private List<ConsignmentEntryWsDTO> entries;
    @ApiModelProperty(name = "shippingAddress", value = "Shipping address")
    private AddressWsDTO shippingAddress;
    @ApiModelProperty(name = "deliveryPointOfService", value = "Delivery point of service")
    private PointOfServiceWsDTO deliveryPointOfService;
    @ApiModelProperty(name = "statusMessage")
    private String statusMessage;
    @ApiModelProperty(name = "shippingDate")
    private String shippingDate;
    @ApiModelProperty(name = "consignmentStatus")
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


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setStatusDisplay(String statusDisplay)
    {
        this.statusDisplay = statusDisplay;
    }


    public String getStatusDisplay()
    {
        return this.statusDisplay;
    }


    public void setStatusDate(Date statusDate)
    {
        this.statusDate = statusDate;
    }


    public Date getStatusDate()
    {
        return this.statusDate;
    }


    public void setEntries(List<ConsignmentEntryWsDTO> entries)
    {
        this.entries = entries;
    }


    public List<ConsignmentEntryWsDTO> getEntries()
    {
        return this.entries;
    }


    public void setShippingAddress(AddressWsDTO shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }


    public AddressWsDTO getShippingAddress()
    {
        return this.shippingAddress;
    }


    public void setDeliveryPointOfService(PointOfServiceWsDTO deliveryPointOfService)
    {
        this.deliveryPointOfService = deliveryPointOfService;
    }


    public PointOfServiceWsDTO getDeliveryPointOfService()
    {
        return this.deliveryPointOfService;
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
