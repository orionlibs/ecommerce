package de.hybris.platform.b2bwebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OrderApprovalDecision", description = "Decision for an order approval")
public class OrderApprovalDecisionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "decision", value = "Decision from the approver when approving/rejecting an order. Typical decisions are: APPROVE, REJECT", required = true, example = "APPROVE")
    private String decision;
    @ApiModelProperty(name = "comment", value = "Any comments the approver (or the workflow system) adds when approving/rejecting an order.", example = "Rejected because montly budget was exceeded")
    private String comment;


    public void setDecision(String decision)
    {
        this.decision = decision;
    }


    public String getDecision()
    {
        return this.decision;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }
}
