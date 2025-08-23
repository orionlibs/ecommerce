package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "B2BApprovalProcess", description = "Representation of an organizational approval process")
public class B2BApprovalProcessWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the Organizational Approval Process", required = true, example = "accApproval")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the Organizational Approval Process", example = "Escalation Approval with Merchant Check")
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
