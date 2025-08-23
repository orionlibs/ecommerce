package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OrderApprovalPermissionType", description = "Request body fields required and optional to operate on Order Approval Permission Type data. Typical permission types are: B2BBudgetExceededPermission, B2BOrderThresholdTimespanPermission and B2BOrderThresholdPermission")
public class B2BPermissionTypeWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the Order Approval Permission Type", required = true, example = "B2BOrderThresholdTimespanPermission")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the Order Approval Permission Type", example = "Allowed Order Threshold (per timespan)")
    private String name;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }
}
