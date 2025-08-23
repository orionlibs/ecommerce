package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionResultWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

@ApiModel(value = "Order", description = "Representation of an Order")
public class OrderWsDTO extends AbstractOrderWsDTO
{
    @ApiModelProperty(name = "created", value = "Date of order creation")
    private Date created;
    @ApiModelProperty(name = "status", value = "Status of order")
    private String status;
    @ApiModelProperty(name = "statusDisplay", value = "Status display")
    private String statusDisplay;
    @ApiModelProperty(name = "guestCustomer", value = "Flag showing if customer is Guest customer")
    private Boolean guestCustomer;
    @ApiModelProperty(name = "consignments", value = "List of consignment")
    private List<ConsignmentWsDTO> consignments;
    @ApiModelProperty(name = "deliveryStatus", value = "Order delivery status")
    private String deliveryStatus;
    @ApiModelProperty(name = "deliveryStatusDisplay", value = "Order delivery status display")
    private String deliveryStatusDisplay;
    @ApiModelProperty(name = "unconsignedEntries", value = "List of unconsigned order entries")
    private List<OrderEntryWsDTO> unconsignedEntries;
    @ApiModelProperty(name = "cancellable", value = "Boolean flag showing if order is cancellable", example = "true")
    private Boolean cancellable;
    @ApiModelProperty(name = "returnable", value = "Boolean flag showing if order is returnable", example = "true")
    private Boolean returnable;
    @ApiModelProperty(name = "totalUnitCount")
    private Integer totalUnitCount;
    @ApiModelProperty(name = "purchaseOrderNumber", value = "Purchase order number")
    private String purchaseOrderNumber;
    @ApiModelProperty(name = "orgCustomer", value = "Customer who placed the order")
    private UserWsDTO orgCustomer;
    @ApiModelProperty(name = "costCenter", value = "Cost Center associated with the order")
    private B2BCostCenterWsDTO costCenter;
    @ApiModelProperty(name = "permissionResults", value = "Results of permissions associated with the order")
    private List<B2BPermissionResultWsDTO> permissionResults;


    public void setCreated(Date created)
    {
        this.created = created;
    }


    public Date getCreated()
    {
        return this.created;
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


    public void setGuestCustomer(Boolean guestCustomer)
    {
        this.guestCustomer = guestCustomer;
    }


    public Boolean getGuestCustomer()
    {
        return this.guestCustomer;
    }


    public void setConsignments(List<ConsignmentWsDTO> consignments)
    {
        this.consignments = consignments;
    }


    public List<ConsignmentWsDTO> getConsignments()
    {
        return this.consignments;
    }


    public void setDeliveryStatus(String deliveryStatus)
    {
        this.deliveryStatus = deliveryStatus;
    }


    public String getDeliveryStatus()
    {
        return this.deliveryStatus;
    }


    public void setDeliveryStatusDisplay(String deliveryStatusDisplay)
    {
        this.deliveryStatusDisplay = deliveryStatusDisplay;
    }


    public String getDeliveryStatusDisplay()
    {
        return this.deliveryStatusDisplay;
    }


    public void setUnconsignedEntries(List<OrderEntryWsDTO> unconsignedEntries)
    {
        this.unconsignedEntries = unconsignedEntries;
    }


    public List<OrderEntryWsDTO> getUnconsignedEntries()
    {
        return this.unconsignedEntries;
    }


    public void setCancellable(Boolean cancellable)
    {
        this.cancellable = cancellable;
    }


    public Boolean getCancellable()
    {
        return this.cancellable;
    }


    public void setReturnable(Boolean returnable)
    {
        this.returnable = returnable;
    }


    public Boolean getReturnable()
    {
        return this.returnable;
    }


    public void setTotalUnitCount(Integer totalUnitCount)
    {
        this.totalUnitCount = totalUnitCount;
    }


    public Integer getTotalUnitCount()
    {
        return this.totalUnitCount;
    }


    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber()
    {
        return this.purchaseOrderNumber;
    }


    public void setOrgCustomer(UserWsDTO orgCustomer)
    {
        this.orgCustomer = orgCustomer;
    }


    public UserWsDTO getOrgCustomer()
    {
        return this.orgCustomer;
    }


    public void setCostCenter(B2BCostCenterWsDTO costCenter)
    {
        this.costCenter = costCenter;
    }


    public B2BCostCenterWsDTO getCostCenter()
    {
        return this.costCenter;
    }


    public void setPermissionResults(List<B2BPermissionResultWsDTO> permissionResults)
    {
        this.permissionResults = permissionResults;
    }


    public List<B2BPermissionResultWsDTO> getPermissionResults()
    {
        return this.permissionResults;
    }
}
