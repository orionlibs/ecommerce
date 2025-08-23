package de.hybris.platform.b2bwebservicescommons.dto.mycompany;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(value = "Budget", description = "Request body fields required and optional to operate on Budget data. This bean may have relationships to other Org Unit WsDTO, whose type are WsDTO beans as well.")
public class BudgetWsDTO extends BudgetBaseWsDTO
{
    @ApiModelProperty(name = "orgUnit", value = "The unit of the Budget", example = "Rustic")
    private B2BUnitWsDTO orgUnit;
    @ApiModelProperty(name = "costCenters", value = "List of Cost Centers")
    private List<B2BCostCenterWsDTO> costCenters;


    public void setOrgUnit(B2BUnitWsDTO orgUnit)
    {
        this.orgUnit = orgUnit;
    }


    public B2BUnitWsDTO getOrgUnit()
    {
        return this.orgUnit;
    }


    public void setCostCenters(List<B2BCostCenterWsDTO> costCenters)
    {
        this.costCenters = costCenters;
    }


    public List<B2BCostCenterWsDTO> getCostCenters()
    {
        return this.costCenters;
    }
}
