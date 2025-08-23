package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionResultData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.core.enums.OrderStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderHistoryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private OrderStatus status;
    private String statusDisplay;
    private Date placed;
    private String guid;
    private PriceData total;
    private B2BCostCenterData costCenter;
    private String purchaseOrderNumber;
    private List<B2BPermissionResultData> b2bPermissionResults;
    private List<PrincipalData> managers;
    private List<OrderEntryData> entries;
    private List<ConsignmentData> consignments;
    private String erpOrderNumber;
    private Integer totalEntries;
    private String creationTime;
    private String totalPrice;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setStatus(OrderStatus status)
    {
        this.status = status;
    }


    public OrderStatus getStatus()
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


    public void setTotal(PriceData total)
    {
        this.total = total;
    }


    public PriceData getTotal()
    {
        return this.total;
    }


    public void setCostCenter(B2BCostCenterData costCenter)
    {
        this.costCenter = costCenter;
    }


    public B2BCostCenterData getCostCenter()
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


    public void setB2bPermissionResults(List<B2BPermissionResultData> b2bPermissionResults)
    {
        this.b2bPermissionResults = b2bPermissionResults;
    }


    public List<B2BPermissionResultData> getB2bPermissionResults()
    {
        return this.b2bPermissionResults;
    }


    public void setManagers(List<PrincipalData> managers)
    {
        this.managers = managers;
    }


    public List<PrincipalData> getManagers()
    {
        return this.managers;
    }


    public void setEntries(List<OrderEntryData> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryData> getEntries()
    {
        return this.entries;
    }


    public void setConsignments(List<ConsignmentData> consignments)
    {
        this.consignments = consignments;
    }


    public List<ConsignmentData> getConsignments()
    {
        return this.consignments;
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
}
