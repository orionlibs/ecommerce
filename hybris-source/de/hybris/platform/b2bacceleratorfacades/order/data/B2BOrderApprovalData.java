package de.hybris.platform.b2bacceleratorfacades.order.data;

import de.hybris.platform.commercefacades.order.data.OrderData;
import java.io.Serializable;
import java.util.List;

public class B2BOrderApprovalData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String workflowActionModelCode;
    private OrderData b2bOrderData;
    private List<String> permissionTypes;
    private List<String> allDecisions;
    private String selectedDecision;
    private String approvalComments;
    private boolean approvalDecisionRequired;
    private List<B2BOrderHistoryEntryData> orderHistoryEntriesData;
    private List<B2BOrderHistoryEntryData> quotesApprovalHistoryEntriesData;
    private List<B2BOrderHistoryEntryData> merchantApprovalHistoryEntriesData;


    public void setWorkflowActionModelCode(String workflowActionModelCode)
    {
        this.workflowActionModelCode = workflowActionModelCode;
    }


    public String getWorkflowActionModelCode()
    {
        return this.workflowActionModelCode;
    }


    public void setB2bOrderData(OrderData b2bOrderData)
    {
        this.b2bOrderData = b2bOrderData;
    }


    public OrderData getB2bOrderData()
    {
        return this.b2bOrderData;
    }


    public void setPermissionTypes(List<String> permissionTypes)
    {
        this.permissionTypes = permissionTypes;
    }


    public List<String> getPermissionTypes()
    {
        return this.permissionTypes;
    }


    public void setAllDecisions(List<String> allDecisions)
    {
        this.allDecisions = allDecisions;
    }


    public List<String> getAllDecisions()
    {
        return this.allDecisions;
    }


    public void setSelectedDecision(String selectedDecision)
    {
        this.selectedDecision = selectedDecision;
    }


    public String getSelectedDecision()
    {
        return this.selectedDecision;
    }


    public void setApprovalComments(String approvalComments)
    {
        this.approvalComments = approvalComments;
    }


    public String getApprovalComments()
    {
        return this.approvalComments;
    }


    public void setApprovalDecisionRequired(boolean approvalDecisionRequired)
    {
        this.approvalDecisionRequired = approvalDecisionRequired;
    }


    public boolean isApprovalDecisionRequired()
    {
        return this.approvalDecisionRequired;
    }


    public void setOrderHistoryEntriesData(List<B2BOrderHistoryEntryData> orderHistoryEntriesData)
    {
        this.orderHistoryEntriesData = orderHistoryEntriesData;
    }


    public List<B2BOrderHistoryEntryData> getOrderHistoryEntriesData()
    {
        return this.orderHistoryEntriesData;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public void setQuotesApprovalHistoryEntriesData(List<B2BOrderHistoryEntryData> quotesApprovalHistoryEntriesData)
    {
        this.quotesApprovalHistoryEntriesData = quotesApprovalHistoryEntriesData;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public List<B2BOrderHistoryEntryData> getQuotesApprovalHistoryEntriesData()
    {
        return this.quotesApprovalHistoryEntriesData;
    }


    public void setMerchantApprovalHistoryEntriesData(List<B2BOrderHistoryEntryData> merchantApprovalHistoryEntriesData)
    {
        this.merchantApprovalHistoryEntriesData = merchantApprovalHistoryEntriesData;
    }


    public List<B2BOrderHistoryEntryData> getMerchantApprovalHistoryEntriesData()
    {
        return this.merchantApprovalHistoryEntriesData;
    }
}
