package de.hybris.platform.b2bwebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderApproval", description = "Details of one specific order approval")
public class OrderApprovalWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code that identifies the approval.", required = true, example = "00000005")
    private String code;
    @ApiModelProperty(name = "order", value = "Order linked to this approval.")
    private OrderWsDTO order;
    @ApiModelProperty(name = "approvalDecisionRequired", value = "Boolean flag which states whether an approval decision is required.")
    private Boolean approvalDecisionRequired;
    @ApiModelProperty(name = "customerOrderApprovalRecords", value = "Customer approval records related to this order approval.")
    private List<OrderApprovalRecordWsDTO> customerOrderApprovalRecords;
    @ApiModelProperty(name = "merchantOrderApprovalRecords", value = "Merchant approval records related to this order approval.")
    private List<OrderApprovalRecordWsDTO> merchantOrderApprovalRecords;
    @ApiModelProperty(name = "trigger", value = "Replenishment trigger if this is an approval for a replenishment order.")
    private TriggerWsDTO trigger;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setOrder(OrderWsDTO order)
    {
        this.order = order;
    }


    public OrderWsDTO getOrder()
    {
        return this.order;
    }


    public void setApprovalDecisionRequired(Boolean approvalDecisionRequired)
    {
        this.approvalDecisionRequired = approvalDecisionRequired;
    }


    public Boolean getApprovalDecisionRequired()
    {
        return this.approvalDecisionRequired;
    }


    public void setCustomerOrderApprovalRecords(List<OrderApprovalRecordWsDTO> customerOrderApprovalRecords)
    {
        this.customerOrderApprovalRecords = customerOrderApprovalRecords;
    }


    public List<OrderApprovalRecordWsDTO> getCustomerOrderApprovalRecords()
    {
        return this.customerOrderApprovalRecords;
    }


    public void setMerchantOrderApprovalRecords(List<OrderApprovalRecordWsDTO> merchantOrderApprovalRecords)
    {
        this.merchantOrderApprovalRecords = merchantOrderApprovalRecords;
    }


    public List<OrderApprovalRecordWsDTO> getMerchantOrderApprovalRecords()
    {
        return this.merchantOrderApprovalRecords;
    }


    public void setTrigger(TriggerWsDTO trigger)
    {
        this.trigger = trigger;
    }


    public TriggerWsDTO getTrigger()
    {
        return this.trigger;
    }
}
