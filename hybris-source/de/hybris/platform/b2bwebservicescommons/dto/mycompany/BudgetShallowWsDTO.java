package de.hybris.platform.b2bwebservicescommons.dto.mycompany;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "BudgetShallow", description = "Request body fields required and optional to operate on Budget data. This bean is shallow, which means its potential relationship fields to other Org Unit fields are simple ids. No Org Unit WsDTO should be declared in this bean to avoid circular references.")
public class BudgetShallowWsDTO extends BudgetBaseWsDTO
{
}
