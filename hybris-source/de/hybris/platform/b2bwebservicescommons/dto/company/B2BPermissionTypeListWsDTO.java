package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderApprovalPermissionTypeList", description = "Representation of an Order Approval Permission Type list")
public class B2BPermissionTypeListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderApprovalPermissionTypes", value = "List of Order Approval Permission Types", required = true)
    private List<B2BPermissionTypeWsDTO> orderApprovalPermissionTypes;


    public void setOrderApprovalPermissionTypes(List<B2BPermissionTypeWsDTO> orderApprovalPermissionTypes)
    {
        this.orderApprovalPermissionTypes = orderApprovalPermissionTypes;
    }


    public List<B2BPermissionTypeWsDTO> getOrderApprovalPermissionTypes()
    {
        return this.orderApprovalPermissionTypes;
    }
}
