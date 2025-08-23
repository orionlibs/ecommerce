package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "B2BCostCenterShallow", description = "Representation of a cost center. This bean is shallow, which means its potential relationship fields to other Org Unit fields are simple ids. No Org Unit WsDTO fields should be declared in this bean to avoid circular references.")
public class B2BCostCenterShallowWsDTO extends B2BCostCenterBaseWsDTO
{
}
