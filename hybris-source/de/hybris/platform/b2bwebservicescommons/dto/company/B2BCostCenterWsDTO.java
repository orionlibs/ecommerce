package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.b2bwebservicescommons.dto.mycompany.BudgetShallowWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(value = "B2BCostCenter", description = "Representation of a cost center. This bean may have relationships to other Org Unit WsDTO, whose type are WsDTO beans as well.")
public class B2BCostCenterWsDTO extends B2BCostCenterBaseWsDTO
{
    @ApiModelProperty(name = "unit", value = "The parent unit of the cost center")
    private B2BUnitWsDTO unit;
    @ApiModelProperty(name = "assignedBudgets", value = "Budgets assigned to this cost center")
    private List<BudgetShallowWsDTO> assignedBudgets;


    public void setUnit(B2BUnitWsDTO unit)
    {
        this.unit = unit;
    }


    public B2BUnitWsDTO getUnit()
    {
        return this.unit;
    }


    public void setAssignedBudgets(List<BudgetShallowWsDTO> assignedBudgets)
    {
        this.assignedBudgets = assignedBudgets;
    }


    public List<BudgetShallowWsDTO> getAssignedBudgets()
    {
        return this.assignedBudgets;
    }
}
