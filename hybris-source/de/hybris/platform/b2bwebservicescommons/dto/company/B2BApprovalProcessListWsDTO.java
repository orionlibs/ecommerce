package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "B2BApprovalProcessList", description = "Representation of an organizational approval process list")
public class B2BApprovalProcessListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "approvalProcesses", value = "List of Organizational Approval Process", required = true)
    private List<B2BApprovalProcessWsDTO> approvalProcesses;


    public void setApprovalProcesses(List<B2BApprovalProcessWsDTO> approvalProcesses)
    {
        this.approvalProcesses = approvalProcesses;
    }


    public List<B2BApprovalProcessWsDTO> getApprovalProcesses()
    {
        return this.approvalProcesses;
    }
}
