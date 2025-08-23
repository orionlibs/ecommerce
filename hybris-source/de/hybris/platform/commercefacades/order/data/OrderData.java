package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCommentData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.data.TriggerData;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionResultData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import java.util.Date;
import java.util.List;

public class OrderData extends AbstractOrderData
{
    private Date created;
    private OrderStatus status;
    private String statusDisplay;
    private boolean guestCustomer;
    private List<ConsignmentData> consignments;
    private DeliveryStatus deliveryStatus;
    private String deliveryStatusDisplay;
    private List<OrderEntryData> unconsignedEntries;
    private String placedBy;
    private String quoteCode;
    private boolean cancellable;
    private boolean returnable;
    private B2BCostCenterData costCenter;
    private B2BPaymentTypeData paymentType;
    private B2BCommentData b2BComment;
    private CustomerData b2bCustomerData;
    private List<B2BCommentData> b2bCommentData;
    private Date quoteExpirationDate;
    private String purchaseOrderNumber;
    private TriggerData triggerData;
    private List<B2BPermissionResultData> b2bPermissionResult;
    private String jobCode;


    public void setCreated(Date created)
    {
        this.created = created;
    }


    public Date getCreated()
    {
        return this.created;
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


    public void setGuestCustomer(boolean guestCustomer)
    {
        this.guestCustomer = guestCustomer;
    }


    public boolean isGuestCustomer()
    {
        return this.guestCustomer;
    }


    public void setConsignments(List<ConsignmentData> consignments)
    {
        this.consignments = consignments;
    }


    public List<ConsignmentData> getConsignments()
    {
        return this.consignments;
    }


    public void setDeliveryStatus(DeliveryStatus deliveryStatus)
    {
        this.deliveryStatus = deliveryStatus;
    }


    public DeliveryStatus getDeliveryStatus()
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


    public void setUnconsignedEntries(List<OrderEntryData> unconsignedEntries)
    {
        this.unconsignedEntries = unconsignedEntries;
    }


    public List<OrderEntryData> getUnconsignedEntries()
    {
        return this.unconsignedEntries;
    }


    public void setPlacedBy(String placedBy)
    {
        this.placedBy = placedBy;
    }


    public String getPlacedBy()
    {
        return this.placedBy;
    }


    public void setQuoteCode(String quoteCode)
    {
        this.quoteCode = quoteCode;
    }


    public String getQuoteCode()
    {
        return this.quoteCode;
    }


    public void setCancellable(boolean cancellable)
    {
        this.cancellable = cancellable;
    }


    public boolean isCancellable()
    {
        return this.cancellable;
    }


    public void setReturnable(boolean returnable)
    {
        this.returnable = returnable;
    }


    public boolean isReturnable()
    {
        return this.returnable;
    }


    public void setCostCenter(B2BCostCenterData costCenter)
    {
        this.costCenter = costCenter;
    }


    public B2BCostCenterData getCostCenter()
    {
        return this.costCenter;
    }


    public void setPaymentType(B2BPaymentTypeData paymentType)
    {
        this.paymentType = paymentType;
    }


    public B2BPaymentTypeData getPaymentType()
    {
        return this.paymentType;
    }


    public void setB2BComment(B2BCommentData b2BComment)
    {
        this.b2BComment = b2BComment;
    }


    public B2BCommentData getB2BComment()
    {
        return this.b2BComment;
    }


    public void setB2bCustomerData(CustomerData b2bCustomerData)
    {
        this.b2bCustomerData = b2bCustomerData;
    }


    public CustomerData getB2bCustomerData()
    {
        return this.b2bCustomerData;
    }


    public void setB2bCommentData(List<B2BCommentData> b2bCommentData)
    {
        this.b2bCommentData = b2bCommentData;
    }


    public List<B2BCommentData> getB2bCommentData()
    {
        return this.b2bCommentData;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public void setQuoteExpirationDate(Date quoteExpirationDate)
    {
        this.quoteExpirationDate = quoteExpirationDate;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public Date getQuoteExpirationDate()
    {
        return this.quoteExpirationDate;
    }


    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber()
    {
        return this.purchaseOrderNumber;
    }


    public void setTriggerData(TriggerData triggerData)
    {
        this.triggerData = triggerData;
    }


    public TriggerData getTriggerData()
    {
        return this.triggerData;
    }


    public void setB2bPermissionResult(List<B2BPermissionResultData> b2bPermissionResult)
    {
        this.b2bPermissionResult = b2bPermissionResult;
    }


    public List<B2BPermissionResultData> getB2bPermissionResult()
    {
        return this.b2bPermissionResult;
    }


    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode;
    }


    public String getJobCode()
    {
        return this.jobCode;
    }
}
