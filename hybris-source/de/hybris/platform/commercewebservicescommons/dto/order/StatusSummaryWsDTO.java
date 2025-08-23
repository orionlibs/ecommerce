package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "StatusSummary", description = "Representation of a status summary, an aggregated view on issues for a specific status or severity. These issues are attached to configurations of products or order entries")
public class StatusSummaryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "status", value = "Status or severity indicator, can be one of ERROR, WARNING, INFO or SUCCESS", example = "ERROR")
    private String status;
    @ApiModelProperty(name = "numberOfIssues", value = "Number of issues per status", example = "3")
    private Integer numberOfIssues;


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setNumberOfIssues(Integer numberOfIssues)
    {
        this.numberOfIssues = numberOfIssues;
    }


    public Integer getNumberOfIssues()
    {
        return this.numberOfIssues;
    }
}
