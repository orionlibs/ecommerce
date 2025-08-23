package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "OrderHistory", description = "Representation of an Order History")
public class OrderHistoryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of Order History")
    private String code;
    @ApiModelProperty(name = "status", value = "Status of Order History")
    private String status;
    @ApiModelProperty(name = "statusDisplay", value = "Status display")
    private String statusDisplay;
    @ApiModelProperty(name = "placed", value = "Date of placing order")
    private Date placed;
    @ApiModelProperty(name = "guid", value = "Guest user identifier")
    private String guid;
    @ApiModelProperty(name = "total", value = "Total price")
    private PriceWsDTO total;
    @ApiModelProperty(name = "costCenter", value = "Order Cost Center")
    private B2BCostCenterWsDTO costCenter;
    @ApiModelProperty(name = "purchaseOrderNumber", value = "Purchase order number")
    private String purchaseOrderNumber;
    @ApiModelProperty(name = "entries", value = "List of order entries")
    private List<OrderEntryWsDTO> entries;
    @ApiModelProperty(name = "erpOrderNumber")
    private String erpOrderNumber;
    @ApiModelProperty(name = "totalEntries")
    private Integer totalEntries;
    @ApiModelProperty(name = "creationTime")
    private String creationTime;
    @ApiModelProperty(name = "totalPrice")
    private String totalPrice;
    @ApiModelProperty(name = "consignments", value = "List of consignment")
    private List<ConsignmentWsDTO> consignments;


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


    public void setStatusDisplay(String statusDisplay)
    {
        this.statusDisplay = statusDisplay;
    }


    public String getStatusDisplay()
    {
        return this.statusDisplay;
    }


    public void setPlaced(Date placed)
    {
        this.placed = placed;
    }


    public Date getPlaced()
    {
        return this.placed;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public String getGuid()
    {
        return this.guid;
    }


    public void setTotal(PriceWsDTO total)
    {
        this.total = total;
    }


    public PriceWsDTO getTotal()
    {
        return this.total;
    }


    public void setCostCenter(B2BCostCenterWsDTO costCenter)
    {
        this.costCenter = costCenter;
    }


    public B2BCostCenterWsDTO getCostCenter()
    {
        return this.costCenter;
    }


    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber()
    {
        return this.purchaseOrderNumber;
    }


    public void setEntries(List<OrderEntryWsDTO> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryWsDTO> getEntries()
    {
        return this.entries;
    }


    public void setErpOrderNumber(String erpOrderNumber)
    {
        this.erpOrderNumber = erpOrderNumber;
    }


    public String getErpOrderNumber()
    {
        return this.erpOrderNumber;
    }


    public void setTotalEntries(Integer totalEntries)
    {
        this.totalEntries = totalEntries;
    }


    public Integer getTotalEntries()
    {
        return this.totalEntries;
    }


    public void setCreationTime(String creationTime)
    {
        this.creationTime = creationTime;
    }


    public String getCreationTime()
    {
        return this.creationTime;
    }


    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public String getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setConsignments(List<ConsignmentWsDTO> consignments)
    {
        this.consignments = consignments;
    }


    public List<ConsignmentWsDTO> getConsignments()
    {
        return this.consignments;
    }
}
