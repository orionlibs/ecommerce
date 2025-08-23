package de.hybris.platform.b2bwebservicescommons.dto.order;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionTypeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.PrincipalWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderApprovalRecord", description = "Details of one order approval record")
public class OrderApprovalRecordWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "permissionTypes", value = "List of permission types related to this approval record.")
    private List<B2BPermissionTypeWsDTO> permissionTypes;
    @ApiModelProperty(name = "approver", value = "Principal responsible to this approval record.")
    private PrincipalWsDTO approver;
    @ApiModelProperty(name = "statusDisplay", value = "Status of the order approval.", example = "Pending approval")
    private String statusDisplay;
    @ApiModelProperty(name = "comments", value = "Any comments the approver (or the workflow system) added to the approval item.", example = "Submitted for approval")
    private String comments;


    public void setPermissionTypes(List<B2BPermissionTypeWsDTO> permissionTypes)
    {
        this.permissionTypes = permissionTypes;
    }


    public List<B2BPermissionTypeWsDTO> getPermissionTypes()
    {
        return this.permissionTypes;
    }


    public void setApprover(PrincipalWsDTO approver)
    {
        this.approver = approver;
    }


    public PrincipalWsDTO getApprover()
    {
        return this.approver;
    }


    public void setStatusDisplay(String statusDisplay)
    {
        this.statusDisplay = statusDisplay;
    }


    public String getStatusDisplay()
    {
        return this.statusDisplay;
    }


    public void setComments(String comments)
    {
        this.comments = comments;
    }


    public String getComments()
    {
        return this.comments;
    }
}
