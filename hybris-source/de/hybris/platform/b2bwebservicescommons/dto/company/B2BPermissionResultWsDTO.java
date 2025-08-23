package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OrderApprovalPermissionResult", description = "Details of an order approval permission result related to an order")
public class B2BPermissionResultWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "approverName", value = "Name of the approver responsible for this permission result.")
    private String approverName;
    @ApiModelProperty(name = "approverNotes", value = "Any comments the approver added to the approval item.", example = "The order is rejected due to insufficient budget")
    private String approverNotes;
    @ApiModelProperty(name = "permissionType", value = "Permission type related to the permission result.")
    private B2BPermissionTypeWsDTO permissionType;
    @ApiModelProperty(name = "statusDisplay", value = "Status of the order approval.", example = "Pending approval")
    private String statusDisplay;


    public void setApproverName(String approverName)
    {
        this.approverName = approverName;
    }


    public String getApproverName()
    {
        return this.approverName;
    }


    public void setApproverNotes(String approverNotes)
    {
        this.approverNotes = approverNotes;
    }


    public String getApproverNotes()
    {
        return this.approverNotes;
    }


    public void setPermissionType(B2BPermissionTypeWsDTO permissionType)
    {
        this.permissionType = permissionType;
    }


    public B2BPermissionTypeWsDTO getPermissionType()
    {
        return this.permissionType;
    }


    public void setStatusDisplay(String statusDisplay)
    {
        this.statusDisplay = statusDisplay;
    }


    public String getStatusDisplay()
    {
        return this.statusDisplay;
    }
}
